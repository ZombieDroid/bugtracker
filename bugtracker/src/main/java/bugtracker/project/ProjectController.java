package bugtracker.project;

import bugtracker.timelog.TimeLogEntity;
import bugtracker.timelog.TimeLogService;
import bugtracker.ticket.TicketEntity;
import bugtracker.ticket.TicketService;
import bugtracker.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    public static final String UPDATE = "update";
    public static final String CREATE = "create";

    // todo: move related logic to ProjectService!

    @Inject
    ProjectService projectService;

    @Inject
    TimeLogService timeLogService;

    @Inject
    TicketService ticketService;

    @Inject
    UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity addNewProject(@RequestBody ProjectEntity project) {
        try {
            projectService.saveProjectWithHistory(project, CREATE);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProject(@RequestBody ProjectEntity project) {
        try{
            projectService.saveProjectWithHistory(project, UPDATE);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProject(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("project");
        try{
            return new ResponseEntity<>(projectService.getProjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ModelAndView getAllProjects() {
        ModelAndView mv = new ModelAndView("projects");
        try{
            String projectJson = (new ObjectMapper()).writeValueAsString(projectService.getAllProject());
            mv.addObject(projectJson);
        } catch (JsonProcessingException e) {
            mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return mv;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProjectEntity>> getAll(){
        return new ResponseEntity<>(projectService.getAllProject(), HttpStatus.OK);
    }

    @GetMapping("/searchProjects/")
    public ResponseEntity<List<ProjectEntity>> searchAllProjects(){
        return getAll();
    }

    @GetMapping("/searchProjects/{searchText}")
    public ResponseEntity<List<ProjectEntity>> searchProjects(@PathVariable String searchText){
        List<ProjectEntity> projects = projectService.searchByName(searchText);
        List<ProjectEntity> tmpProjs = projectService.searchByDescription(searchText);

        for(ProjectEntity proj : tmpProjs){
            if(!projects.contains(proj)){
                projects.add(proj);
            }
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @AllArgsConstructor
    static class TimeLogStatistics{
        private String month;
        private Long time;

        public TimeLogStatistics(LocalDate date, Long time){
            this.time = time;
            this.month = date.getYear() + "-" + date.getMonth();
        }

        public String getMonth(){
            return month;
        }

        public Long getTime(){
            return time;
        }
    }

    @GetMapping("/timelogstats/{projectId}")
    public ResponseEntity<List<TimeLogStatistics>> getTimeLogStats(@PathVariable Long projectId){

        List<TicketEntity> projectTickets = ticketService.searchByProject(projectId);

        List<TimeLogEntity> timelogs = timeLogService.getAllTimeLog();
        List<TimeLogEntity> tl = timelogs.
                stream().filter(timeLogEntity -> projectTickets.stream().
                anyMatch(ticketEntity -> timeLogEntity.getTicketId().equals(ticketEntity.getId()))).collect(Collectors.toList());

        Map<LocalDate, Long> stats = tl.stream().collect(Collectors.
                groupingBy(item -> item.getDate().with(TemporalAdjusters.firstDayOfMonth()), Collectors.summingLong(TimeLogEntity::getTime)));

        List<TimeLogStatistics> statsList =  stats.entrySet().stream().
                map(localDateLongEntry -> new TimeLogStatistics(localDateLongEntry.getKey(), localDateLongEntry.getValue())).collect(Collectors.toList());

        return new ResponseEntity<>(statsList, HttpStatus.OK);
    }

    @AllArgsConstructor
    static class UserTimeLogStatistics{
        private String month;
        private Long time;
        private String user;

        public UserTimeLogStatistics(LocalDate date, Long time, String user){
            this.time = time;
            this.month = date.getYear() + "-" + date.getMonth();
            this.user = user;
        }

        public String getMonth(){
            return month;
        }

        public Long getTime(){
            return time;
        }

        public String getUser(){
            return user;
        }
    }

    @GetMapping("/usertimelogstats/{projectId}")
    public ResponseEntity<List<UserTimeLogStatistics>> getUserTimeLogStats(@PathVariable Long projectId){

        List<TicketEntity> projectTickets = ticketService.searchByProject(projectId);

        List<TimeLogEntity> timelogs = timeLogService.getAllTimeLog();
        List<TimeLogEntity> tl = timelogs.
                stream().filter(timeLogEntity -> projectTickets.stream().
                anyMatch(ticketEntity -> timeLogEntity.getTicketId().equals(ticketEntity.getId()))).collect(Collectors.toList());

        Map<LocalDate, List<TimeLogEntity>> stats = tl.stream().collect(Collectors.
                groupingBy(item -> item.getDate().with(TemporalAdjusters.firstDayOfMonth())));

        List<UserTimeLogStatistics> statsList = new ArrayList<>();

        for(Map.Entry<LocalDate, List<TimeLogEntity>> dateMapEntry : stats.entrySet()){
            Map<Long, Long> userTime = dateMapEntry.getValue().stream().
                    collect(Collectors.groupingBy(TimeLogEntity::getUserId, Collectors.summingLong(TimeLogEntity::getTime)));

            for(Map.Entry<Long,Long> userTimeEntry : userTime.entrySet()){
                statsList.add(new UserTimeLogStatistics(dateMapEntry.getKey(), userTimeEntry.getValue(), userService.getUserById(userTimeEntry.getKey()).getName()));
            }
        }

        return new ResponseEntity<>(statsList, HttpStatus.OK);
    }

    @AllArgsConstructor
    static class PriorityStatistics{
        private String month;
        private Double s1Percent;
        private Double s2Percent;
        private Double s3Percent;

        public PriorityStatistics(LocalDate date, Double s1, Double s2, Double s3){
            this.month = date.getYear() + "-" + date.getMonth();
            this.s1Percent = s1;
            this.s2Percent = s2;
            this.s3Percent = s3;
        }

        public String getMonth(){
            return month;
        }

        public Double getS1Percent() {
            return s1Percent;
        }

        public Double getS2Percent() {
            return s2Percent;
        }

        public Double getS3Percent() {
            return s3Percent;
        }
    }

    @GetMapping("/prioritystats/{projectId}")
    public ResponseEntity<List<PriorityStatistics>> getPriorityStats(@PathVariable Long projectId) throws Exception {
        List<TicketEntity> tickets = ticketService.searchByProject(projectId);
        List<TimeLogEntity> timeLogs = new LinkedList<>();
        for(TicketEntity te : tickets){
            timeLogs.addAll(timeLogService.getTimeLogsByTicketId(te.getId()));
        }

        ProjectEntity project = projectService.getProjectById(projectId);

        Optional<TimeLogEntity> minDateTimeLog = timeLogs.stream().min(Comparator.comparing(TimeLogEntity::getDate));
        List<PriorityStatistics> priorityStatisticsList = new LinkedList<>();
        if(!minDateTimeLog.isPresent()){
            return new ResponseEntity<>(priorityStatisticsList, HttpStatus.OK);
        }

        for (LocalDate ld = minDateTimeLog.get().getDate(); !ld.isAfter(LocalDate.now()); ld = ld.plusMonths(1)) {
            final LocalDate locDate = ld;
            List<TicketEntity> createdTickets = tickets.stream().filter(ticketEntity -> !ticketEntity.getCreationTime().isAfter(locDate)).collect(Collectors.toList());
            List<TimeLogEntity> loggedTimes = timeLogs.stream().filter(timeLogEntity -> !timeLogEntity.getDate().isAfter(locDate)).collect(Collectors.toList());
            Long allS1Time = 0L;
            long loggedS1Time = 0L;
            Long allS2Time = 0L;
            long loggedS2Time = 0L;
            Long allS3Time = 0L;
            long loggedS3Time = 0L;
            for (TicketEntity te : createdTickets) {
                List<TimeLogEntity> loggedTimesOnTicket =
                        loggedTimes.stream().filter(timeLogEntity -> timeLogEntity.getTicketId().equals(te.getId())).collect(Collectors.toList());
                long loggedTime = loggedTimesOnTicket.stream().mapToLong(TimeLogEntity::getTime).sum();
                if (te.getPriority() == 0) {
                    allS1Time += project.getS1Time();
                    loggedS1Time += loggedTime;
                } else if (te.getPriority() == 1) {
                    allS2Time += project.getS2Time();
                    loggedS2Time += loggedTime;
                } else if (te.getPriority() == 2) {
                    allS3Time += project.getS3Time();
                    loggedS3Time += loggedTime;
                } else {
                    throw new Exception("Unknown priority type: " + te.getPriority());
                }
            }
            Double s1Perc = BigDecimal.valueOf(((double) loggedS1Time / allS1Time) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
            Double s2Perc = BigDecimal.valueOf(((double) loggedS2Time / allS2Time) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
            Double s3Perc = BigDecimal.valueOf(((double) loggedS3Time / allS3Time) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
            priorityStatisticsList.add(new PriorityStatistics(locDate, s1Perc, s2Perc, s3Perc));
        }
        return new ResponseEntity<>(priorityStatisticsList, HttpStatus.OK);
    }
}
