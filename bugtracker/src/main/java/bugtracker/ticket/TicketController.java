package bugtracker.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Inject
    TicketService ticketService;

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity addNewTicet(@RequestBody TicketEntity ticket) {
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
        ModelAndView mv = new ModelAndView("tickets");
        try{
            String ticketJson = (new ObjectMapper()).writeValueAsString(ticketService.getAllTicket());
            mv.addObject("tickets", ticketJson);
        } catch (JsonProcessingException e) {
            mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return mv;
    }
}
