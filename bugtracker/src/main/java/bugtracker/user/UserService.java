package bugtracker.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

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

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByName(userName);
        return new BTUserDetails(user);
    }

    public List<UserEntity> getUsersByType(Long type){
        return userRepository.findByType(type);
    }
}
