package bugtracker.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BTUserDetails implements UserDetails {

    private UserEntity user;
    private List<GrantedAuthority> auths = new ArrayList<>(1);

    public BTUserDetails(UserEntity user){
        this.user = user;

        long tmpType = user.getType();  // TODO: change UserEntity::type's type to enum/short/int/etc.
        switch((short)tmpType){
            case 0:
                auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            case 1:
                auths.add(new SimpleGrantedAuthority("ROLE_DEVELOPER"));
                break;
            case 2:
                auths.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
            case 3:
                auths.add(new SimpleGrantedAuthority("ROLE_APPROVER"));
                break;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auths;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getDeletedTs() == null;
    }

    public Long getId() {
        return user.getId();
    }
}
