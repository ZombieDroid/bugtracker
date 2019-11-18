package bugtracker.project;

import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.List;

@Service
public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    public ProjectEntity createProject(ProjectEntity project){
        return projectRepository.save(project);
    }
    public List<ProjectEntity> getAllProject(){
        return projectRepository.findAll();
    }
    public ProjectEntity getProjectById(Long id){
        return projectRepository.findProjectEntityById(id);
    }
    public List<ProjectEntity> searchByName(String text) { return projectRepository.findByNameContaining(text); }
    public List<ProjectEntity> searchByDescription(String text) { return projectRepository.findByDescriptionContaining(text); }
    public ProjectEntity saveProject(ProjectEntity project) { return projectRepository.save(project);  }
}
