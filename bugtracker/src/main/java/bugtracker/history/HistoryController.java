package bugtracker.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/history")
public class HistoryController {


    @Inject
    HistoryService historyService;

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity addNewHistory(@RequestBody HistoryEntity history) {
        try {
            historyService.createHistory(history);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ModelAndView getHistory(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("history");
        try{
            String historyJson = (new ObjectMapper()).writeValueAsString(historyService.getHistoryById(id));
            mv.addObject(historyJson);
        } catch (JsonProcessingException e) {
            mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return mv;
    }

    @GetMapping("/all")
    public ModelAndView getAllHistoryByObjectId(Long objectId) {
        ModelAndView mv = new ModelAndView("historys");
        try{
            String HistoryJson = (new ObjectMapper()).writeValueAsString(historyService.getAllByObjectId(objectId));
            mv.addObject(HistoryJson);
        } catch (JsonProcessingException e) {
            mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return mv;
    }

}
