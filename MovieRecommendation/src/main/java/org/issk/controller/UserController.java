package org.issk.controller;


import org.issk.dto.Session;
import org.issk.dto.User;
import org.issk.service.UserService;
import org.issk.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<Session> login(@RequestBody User user){
        Session session = userService.login(user);
        return (session == null) ? new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED) : new ResponseEntity<>(session, HttpStatus.ACCEPTED);
    }

    @PostMapping(value="/logout",  consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> logout(HttpServletRequest request, @RequestBody User user){
        return userService.logout(request, user);
    }


    @PutMapping(value="/edit", consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> editPreferences(HttpServletRequest request, @RequestBody User user){
       return userService.editPreferences(request,user);

    }

    @DeleteMapping(value = "/remove", consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deletePreferences(HttpServletRequest request, @RequestBody User user) {
        return userService.removePreferences(request,user);
    }

    @DeleteMapping(value = "/deleteuser", consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteUser(HttpServletRequest request, @RequestBody User user) {
        return userService.deleteUser(request,user);
    }


    @PostMapping(value = "/add-movie", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addFavouriteMovie(HttpServletRequest request, @RequestBody User user){
        return userService.addFavouriteMovies(request,user);

    }


}
