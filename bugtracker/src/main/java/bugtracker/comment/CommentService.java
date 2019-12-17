package bugtracker.comment;

import bugtracker.history.HistoryEntity;
import bugtracker.ticket.TicketEntity;
import bugtracker.ticket.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Inject
    CommentRepository commentRepository;

    @Inject
    TicketService ticketService;



    public CommentEntity createComment(CommentEntity commentEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        HistoryEntity historyEntity = new HistoryEntity();

        TicketEntity ticketEntity = ticketService.getTicketById(commentEntity.getTicketId());

        //project or ticket id
        historyEntity.setObjectId(ticketEntity.getId());
        //initial status
        historyEntity.setInitialStatusId(133L);
        historyEntity.setTargetStatusId(ticketEntity.getStatusId());
        //history text
            historyEntity.setFreeText("New comment added by:" + currentPrincipalName);
        historyEntity.setCreatedAt(LocalDateTime.now());
        //history
        historyEntity.setEventDescription("comment");
        return commentRepository.save(commentEntity);
    }

    public List<CommentEntity> getAllByTicketId(Long ticketId) {
        return commentRepository.findAllByTicketId(ticketId);
    }
}
