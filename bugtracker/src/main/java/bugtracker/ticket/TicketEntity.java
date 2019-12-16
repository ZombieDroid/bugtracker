package bugtracker.ticket;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "TICKET")
@Data
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
    private LocalDate creationTime;

}
