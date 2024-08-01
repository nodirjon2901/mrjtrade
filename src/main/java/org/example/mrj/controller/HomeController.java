package org.example.mrj.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
    @GetMapping("/")
    public ResponseEntity<String> homeController()
    {
        return ResponseEntity.ok("Hello Mello , mrj-api");
    }
}
