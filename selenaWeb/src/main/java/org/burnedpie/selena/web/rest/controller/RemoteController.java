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
        restReturnValue.setMessage("Radio station set to "+radioStation+".");
        return restReturnValue;
    }

    @RequestMapping("/startAirplay")
    RestReturnValue startAirplay() {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus("OK");
        restReturnValue.setMessage("Airplay is started.");
        return restReturnValue;
    }

    @RequestMapping("/stop")
    RestReturnValue stop() {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus("OK");
        restReturnValue.setMessage("Player is stopped.");
        return restReturnValue;
    }

    @RequestMapping("/volumeUp")
    RestReturnValue volumUp() {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus("OK");
        restReturnValue.setMessage("Volume is turned up.");
        return restReturnValue;
    }

    @RequestMapping("/volumeDown")
    RestReturnValue volumeDown() {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus("OK");
        restReturnValue.setMessage("Volume is turned down.");
        return restReturnValue;
    }

    public static void main(String[] args) {
        SpringApplication.run(RemoteController.class, args);
    }

}