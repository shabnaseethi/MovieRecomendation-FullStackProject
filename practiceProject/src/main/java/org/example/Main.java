package org.example;


import org.example.service.PracticeService;

public class Main {
    public static void main(String[] args) {
        PracticeService service = new PracticeService();
        System.out.println("Original Branch!");

        System.out.println(service.sneeze());

        System.out.println(service.isaac());

    }
}