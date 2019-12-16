package bugtracker.comment;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class CommentService {

    @Inject
    CommentRepository commentRepository;

    public CommentEntity createComment(CommentEntity commentEntity) {
        return commentRepository.save(commentEntity);
    }

    public List<CommentEntity> getAllByTicketId(Long ticketId) {
        return commentRepository.findAllByTicketId(ticketId);
    }
}
