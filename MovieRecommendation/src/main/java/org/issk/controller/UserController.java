package org.issk.controller;


import org.issk.dto.User;
import org.issk.service.UserService;
import org.issk.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/create")
    public void createUser(){
        User user = new User();
        userService.createUser(user);

    }
}
