package com.evtd.dictionary.service.impl;

import com.evtd.dictionary.entity.Privilege;
import com.evtd.dictionary.entity.Role;
import com.evtd.dictionary.entity.User;
import com.evtd.dictionary.repository.PrivilegeRepository;
import com.evtd.dictionary.repository.RoleRepository;
import com.evtd.dictionary.repository.UserRepository;
import com.evtd.dictionary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createTestingData() {
        Privilege privilege1 = privilegeRepository.save(Privilege.builder().name("read").build());
        Privilege privilege2 = privilegeRepository.save(Privilege.builder().name("write").build());
        Privilege privilege3 = privilegeRepository.save(Privilege.builder().name("edit").build());

        Role roleUser = roleRepository.save(Role.builder().name("USER")
                .privileges(Collections.singletonList(privilege1)).build());
        Role roleAdmin = roleRepository.save(Role.builder().name("ADMIN")
                .privileges(Collections.singletonList(privilege2)).build());

        userRepository.save(User.builder().username("user").email("user@cnj.vn").password(passwordEncoder.encode("123")).roles(Collections.singletonList(roleUser)).build());

        userRepository.save(User.builder().username("admin").email("admin@cnj.vn").password(passwordEncoder.encode("123")).roles(Collections.singletonList(roleAdmin)).build());
    }
}
