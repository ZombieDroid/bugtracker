package bugtracker.project;

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
    public static final String UPDATE = "update";
    public static final String CREATE = "create";

    // todo: move related logic to ProjectService!

    @Inject
    ProjectService projectService;

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity addNewProject(@RequestBody ProjectEntity project) {
        try {
            projectService.saveProjectWithHistory(project, CREATE);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProject(@RequestBody ProjectEntity project) {
        try{
            projectService.saveProjectWithHistory(project, UPDATE);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProject(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("project");
        try{
            return new ResponseEntity<>(projectService.getProjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @GetMapping("/searchProjects/")
    public ResponseEntity<List<ProjectEntity>> searchAllProjects(){
        return getAll();
    }

    @GetMapping("/searchProjects/{searchText}")
    public ResponseEntity<List<ProjectEntity>> searchProjects(@PathVariable String searchText){
        List<ProjectEntity> projects = projectService.searchByName(searchText);
        List<ProjectEntity> tmpProjs = projectService.searchByDescription(searchText);

        for(ProjectEntity proj : tmpProjs){
            if(!projects.contains(proj)){
                projects.add(proj);
            }
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
