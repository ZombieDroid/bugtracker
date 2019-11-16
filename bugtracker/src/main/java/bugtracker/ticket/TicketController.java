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
    public ModelAndView getTicket(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("ticket");
        try{
            String ticketJson = (new ObjectMapper()).writeValueAsString(ticketService.getTicketById(id));
            mv.addObject(ticketJson);
        } catch (JsonProcessingException e) {
            mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return mv;
    }

    @GetMapping("/all")
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
        List<TicketEntity> tickets = new LinkedList<>();
        List<TicketEntity> allTickets = ticketService.getAllTicket();
        for(TicketEntity ticket : allTickets){
            if((ticket.getName() != null && ticket.getName().contains(searchText)) ||
                    (ticket.getDescription() != null && ticket.getDescription().contains(searchText))){
                tickets.add(ticket);
            }
        }

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}
