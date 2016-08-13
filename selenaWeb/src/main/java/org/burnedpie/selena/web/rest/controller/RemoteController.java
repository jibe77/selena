package org.burnedpie.selena.web.rest.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class RemoteController {

    @RequestMapping("/playRadioStation")
    RestReturnValue playRadioStation(@RequestParam(value="radioStation", required = true) Integer radioStation) {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus("OK");
        restReturnValue.setMessage("Radio station set to 1.");
        return restReturnValue;
    }

    public static void main(String[] args) {
        SpringApplication.run(RemoteController.class, args);
    }

}