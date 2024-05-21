package org.issk;

import org.issk.dao.UserDaoDBImpl;
import org.issk.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(App.class, args);
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}