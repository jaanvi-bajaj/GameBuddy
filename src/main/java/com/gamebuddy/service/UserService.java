package com.gamebuddy.service;

import com.gamebuddy.model.User;
import com.gamebuddy.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StorageService storageService;

    public UserService(UserRepository userRepository, StorageService storageService){
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public User createUser(User u, org.springframework.web.multipart.MultipartFile idFile){
        // hash password
        String salt = BCrypt.gensalt();
        String hashed = BCrypt.hashpw(u.getPasswordHash(), salt);
        u.setPasswordHash(hashed);

        // store file
        if (idFile != null && !idFile.isEmpty()){
            String fileName = storageService.store(idFile, "id");
            u.setIdCardFilename(fileName);
        }
        u.setActive(true);
        return userRepository.save(u);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id){ return userRepository.findById(id); }

    public boolean checkPassword(User user, String rawPassword){
        return BCrypt.checkpw(rawPassword, user.getPasswordHash());
    }
}