package no.ntnu.gr12.krrr_project.controllers;

import no.ntnu.gr12.krrr_project.dto.UserProfileDto;
import no.ntnu.gr12.krrr_project.services.AccessUserService;
import no.ntnu.gr12.krrr_project.services.UserService;
import no.ntnu.gr12.krrr_project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessUserService accessUserService;

    //TODO must be fixed, gives an error when accessed.
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() {

        List<User> userList = new ArrayList<>();
        Iterator<User> it = userService.readUsers().iterator();

        while(it.hasNext()){
            userList.add(it.next());
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @GetMapping("/api/users/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username) throws InterruptedException {
        User sessionUser = accessUserService.getSessionUser();
        if (sessionUser != null && sessionUser.getUsername().equals(username)) {
            UserProfileDto profile = new UserProfileDto(sessionUser.getEmail());
            Thread.sleep(2000); // Simulate sleep
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } else if (sessionUser == null) {
            return new ResponseEntity<>("Profile data accessible only to authenticated users", HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>("Profile data for other users not accessible!", HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping("/api/users/{username}")
    public ResponseEntity<String> updateProfile(@PathVariable String username, @RequestBody UserProfileDto profileData) throws InterruptedException {
        User sessionUser = accessUserService.getSessionUser();
        ResponseEntity<String> response;
        if (sessionUser != null && sessionUser.getUsername().equals(username)) {
            if (profileData != null) {
                if (accessUserService.updateProfile(sessionUser)) {
                    Thread.sleep(2000); // Simulate long operation
                    response = new ResponseEntity<>("", HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>("Could not update Profile data", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response = new ResponseEntity<>("Profile data not supplied", HttpStatus.BAD_REQUEST);
            }
        } else if (sessionUser == null) {
            response = new ResponseEntity<>("Profile data accessible only to authenticated users", HttpStatus.UNAUTHORIZED);
        } else {
            response = new ResponseEntity<>("Profile data for other users not accessible!", HttpStatus.FORBIDDEN);
        }
        return response;
    }

    @GetMapping("/users/{username}/cartID")
    public ResponseEntity<String> getCartItems(@PathVariable String username) {
        User sessionUser = accessUserService.getSessionUser();
        if (sessionUser != null && sessionUser.getUsername().equals(username)) {
            return new ResponseEntity<String>(sessionUser.getCartID(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<String>("Error: cart not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Not needed
    @PostMapping(value = "/users")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }
    @PutMapping(value = "/users/{id}")
    public void updateUser(@RequestBody User user) {
        userService.updateUsers(user);
    }
    @DeleteMapping(value = "/users/{id}")
    public void deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
    }

     */
}