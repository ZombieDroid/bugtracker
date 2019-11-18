package bugtracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import bugtracker.user.UserEntity;
import bugtracker.user.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userDao;
	
	private List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>(5);
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		UserEntity user = userDao.findUserEntityByName(name);

		//return Optional.ofNullable(optionalUser).orElseThrow(()->new UsernameNotFoundException("Username Not Found")).map(UserDetailsImpl::new).get();
		
		//assign role from (user)type field
		auths.add(new SimpleGrantedAuthority("ROLE_" + user.getType().toString()));
		
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), auths);
	}

}
