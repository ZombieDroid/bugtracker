package bugtracker.status;

import javax.inject.Inject;
import java.util.List;

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
