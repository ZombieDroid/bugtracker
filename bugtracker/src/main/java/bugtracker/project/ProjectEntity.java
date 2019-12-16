package bugtracker.project;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROJECT")
@Data
public class ProjectEntity {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private Long defaultApproverId;
    private Long defaultDeveloperId;
    private Long s1Time;
    private Long s2Time;
    private Long s3Time;
}
