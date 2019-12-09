package bugtracker.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "PROJECT")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDefaultApproverId() {
        return defaultApproverId;
    }

    public void setDefaultApproverId(Long defaultApproverId) {
        this.defaultApproverId = defaultApproverId;
    }

    public Long getDefaultDeveloperId() {
        return defaultDeveloperId;
    }

    public void setDefaultDeveloperId(Long defaultDeveloperId) {
        this.defaultDeveloperId = defaultDeveloperId;
    }

    public Long getS1Time() {
        return s1Time;
    }

    public void setS1Time(Long s1Time) {
        this.s1Time = s1Time;
    }

    public Long getS2Time() {
        return s2Time;
    }

    public void setS2Time(Long s2Time) {
        this.s2Time = s2Time;
    }

    public Long getS3Time() {
        return s3Time;
    }

    public void setS3Time(Long s3Time) {
        this.s3Time = s3Time;
    }
}
