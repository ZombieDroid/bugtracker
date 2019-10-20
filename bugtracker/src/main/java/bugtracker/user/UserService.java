package bugtracker.user;

import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.List;

@Service
public class UserService {

    @Inject
    UserRepository userRepository;

    public UserEntity createUser(UserEntity user){
        return userRepository.save(user);
    }

    public List<UserEntity> getAllUser(){
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id){
        return userRepository.findUserEntityById(id);
    }

}
