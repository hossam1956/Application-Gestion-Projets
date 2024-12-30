package com.Projet_E2425G3_1.GestionProjets.services.user;

import com.Projet_E2425G3_1.GestionProjets.dto.PageResponse;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.exception.EmailAlreadyUsedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(String username, String email, String password, String profileImageUrl) throws EmailAlreadyUsedException;
    User updateUser(Long id, String username, String email, String password, String profileImageUrl);
    PageResponse<User> findAllUsers(int size,int pageNum,String search);
    Optional<User> findUserById(Long id);
    void deleteUserById(Long id);
}
