package bugtracker.timelog;

import bugtracker.user.UserService;
import bugtracker.user.UserEntity;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.sql.Date;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/timelog")
public class TimeLogController {

    @Inject
    TimeLogService timeLogService;

    @Inject
    UserService userService;

    @RequestMapping(value = "/log", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity logTime(@RequestBody TimeLogEntity timeLog){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        UserEntity user = userService.getUserByName(username);

        if(user != null){
            timeLog.setUserId(user.getId());
            timeLog.setDate(LocalDate.now());
            timeLogService.createTimeLog(timeLog);

            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
