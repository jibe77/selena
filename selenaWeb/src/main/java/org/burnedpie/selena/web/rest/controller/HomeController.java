package org.burnedpie.selena.web.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jibe on 14/09/16.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(){
        return "index";

    }
}