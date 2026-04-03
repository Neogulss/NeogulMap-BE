package com.neogulss.neogulmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NeogulMapApplication {

  public static void main(String[] args) {
    SpringApplication.run(NeogulMapApplication.class, args);
  }

}
