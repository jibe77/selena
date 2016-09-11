package org.burnedpie.selena.web.rest.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jibe on 08/09/16.
 */
public class SecurityUser extends User implements UserDetails {

    public SecurityUser() {
        super();
    }

    public SecurityUser(User user) {
        super(user.getUsername(), user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ADMIN";
            }
        };
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(1);
        grantedAuthorities.add(grantedAuthority);
        return grantedAuthorities;
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
        return true;
    }
}
