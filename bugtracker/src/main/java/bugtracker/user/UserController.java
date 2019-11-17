package bugtracker.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Inject
    UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity<String> addNewUser(@RequestBody UserEntity user) {
        try{
            userService.createUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){   // TODO: error handling (already existing name, invalid email, etc.)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public UserEntity deleteUser(@RequestBody UserEntity user) {
        return this.userService.deleteUser(user);
    }


    @RequestMapping(value = "/modify", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public UserEntity modifyUser(@RequestBody UserEntity user) {
        return this.userService.modifyUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<UserEntity> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/getAllApprover")
    public ResponseEntity<List<UserEntity>> getAllApprover(){
        return new ResponseEntity<>(userService.getUsersByType(3L), HttpStatus.OK);
    }

    @GetMapping("/getAllDeveloper")
    public ResponseEntity<List<UserEntity>> getAllDeveloper(){
        return new ResponseEntity<>(userService.getUsersByType(1L), HttpStatus.OK);
    }
}
