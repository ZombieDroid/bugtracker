package bugtracker.history;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HISTORY")
public class HistoryEntity {

    @Id
    @GeneratedValue
    private long id;
    private int objectId;
    private String eventDescription;
    private long initialStatusId;
    private long targetStatusId;
    private String freeText;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public long getInitialStatusId() {
        return initialStatusId;
    }

    public void setInitialStatusId(long initialSatusId) {
        this.initialStatusId = initialSatusId;
    }

    public long getTargetStatusId() {
        return targetStatusId;
    }

    public void setTargetStatusId(long targetStatusId) {
        this.targetStatusId = targetStatusId;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }
}
