package no.ntnu.gr12.krrr_project.DBClasses.services;

import no.ntnu.gr12.krrr_project.DBClasses.models.User;
import no.ntnu.gr12.krrr_project.DBClasses.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Transactional
    public String addUser(User user) {
        try {
            if(!repository.existsById(user.getId().toString())) {
                repository.save(user);
                return "User created";
            } else {
                return "user is already in the database";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public Iterable<User> readUsers() {
        return repository.findAll();
    }

    @Transactional
    public String updateUsers(User user) {
        if (repository.existsById(user.getId().toString())) {
            try {
                User userToUpdate = repository.findById(user.getId().toString()).get();
                userToUpdate.setDescription(user.getDescription());
                repository.save(userToUpdate);
                return "user info updated";
            } catch (Exception e) {
                throw e;
            }
        }else {
            return "User does not exist in DB";
    }
    }

    @Transactional
    public String deleteUser(User user) {
        if (repository.existsById(user.getId().toString())) {
            try {
                repository.delete(user);
                return "user has been deleted";
            }catch (Exception e) {
                throw e;
            }
        }else {
            return "User deos not exist in DB";
        }
    }

}
