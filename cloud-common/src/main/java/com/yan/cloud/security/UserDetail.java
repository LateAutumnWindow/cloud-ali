//package com.yan.cloud.security;
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserDetail implements UserDetailsService {
//
//    @Override
//    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
//        List<SimpleGrantedAuthority> sga = new ArrayList<>();
//        sga.add(new SimpleGrantedAuthority("p1"));
//        User user = new User("admin", new BCryptPasswordEncoder().encode("123"), sga);
//        return user;
//
//    }
//}
