package bugtracker.ticket;

import bugtracker.history.HistoryEntity;
import bugtracker.history.HistoryService;
import bugtracker.status.StatusEntity;
import bugtracker.status.StatusService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.List;

@Service
public class TicketService {

    @Inject
    TicketRepository ticketRepository;

    @Inject
    StatusService statusService;

    @Inject
    HistoryService historyService;

    public TicketEntity createTicket(TicketEntity ticket){
        return ticketRepository.save(ticket);
    }

    public List<TicketEntity> getAllTicket(){
        return ticketRepository.findAll();
    }

    public TicketEntity getTicketById(Long id){
        return ticketRepository.findTicketEntityById(id);
    }

    public void saveTicket(TicketEntity ticket, String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        HistoryEntity historyEntity = new HistoryEntity();

        TicketEntity ticketEntity = ticketRepository.save(ticket);

        //project or ticket id
        historyEntity.setObjectId(ticket.getId());
        //initial status
        historyEntity.setInitialStatusId(133L);
        historyEntity.setTargetStatusId(ticket.getStatusId());
        //history text
        StatusEntity statusEntity = statusService.getStatusById(ticket.getStatusId());
        historyEntity.setFreeText("The modified ticket status: " + statusEntity.getName() + ", modified by:" + currentPrincipalName);
        //history
        historyEntity.setEventDescription( statusEntity.getName());
        historyService.createHistory(historyEntity);

        ticketRepository.save(ticket);
    }

    public List<TicketEntity> searchByName(String text) { return ticketRepository.findByNameContaining(text); }
    public List<TicketEntity> searchByDescription(String text) { return ticketRepository.findByDescriptionContaining(text); }

}
