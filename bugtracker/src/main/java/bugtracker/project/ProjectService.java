package bugtracker.project;

import bugtracker.history.HistoryEntity;
import bugtracker.history.HistoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    HistoryService historyService;


    public List<ProjectEntity> getAllProject(){
        return projectRepository.findAll();
    }
    public ProjectEntity getProjectById(Long id){
        return projectRepository.findProjectEntityById(id);
    }
    public List<ProjectEntity> searchByName(String text) { return projectRepository.findByNameContaining(text); }
    public List<ProjectEntity> searchByDescription(String text) { return projectRepository.findByDescriptionContaining(text); }

    public ProjectEntity saveProjectWithHistory(ProjectEntity project, String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        HistoryEntity historyEntity = new HistoryEntity();
        ProjectEntity projectEntity = projectRepository.save(project);

        //project or ticket id
        historyEntity.setProjectId(projectEntity.getId());
        historyEntity.setCreatedAt(LocalDateTime.now());
        //initial status
        historyEntity.setInitialStatusId(133L);
        historyEntity.setTargetStatusId(133L);
        //history text
        historyEntity.setFreeText("The project status: " + type + ", modified by:" + currentPrincipalName);
        //history
        historyEntity.setEventDescription(type);
        historyService.createHistory(historyEntity);

        return projectEntity;
    }
}
