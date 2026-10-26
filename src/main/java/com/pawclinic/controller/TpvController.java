package com.pawclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tpv")
public class TpvController {

    @GetMapping
    public String tpv() {
        return "tpv/tpv";
    }

}
