package bugtracker.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findAll();
    ProjectEntity findProjectEntityById(Long id);
    List<ProjectEntity> findByNameContaining(String text);
    List<ProjectEntity> findByDescriptionContaining(String text);
}

