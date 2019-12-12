package bugtracker.Comment;

import bugtracker.ticket.TicketEntity;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketEntity ticketId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TicketEntity getTicketId() {
        return ticketId;
    }

    public void setTicketId(TicketEntity ticketId) {
        this.ticketId = ticketId;
    }
}
