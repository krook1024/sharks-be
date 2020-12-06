package krook1024.sharksAPI.user.service;

import antlr.BaseAST;
import krook1024.sharksAPI.exception.CustomException;
import krook1024.sharksAPI.user.model.User;
import krook1024.sharksAPI.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bcryptPasswordEncoder;

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public User getOne(int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new CustomException("User does not exist", HttpStatus.NOT_FOUND);
        }

        return user.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public User saveOne(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomException("User already exists", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
