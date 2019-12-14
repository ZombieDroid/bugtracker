package bugtracker.timelog;

import bugtracker.ticket.TicketEntity;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class TimeLogService {
    @Inject
    TimeLogRepository timeLogRepository;

    public TimeLogEntity createTimeLog(TimeLogEntity timeLogEntity){
        return timeLogRepository.save(timeLogEntity);
    }

    public List<TimeLogEntity> getAllTimeLog(){
        return timeLogRepository.findAll();
    }

    public TimeLogEntity getTimeLogById(Long id){
        return timeLogRepository.findTimeLogEntityById(id);
    }
}
