package bugtracker.comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Inject
    CommentService commentService;

    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody()
    public ResponseEntity addNewComment(@RequestBody CommentEntity commentEntity) {
        try {
            commentService.createComment(commentEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all/{ticketId}")
    public ResponseEntity<List<CommentEntity>> getAllByTicketID(@PathVariable Long ticketId) {
        return new ResponseEntity<>(commentService.getAllByTicketId(ticketId), HttpStatus.OK);
    }
}
