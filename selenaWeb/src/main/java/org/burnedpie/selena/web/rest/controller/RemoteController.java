package org.burnedpie.selena.web.rest.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class RemoteController {

    // global constants
    public static final String SUCCESS              =   "SUCCESS";
    public static final String FAIL                 =   "FAIL";

    // player stopped constants and method
    public static final String PLAYER_STOPPED       =   "Player is stopped.";
    public static final String REST_STOP            =   "/stop";

    @RequestMapping(REST_STOP)
    RestReturnValue stop() {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus(SUCCESS);
        restReturnValue.setMessage(PLAYER_STOPPED);
        return restReturnValue;
    }

    // volume constants and method
    public static final String VOLUME_TURNED_UP     =   "Volume is turned up.";
    public static final String VOLUME_TURNED_DOWN   =   "Volume is turned down.";
    public static final String REST_VOLUME_UP       =   "/volumeUp";
    public static final String REST_VOLUME_DOWN     =   "/volumeDown";

    @RequestMapping(REST_VOLUME_UP)
    RestReturnValue volumUp() {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus(SUCCESS);
        restReturnValue.setMessage(VOLUME_TURNED_UP);
        return restReturnValue;
    }

    @RequestMapping(REST_VOLUME_DOWN)
    RestReturnValue volumeDown() {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus(SUCCESS);
        restReturnValue.setMessage(VOLUME_TURNED_DOWN);
        return restReturnValue;
    }

    // radio constants and methods
    public static final String RADIO_STATION_SET         =   "Radio station set to {0}.";
    public static final String REST_PLAY_RADIO_STATION =   "/playRadioStation";

    @RequestMapping(REST_PLAY_RADIO_STATION)
    RestReturnValue playRadioStation(@RequestParam(value="radioStation", required = true) Integer radioStation) {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus(SUCCESS);
        restReturnValue.setMessage(RADIO_STATION_SET.replace("{0}", String.valueOf(radioStation)));
        return restReturnValue;
    }

    // airplay constants and methods
    public static final String AIRPLAY_STARTED      =   "Airplay is started.";
    public static final String REST_START_AIRPLAY   =   "/startAirplay";

    @RequestMapping(REST_START_AIRPLAY)
    RestReturnValue startAirplay() {
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus(SUCCESS);
        restReturnValue.setMessage(AIRPLAY_STARTED);
        return restReturnValue;
    }







    /**
     * Starts web server, for test purpose.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RemoteController.class, args);
    }

}