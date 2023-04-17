package org.lessons.java.fotoalbum.security;

import org.lessons.java.fotoalbum.model.Role;
import org.lessons.java.fotoalbum.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DatabaseUserDetails implements UserDetails {

    private String username;
    private String password;

    private Set<GrantedAuthority> authorities;

    public DatabaseUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = new HashSet<>();
        for (Role r : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
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
