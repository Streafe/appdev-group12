package no.ntnu.gr12.krrr_project.services;

import no.ntnu.gr12.krrr_project.dto.UserDto;
import no.ntnu.gr12.krrr_project.dto.UserProfileDto;
import no.ntnu.gr12.krrr_project.models.Role;
import no.ntnu.gr12.krrr_project.models.User;
import no.ntnu.gr12.krrr_project.repositories.RoleRepository;
import no.ntnu.gr12.krrr_project.repositories.UserRepository;
import no.ntnu.gr12.krrr_project.security.AccessUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Optional;

@Service
public class AccessUserService implements UserDetailsService {
    private static final int MIN_PASSWORD_LENGTH = 6;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new AccessUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("User " + username + "not found");
        }
    }

    public UserDto loadUserById(Long id) throws UsernameNotFoundException {
        User user = null;
        for (User cachedUser : userService.readUsers()) {
            if (cachedUser.getId().equals(id)) {
                user = cachedUser;
            }
        }
        if (user != null) {
            return new UserDto(user.getUsername(),user.getId().toString());
        } else {
            throw new UsernameNotFoundException("User with id " + id + "not found");
        }
    }

    /**
     * Get the user which is authenticated for the current session
     *
     * @return User object or null if no user has logged in
     */
    public User getSessionUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Check if user with given username exists in the database
     *
     * @param username Username of the user to check, case-sensitive
     * @return True if user exists, false otherwise
     */
    private boolean userExists(String username) {
        try {
            loadUserByUsername(username);
            return true;
        } catch (UsernameNotFoundException ex) {
            return false;
        }
    }

    /**
     * Try to create a new user
     *
     * @param username Username of the new user
     * @param password Plaintext password of the new user
     * @return null when user created, error message on error
     */
    public String tryCreateNewUser(String username, String password) {
        String errorMessage;
        if ("".equals(username)) {
            errorMessage = "Username can't be empty";
        } else if (userExists(username)) {
            errorMessage = "Username already taken";
        } else {
            errorMessage = checkPasswordRequirements(password);
            if (errorMessage == null) {
                createUser(username, password);
            }
        }
        return errorMessage;
    }

    /**
     * Check if password matches the requirements
     *
     * @param password A password to check
     * @return null if all OK, error message on error
     */
    private String checkPasswordRequirements(String password) {
        String errorMessage = null;
        if (password == null || password.length() == 0) {
            errorMessage = "Password can't be empty";
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            errorMessage = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
        }
        return errorMessage;
    }


    /**
     * Create a new user in the database
     *
     * @param username Username of the new user
     * @param password Plaintext password of the new user
     */
    private void createUser(String username, String password) {
        Role userRole = roleRepository.findOneByName("ROLE_USER");
        if (userRole != null) {
            User user = new User(username, createHash(password));
            user.addRole(userRole);
            userRepository.save(user);
        }
    }

    /**
     * Create a secure hash of a password
     *
     * @param password Plaintext password
     * @return BCrypt hash, with random salt
     */
    private String createHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Update profile information for a user
     * @param user User to update
     * @return True on success, false otherwise
     */
    @Transactional
    public boolean updateProfile(User user, UserProfileDto profileData) {
        user.setBio(profileData.getBio());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean updateBio(String username, String newBio) {
        if (userRepository.findByUsername(username).isPresent()) {
            try {
                User userToUpdate = userRepository.findByUsername(username).get();
                userToUpdate.setBio(newBio);
                return true;
            } catch (Exception e) {
                throw e;
            }
        } else {
            return false;
        }
    }
}
