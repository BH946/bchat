package org.example;


import org.example.service.UserService;

public class Main {

  public static void main(String[] args) {
    System.out.println("Oracle test: 사용자 등록");
    UserService userService = new UserService();
    userService.register();
    System.out.println();
  }
}