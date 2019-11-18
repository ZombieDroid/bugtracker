package bugtracker.ticket;

import bugtracker.project.ProjectEntity;
import bugtracker.project.ProjectService;
import bugtracker.user.UserEntity;
import bugtracker.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Inject
    TicketService ticketService;

    @Inject
    UserService userService;

    @Inject
    ProjectService projectService;

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity addNewTicket(@RequestBody TicketEntity ticket) {
        if(ticket.getName().isEmpty()){
            return new ResponseEntity<>("Ticket must have a name", HttpStatus.BAD_REQUEST);
        }
        try {
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

    @GetMapping("/getStatus/{id}")
    public ResponseEntity<String> getStatusById(@PathVariable long id) throws Exception {
        String status = "";
        switch((short)id){
            case 0: status = "Open";
                break;
            case 1: status = "In Progress";
                break;
            case 2: status = "Done";
                break;
            default:
                throw new Exception("Unknown status: " + id);
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
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
}
