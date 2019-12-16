package bugtracker.history;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HISTORY")
@Data
public class HistoryEntity {

    @Id
    @GeneratedValue
    private long id;
    private Long objectId;
    @Column(name = "project_id", nullable = true)
    private Long projectId;
    private String eventDescription;
    private long initialStatusId;
    private long targetStatusId;
    private String freeText;
    private LocalDateTime createdAt;

}
