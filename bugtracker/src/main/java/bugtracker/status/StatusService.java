package bugtracker.status;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
class StatusService {

    @Inject
    StatusRepository statusRepository;

    StatusEntity createStatus(StatusEntity statusEntity) {
        return statusRepository.save(statusEntity);
    }

    StatusEntity getStatusById(Long id) {
        return statusRepository.findStatusById(id);
    }

    List<StatusEntity> getAllStatus() {
        return statusRepository.findAll();
    }

    List<StatusEntity> searchByName(String text) {
        return statusRepository.findByNameContaining(text);
    }
}
