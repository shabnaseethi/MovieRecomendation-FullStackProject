package org.issk;

import org.issk.dao.UserDaoDBImpl;
import org.issk.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    @Autowired
    UserDaoDBImpl userDaoDB;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(App.class, args);
    }
}