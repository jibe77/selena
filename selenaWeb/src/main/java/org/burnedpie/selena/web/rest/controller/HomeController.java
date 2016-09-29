package org.burnedpie.selena.web.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jibe on 14/09/16.
 */
@Controller
public class HomeController {

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home(Model model){
        return "redirect:/player";

    }

    @RequestMapping(value="/player", method=RequestMethod.GET)
    public String player(Model model){
        model.addAttribute("currentPage", "player");
        return "views/player";
    }

    @RequestMapping(value="/recorder", method=RequestMethod.GET)
    public String recorder(Model model){
        model.addAttribute("currentPage", "recorder");
        return "views/recorder";
    }

    @RequestMapping(value="/users", method=RequestMethod.GET)
    public String users(Model model){
        return "views/users";
    }

    @RequestMapping(value="/roles", method= RequestMethod.GET)
    public String roles(Model model){
        return "views/roles";
    }

}