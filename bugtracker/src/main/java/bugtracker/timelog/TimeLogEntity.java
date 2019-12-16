package bugtracker.timelog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "TIMELOG")
public class TimeLogEntity {
    @Id
    @GeneratedValue
    private long id;
    private Long time;
    private Long userId;
    private Long ticketId;
    private String description;
    private LocalDate logdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time){
        this.time = time;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return logdate;
    }

    public void setDate(LocalDate date) {
        this.logdate = date;
    }
}
