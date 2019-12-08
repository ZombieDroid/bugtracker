package bugtracker.user;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
    @Secured("ROLE_ADMIN")
    public List<UserEntity> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/getAllApprover")
    public ResponseEntity<List<UserEntity>> getAllApprover(){
        return new ResponseEntity<>(userService.getUsersByType(3), HttpStatus.OK);
    }

    @PostMapping("/dispatch")
    public ResponseEntity<Void> dispatchUser() {
        SecurityContext cc = SecurityContextHolder.getContext();
        HttpHeaders headers = new HttpHeaders();
        if (cc.getAuthentication() != null) {
            Authentication auth = cc.getAuthentication();
            try {
                headers.setLocation(new URI("/home"));
            } catch (URISyntaxException ignored) {}
        }

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/getAllDeveloper")
    public ResponseEntity<List<UserEntity>> getAllDeveloper(){
        return new ResponseEntity<>(userService.getUsersByType(1), HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserEntity>> getAllSimpleUser(){
        return new ResponseEntity<>(userService.getUsersByType(2), HttpStatus.OK);
    }
}
