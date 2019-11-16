package bugtracker.project;

import bugtracker.ticket.TicketEntity;
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
@RequestMapping("/api/project")
public class ProjectController {

    // todo: move related logic to ProjectService!

    @Inject
    ProjectService projectService;

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity addNewProject(@RequestBody ProjectEntity project) {
        try {
            projectService.createProject(project);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ModelAndView getProject(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("project");
        try{
            String projectJson = (new ObjectMapper()).writeValueAsString(projectService.getProjectById(id));
            mv.addObject(projectJson);
        } catch (JsonProcessingException e) {
            mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return mv;
    }

    @GetMapping("/all")
    public ModelAndView getAllProjects() {
        ModelAndView mv = new ModelAndView("projects");
        try{
            String projectJson = (new ObjectMapper()).writeValueAsString(projectService.getAllProject());
            mv.addObject(projectJson);
        } catch (JsonProcessingException e) {
            mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return mv;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProjectEntity>> getAll(){
        return new ResponseEntity<>(projectService.getAllProject(), HttpStatus.OK);
    }
}
