package com.businessanddecisions.services;

import com.businessanddecisions.models.UserModel;
import com.businessanddecisions.repositories.UserRepository;
import org.businessanddecisions.common.CustomPasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserModel> getAllUsers()  {
        return this.userRepository.findAll();
        //return this.userRepository.getAllUsersForAdmin();
    }

    public UserModel findUserById(Long id) {
         return this.userRepository.findById(id).get();
    }

    public boolean updateUser(UserModel user) {
        /*UserModel tmp = this.userRepository.findByEmail(user.getEmail());
        if(tmp !=null) {
             tmp.setFirstName(user.getFirstName());
             tmp.setLastName(user.getLastName());
             tmp.setEmail(user.getEmail());
             if(!user.getPassword().equals("anything")) {
                 tmp.setPassword();
             }
        }*/
        return true;
    }

    public boolean addUser(UserModel user) {
        try{
            CustomPasswordEncoder customPasswordEncoder= CustomPasswordEncoder.hash(user.getPassword());
            user.setPassword(customPasswordEncoder.getHashedPassword());
            this.userRepository.save(user);

            return true;
        }catch(Exception e) {
            return false;
        }
    }


    public UserModel getUser(Long id) {
        return this.userRepository.findById(id).get();
    }

    public boolean updateUser(Long id, UserModel user) {
        UserModel userToUpdate = this.userRepository.findById(id).get();
        if(userToUpdate != null) {
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setRole(user.getRole());
            if(!user.getPassword().equals("anything")) {
                String password = user.getPassword();
                password = this.encodePassword(password);
                userToUpdate.setPassword(password);
            }
            if(user.getHisBoss() != null) {
                userToUpdate.setHisBoss(user.getHisBoss());
            }
            UserModel res = this.userRepository.save(userToUpdate);
            return res != null ? true: false;
        }
        return false;
    }

    public boolean deleteUser(Long id) {
        try{
           this.userRepository.deleteById(id);
           return true;
        }catch(Exception e) {
            return false;
        }
    }

    public List<UserModel> getAllBosses() {
        return this.userRepository.findByRoleNot("employee");

    }


    public UserModel findOne(String email) {
           return this.userRepository.findByEmail(email);
    }

    public boolean deleteUser(long id) {
        try {
            this.userRepository.deleteById(id);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public  List<UserModel> paginateNext(long id) {
        return this.userRepository.getNextTenUsers(id);
    }

    public List<UserModel> paginatePrevious(long id){
        return this.userRepository.getPreviousTenUsers(id);
    }

    public List<UserModel> getManagerEmployees(String email) {
        return this.userRepository.getManagerEmployees(email);
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.encode(password);
    }






}
