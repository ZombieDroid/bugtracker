package bugtracker.ticket;

import bugtracker.Comment.CommentEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "TICKET")
public class TicketEntity {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private Long priority;
    private String description;
    private Long reporterId;
    private Long ownerId;
    private Long statusId;
    private Long projectId;
    private LocalDateTime spentTime;
    private Long type;

    @OneToMany(mappedBy = "comment")
    private Set<CommentEntity> comments;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Priority {
        S1,
        S2,
        S3
    }
}
