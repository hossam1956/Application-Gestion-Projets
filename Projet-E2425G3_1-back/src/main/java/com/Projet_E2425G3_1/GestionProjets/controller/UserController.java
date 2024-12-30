package com.Projet_E2425G3_1.GestionProjets.controller;

import com.Projet_E2425G3_1.GestionProjets.config.JwtUtil;
import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserRepository;
import com.Projet_E2425G3_1.GestionProjets.dto.PageResponse;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.entities.UserProject;
import com.Projet_E2425G3_1.GestionProjets.exception.EmailAlreadyUsedException;
import com.Projet_E2425G3_1.GestionProjets.exception.UserNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.services.s3.S3Service;
import com.Projet_E2425G3_1.GestionProjets.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final S3Service s3Service;
    private final UserService userService;
    private final UserProjectRepository userProjectRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserController(S3Service s3Service, UserService userService, UserProjectRepository userProjectRepository, ProjectRepository projectRepository,UserRepository userRepository,JwtUtil jwtUtil) {
        this.s3Service = s3Service;
        this.userService = userService;
        this.userProjectRepository = userProjectRepository;
        this.projectRepository = projectRepository;
        this.userRepository=userRepository;
        this.jwtUtil=jwtUtil;
    }

    @GetMapping
    public ResponseEntity<PageResponse<User>> getAllUser(@RequestParam("size") int size,
                                                         @RequestParam("pageNum") int pageNum,
                                                         @RequestParam(value = "search", defaultValue = "") String search) {

        return ResponseEntity.ok(userService.findAllUsers(size, pageNum, search));
    }
    @GetMapping("all")
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@RequestParam("id") Long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        Set<Project> projects = new HashSet<>(
                userProjectRepository.findAllByUserId(id).stream()
                        .map(UserProject::getProject)
                        .toList()
        );
        projects.addAll(projectRepository.findAllByManagerId(id));
        return ResponseEntity.status(200).body(Map.of(
                "user", user,
                "projects", projects
        ));

    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestParam("username") String username,
                                        @RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        @RequestParam("profileImage") MultipartFile profileImage) {
        if (!s3Service.isSupportedImageFormat(profileImage.getContentType())) {
            return ResponseEntity.status(400).body("Invalid file format. Only JPEG,JPG and PNG are supported.   "+profileImage.getContentType());
        }

        try {
            String imageUrl = s3Service.uploadFile(profileImage);
            User user = userService.createUser(username, email, password, imageUrl);
            return ResponseEntity.status(201).body(Map.of(
                    "message", "User created successfully",
                    "userId", user.getId()
            ));
        } catch (EmailAlreadyUsedException e) {
            return ResponseEntity.status(400).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }


    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@RequestParam(value = "id") Long id,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "email", required = false) String email,
                                        @RequestParam(value = "password", required = false) String password,
                                        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {


        if (profileImage != null && !s3Service.isSupportedImageFormat(profileImage.getContentType())) {
            throw new IllegalArgumentException("Invalid file format. Only JPEG and PNG are supported.");
        }

        try {
            String imageUrl = null;
            if (profileImage != null) {
                imageUrl = s3Service.uploadFile(profileImage);
            }

            User user = userService.updateUser(id, username, email, password, imageUrl);
            return ResponseEntity.status(200).body(Map.of(
                    "message", "User updated successfully",
                    "userId", user.getId()
            ));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (EmailAlreadyUsedException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }


    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        try {
            Optional<User> user = userService.findUserById(id);
            userService.deleteUserById(id);
            return ResponseEntity.status(200).body(Map.of(
                    "message", "User deleted successfully",
                    "userId", id
            ));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }

    }

}
