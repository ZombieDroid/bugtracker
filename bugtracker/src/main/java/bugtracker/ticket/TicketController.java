package bugtracker.ticket;

import bugtracker.project.ProjectEntity;
import bugtracker.project.ProjectService;
import bugtracker.status.StatusEntity;
import bugtracker.status.StatusService;
import bugtracker.status.StatusWorkflow;
import bugtracker.user.UserEntity;
import bugtracker.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Inject
    TicketService ticketService;

    @Inject
    UserService userService;

    @Inject
    ProjectService projectService;

    @Inject
    StatusService statusService;

    @AllArgsConstructor
    @Getter
    private static class NextStatus
    {
        private Long status;
        private Long userType;
        private Long ticketType;
    }

    private static final Hashtable<Long, List<NextStatus>> nextStatusTable;

    static{
        nextStatusTable = new Hashtable<>();

        nextStatusTable.put(133L,
                Arrays.asList(new NextStatus(130L,1L,0L), new NextStatus(129L,3L,1L), new NextStatus(134L,3L,1L)));

        nextStatusTable.put(134L, Arrays.asList(new NextStatus(133L,2L,1L)));

        nextStatusTable.put(129L, Arrays.asList(new NextStatus(130L,1L,1L)));

        nextStatusTable.put(130L, Arrays.asList(new NextStatus(132L,1L,2L)));

        nextStatusTable.put(132L, Arrays.asList(new NextStatus(133L,2L,2L), new NextStatus(131L,2L,2L)));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity addNewTicket(@RequestBody TicketEntity ticket) {
        if(ticket.getName().isEmpty()){
            return new ResponseEntity<>("Ticket must have a name", HttpStatus.BAD_REQUEST);
        }
        try {
            ticket.setStatusId(133L);    // pending
            ticketService.createTicket(ticket);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketEntity> getTicket(@PathVariable Long id) {
        try{
            return new ResponseEntity<>(ticketService.getTicketById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateTicket(@RequestBody TicketEntity ticket) {
        TicketEntity currTicket = ticketService.getTicketById(ticket.getId());
        if(!currTicket.getStatusId().equals(ticket.getStatusId())){
            boolean isValidNextStatus = false;
            for(StatusWorkflow.NextStatus ns : StatusWorkflow.getNextPossibleStatuses(currTicket.getStatusId())){
                if(ticket.getStatusId().equals(ns.getStatus())){
                    isValidNextStatus = true;
                    break;
                }
            }
            if(!isValidNextStatus){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        try{
            ticketService.saveTicket(ticket);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public ModelAndView getAllTicket() {
        return new ModelAndView("tickets");
    }

    @GetMapping("/getReporter/{id}")
    public ResponseEntity<String> getReporterById(@PathVariable Long id){
        String name = "";
        UserEntity user = userService.getUserById(id);
        if(user != null){
            name = user.getName();
        }
        return new ResponseEntity<>(name, HttpStatus.OK);
    }

    @GetMapping("/getProject/{id}")
    public ResponseEntity<String> getProjectById(@PathVariable Long id){
        String name = "";
        ProjectEntity project = projectService.getProjectById(id);
        if(project != null){
            name = project.getName();
        }
        return new ResponseEntity<>(name, HttpStatus.OK);
    }

    @GetMapping("/getType/{id}")
    public ResponseEntity<String> getTypeById(@PathVariable long id) throws Exception {
        String type = "";
        switch((short)id){
            case 0: type = "Feature";
                break;
            case 1: type = "Bug";
                break;
            default:
                throw new Exception("Unknown type: " + id);
        }
        return new ResponseEntity<>(type, HttpStatus.OK);
    }

    @GetMapping("/searchTickets/")
    public ResponseEntity<List<TicketEntity>> getAllTickets(){
        return searchTickets("");
    }

    @GetMapping("/searchTickets/{searchText}")
    public ResponseEntity<List<TicketEntity>> searchTickets(@PathVariable String searchText){
        List<TicketEntity> tickets = ticketService.searchByName(searchText);
        List<TicketEntity> tmpTickets = ticketService.searchByDescription(searchText);

        for(TicketEntity ticket : tmpTickets){
            if(!tickets.contains(ticket)){
                tickets.add(ticket);
            }
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("validStatuses/{ticketId}")
    public ResponseEntity<List<StatusEntity>> getValidStatuses(@PathVariable Long ticketId){
        List<StatusEntity> statuses = new LinkedList<>();
        try{
            TicketEntity ticket = ticketService.getTicketById(ticketId);
            statuses.add(statusService.getStatusById(ticket.getStatusId()));
            if(isCurrentUserAssignedToTicket(ticket)){
                StatusEntity ticketStatus = statusService.getStatusById(ticket.getStatusId());
                for(StatusWorkflow.NextStatus ns : StatusWorkflow.getNextPossibleStatuses(ticketStatus.getId())){
                    if(isCurrentUserValid(ns.getUserType()) &&
                            (ticket.getType().equals(ns.getTicketType()) || ns.getTicketType().equals(2L))){
                        statuses.add(statusService.getStatusById(ns.getStatus()));
                    }
                }
            }
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    private boolean isCurrentUserValid(Long validId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        UserEntity user = userService.getUserByName(username);

        if(user != null){
            return user.getType() == validId;
        }

        return false;
    }

    private boolean isCurrentUserAssignedToTicket(TicketEntity ticket){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        UserEntity user = userService.getUserByName(username);

        if(user != null){
            return ticket.getReporterId() != null && ticket.getReporterId().equals(user.getId()) ||
                    ticket.getOwnerId() != null && ticket.getOwnerId().equals(user.getId());
        }

        return false;
    }
}
