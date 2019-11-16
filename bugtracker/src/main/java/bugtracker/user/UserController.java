package bugtracker.user;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Inject
    UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public UserEntity addNewUser(@RequestBody UserEntity user) {
        return this.userService.createUser(user);
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
    public UserEntity getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/all")
    public List<UserEntity> getAllUser() {
        return userService.getAllUser();
    }
}
