package bugtracker.ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Table(name = "TICKET")
public class TicketEntity {

    @Id
    @GeneratedValue
    private long id;
    private Long priority;
    private String description;
    private Long reporterId;
    private Long statusId;
    private Long projectId;
    private LocalDateTime spentTime;
    private Long type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(LocalDateTime spentTime) {
        this.spentTime = spentTime;
    }
}
