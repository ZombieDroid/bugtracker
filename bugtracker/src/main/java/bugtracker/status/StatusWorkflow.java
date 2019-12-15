package bugtracker.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class StatusWorkflow {

    @AllArgsConstructor
    @Getter
    public static class NextStatus
    {
        private Long status;
        private Long userType;
        private Long ticketType;
    }

    private enum Status{
        APPROVED(129L),
        INPROGRESS(130L),
        CLOSED(131L),
        RESOLVED(132L),
        PENDING(133L),
        DECLINED(134L),
        WAITFORRESPONSE(135L),
        POSTPONE(136L);

        public final Long value;
        private Status(Long value){
            this.value = value;
        }
    }

    private enum User{
        ADMIN(0L),
        DEV(1L),
        USER(2L),
        APPROVER(3L);

        public final Long value;
        private User(Long value){
            this.value = value;
        }
    }

    private enum Ticket{
        BUG (0L),
        FEATURE(1L),
        BOTH(2L);

        public final Long value;
        private Ticket(Long value){
            this.value = value;
        }
    }

    private static final Hashtable<Long, List<NextStatus>> nextStatusTable;

    static{
        //key= current status, value= next status 1. toStatus 2. user type, bug/feature/both.
        nextStatusTable = new Hashtable<>();

        nextStatusTable.put(Status.PENDING.value,
                Arrays.asList(new NextStatus(Status.INPROGRESS.value, User.DEV.value,Ticket.BUG.value),
                        new NextStatus(Status.APPROVED.value,User.APPROVER.value,Ticket.FEATURE.value),
                        new NextStatus(Status.DECLINED.value,User.APPROVER.value,Ticket.FEATURE.value)));

        //XXX status, who and ticket type 0 bug, 1 feature, 2 both (need to be fixed)
        nextStatusTable.put(Status.DECLINED.value,
                Arrays.asList(new NextStatus(Status.PENDING.value,User.USER.value,Ticket.FEATURE.value)));

        nextStatusTable.put(Status.APPROVED.value,
                Arrays.asList(new NextStatus(Status.INPROGRESS.value,User.DEV.value,Ticket.FEATURE.value)));

        nextStatusTable.put(Status.INPROGRESS.value,
                Arrays.asList(new NextStatus(Status.RESOLVED.value,User.DEV.value,Ticket.BOTH.value),
                        new NextStatus(Status.WAITFORRESPONSE.value,User.DEV.value,Ticket.BOTH.value),
                        new NextStatus(Status.POSTPONE.value,User.DEV.value,Ticket.BOTH.value)));

        nextStatusTable.put(Status.RESOLVED.value,
                Arrays.asList(new NextStatus(Status.PENDING.value,User.USER.value,Ticket.BOTH.value),
                        new NextStatus(Status.CLOSED.value,User.USER.value,Ticket.BOTH.value)));

        nextStatusTable.put(Status.WAITFORRESPONSE.value,
                Arrays.asList(new NextStatus(Status.PENDING.value,User.USER.value,Ticket.BOTH.value)));

        nextStatusTable.put(Status.POSTPONE.value,
                Arrays.asList(new NextStatus(Status.INPROGRESS.value,User.DEV.value,Ticket.BOTH.value)));
    }

    public static List<NextStatus> getNextPossibleStatuses(long currStatus){
        return nextStatusTable.get(currStatus);
    }
}
