package bugtracker.ticket;

import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.List;

@Service
public class TicketService {

    @Inject
    TicketRepository ticketRepository;

    public TicketEntity createTicket(TicketEntity ticket){
        return ticketRepository.save(ticket);
    }

    public List<TicketEntity> getAllTicket(){
        return ticketRepository.findAll();
    }

    public TicketEntity getTicketById(Long id){
        return ticketRepository.findTicketEntityById(id);
    }

}
