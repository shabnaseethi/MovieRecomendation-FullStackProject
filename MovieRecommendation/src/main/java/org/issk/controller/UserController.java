package org.issk.controller;


import org.issk.dto.Session;
import org.issk.dto.User;
import org.issk.service.UserService;
import org.issk.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value="/create", consumes= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PostMapping(value="/login",  consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Session login(@RequestBody User user){
        return userService.login(user);
    }
}
