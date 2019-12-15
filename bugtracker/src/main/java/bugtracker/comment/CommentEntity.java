package bugtracker.comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "KOMMENT")
public class CommentEntity {
    @Id
    @GeneratedValue
    private long id;

    private long ticketId;
    private long userId;

    private String commentText;
    private LocalDateTime commentTime;

    public long getId() {
        return id;
    }

    public long getTicketId() {
        return ticketId;
    }

    public long getUserId() {
        return userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public LocalDateTime getCommentTime() {
        return commentTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCommentTime(LocalDateTime commentTime) {
        this.commentTime = commentTime;
    }
}
