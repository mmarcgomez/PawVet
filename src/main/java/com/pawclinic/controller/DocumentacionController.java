package com.pawclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/documentacion")
public class DocumentacionController {

    @GetMapping
    public String documentacion() {
        return "documentacion/documentacion";
    }

}
