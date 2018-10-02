package com.businessanddecisions.controllers;


import com.businessanddecisions.models.UserModel;
import com.businessanddecisions.personalizedhttpresponses.CustomHttpResponse;
import com.businessanddecisions.personalizedhttpresponses.CustomResponseType;
import com.businessanddecisions.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;


@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/adduser")
    public ResponseEntity<?> saveUser(@RequestBody UserModel user) {
        boolean res = this.userService.addUser(user);
        if(res)
            return new ResponseEntity<>(new CustomHttpResponse("vous etes bien inscrit", CustomResponseType.SUCCESS),HttpStatus.OK);

        return new ResponseEntity<>(new CustomHttpResponse("email deja utilisé",CustomResponseType.ERROR),HttpStatus.CONFLICT);
    }

    @GetMapping("/users/bosses")
    public List<UserModel> getAllBosses() {
        return this.userService.getAllBosses();
    }

    @GetMapping("/users/all")
    public List<UserModel> getAllUsers() {
       return this.userService.getAllUsers();
    }


    @GetMapping("/users/get/{id}")
    public UserModel getUserById(@PathVariable("id") String id) throws Exception{
        Long userId = Long.parseLong(id);
        return this.userService.findUserById(userId);
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserModel user,@PathVariable long id) {
        boolean res = this.userService.updateUser(id,user);
        if(res) {
            return new ResponseEntity<>("user updated",HttpStatus.OK);
        }
        return new ResponseEntity<>("error when updating user",HttpStatus.FAILED_DEPENDENCY);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        boolean res =this.userService.deleteUser(id);
        if(res) {
            return new ResponseEntity<>("user deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("error when deleting user",HttpStatus.FORBIDDEN);
    }

    @GetMapping("/users/next/{id}")
    public List<UserModel> paginateNext(@PathVariable("id") long id) {
        return this.userService.paginateNext(id);
    }

    @GetMapping("/users/previous/{id}")
    public List<UserModel> paginatePrevious(@PathVariable("id") long id) {
        return this.userService.paginatePrevious(id);
    }

    @PostMapping("/users/manageremployees")
    public List<UserModel> managerEmployees(@RequestBody String email) {
        return this.userService.getManagerEmployees(email);
    }

    @GetMapping("/testing/oussama")
    public ResponseEntity<?> test() {
        return new ResponseEntity<String>("test done",HttpStatus.OK);
    }
}
