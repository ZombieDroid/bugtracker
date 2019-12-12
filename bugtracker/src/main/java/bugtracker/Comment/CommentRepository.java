package bugtracker.Comment;

import bugtracker.ticket.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByTicketId(TicketEntity ticketEntity);

    CommentEntity removeByTicketId(TicketEntity ticketEntity);
}
