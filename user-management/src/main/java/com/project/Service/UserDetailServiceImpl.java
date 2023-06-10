package com.project.Service;

import com.project.Model.User;
import com.project.Repository.AddressRepository;
import com.project.Repository.PaymentcardRepository;
import com.project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentcardRepository paymentcardRepository;
    @Autowired
    AddressRepository addressRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomUserDetail.build(user);
    }

    public UserDetails loadUserByIdEmail(String idemail)throws UsernameNotFoundException{
        User user = userRepository.findById(idemail).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found with Id_email: " + idemail));
        return CustomUserDetail.build(user);

    }

}
