package bugtracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Inject
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserEntity createUser(UserEntity user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserEntity deleteUser(UserEntity user){
        user.setDeletedTs(LocalDateTime.now());
        return userRepository.save(user);
    }

    public UserEntity undeleteUser(UserEntity user){
        user.setDeletedTs(null);
        return userRepository.save(user);
    }

    public UserEntity modifyUser(UserEntity user){
        UserEntity userEntity = userRepository.findUserEntityById(user.getId());
        if (!StringUtils.isEmpty(user.getPassword())) {
            userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userEntity.setEmail(user.getEmail());
        userEntity.setDeletedTs(user.getDeletedTs());
        userEntity.setName(user.getName());
        userEntity.setType(user.getType());
        return userRepository.save(userEntity);
    }

    public List<UserEntity> getAllUser(){
        return userRepository.findAll().stream().map(u -> {u.setPassword(null); return u;}).collect(Collectors.toList());
    }

    public UserEntity getUserById(Long id){
        UserEntity user = userRepository.findUserEntityById(id);
        user.setPassword(null);
        return user;
    }

    public UserEntity getUserByName(String name) {
        UserEntity user = userRepository.findByName(name);
        user.setPassword(null);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username){
        UserEntity user = userRepository.findByName(username);
        if (user == null) throw new UsernameNotFoundException(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new BTUserDetails(user);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public List<UserEntity> getUsersByType(Integer type){
        return userRepository.findByType(type);
    }
    public List<UserEntity> searchByName(String text) { return userRepository.findByNameContaining(text); }
}
