package bugtracker.status;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class StatusService {

    @Inject
    StatusRepository statusRepository;

    public StatusEntity createStatus(StatusEntity statusEntity) {
        return statusRepository.save(statusEntity);
    }

    public StatusEntity getStatusById(Long id) {
        return statusRepository.findStatusById(id);
    }

    public List<StatusEntity> getAllStatus() {
        return statusRepository.findAll();
    }

    public List<StatusEntity> searchByName(String text) {
        return statusRepository.findByNameContaining(text);
    }
}
