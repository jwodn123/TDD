package com.example.tdd.web.service;

import com.example.tdd.web.entity.User;
import com.example.tdd.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public Long save(User user) {
        return userRepository.save(user).getId();
    }

    @Transactional
    public Long update(Long id, String address) {
        User user = findById(id);
        user.update(address);
        return id;
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
