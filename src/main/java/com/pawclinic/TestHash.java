package com.pawclinic;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestHash {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "admin123";

        String hash = encoder.encode(password);

        System.out.println(hash);
    }
}