package bugtracker.user;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Inject
    UserRepository userRepository;

    public UserEntity createUser(UserEntity user){
        return userRepository.save(user);
    }

    public UserEntity deleteUser(UserEntity user){
        user.setDeletedTs(LocalDateTime.now());
        return userRepository.save(user);
    }

    public UserEntity modifyUser(UserEntity user){
        return userRepository.save(user);
    }

    public List<UserEntity> getAllUser(){
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id){
        return userRepository.findUserEntityById(id);
    }
    
    public UserEntity findUserByUsername(String name) {
		return userRepository.findUserEntityByName(name);
	}

}
