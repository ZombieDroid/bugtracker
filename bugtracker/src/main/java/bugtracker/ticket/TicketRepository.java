package bugtracker.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAll();

    TicketEntity findTicketEntityById(Long id);

    List<TicketEntity> findByNameContaining(String text);
    List<TicketEntity> findByDescriptionContaining(String text);
}
