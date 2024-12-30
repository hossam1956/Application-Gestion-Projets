package com.Projet_E2425G3_1.GestionProjets.services.user;

import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserRepository;
import com.Projet_E2425G3_1.GestionProjets.dto.PageResponse;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.exception.EmailAlreadyUsedException;
import com.Projet_E2425G3_1.GestionProjets.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;

    public UserServiceImpl(UserRepository userRepository, ProjectRepository projectRepository,UserProjectRepository userProjectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.userProjectRepository=userProjectRepository;
    }

    @Override
    public User createUser(String username, String email, String password, String profileImageUrl) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException("Email already in use");
        }
        User user = User.builder()
                .username(username.toLowerCase())
                .email(email.toLowerCase())
                .profileImageUrl(profileImageUrl)
                .password(passwordEncoder.encode(password))
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, String username, String email, String password, String profileImageUrl) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user ID : " + id + " not found"));
        if (username != null && !username.isEmpty()) {
            user.setUsername(username);
        }
        if (email != null && !email.isEmpty() && !userRepository.existsByEmail(email)) {
            user.setEmail(email);
        } else if (email != null) {
            throw new EmailAlreadyUsedException("Email already in use");
        }
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            user.setProfileImageUrl(profileImageUrl);
        }

        return userRepository.save(user);
    }


    @Override
    public PageResponse<User> findAllUsers(int size, int pageNum, String search) {
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("username").ascending());
        Page<User> userPages;
        if (search.isEmpty()) {
            userPages = userRepository.findAll(pageable);
        } else {
            userPages = userRepository.findAllByUsernameContainingIgnoreCase(search, pageable);
        }
        PageResponse<User> pageResponse = PageResponse.<User>builder()
                .content(userPages.getContent())
                .number(userPages.getNumber())
                .size(userPages.getSize())
                .totalElements(userPages.getTotalElements())
                .totalPages(userPages.getTotalPages())
                .first(userPages.isFirst())
                .last(userPages.isLast())
                .build();
        return pageResponse;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id) : Optional.empty();
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        List<Project> projects = projectRepository.findAllByManagerId(id);
        if (!projects.isEmpty()) {
            for (Project p : projects) {
                userProjectRepository.deleteByProjectId(p.getId());
                projectRepository.delete(p);
            }
        }
        userRepository.deleteById(id);
    }

}
