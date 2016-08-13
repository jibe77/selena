package org.burnedpie.selena.web.rest.controller;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.persistance.PersistanceService;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class RemoteController {

    // selena audio dependencies
    AirplayService airplayService;
    RadioService radioService;
    PersistanceService persistanceService;

    // global constants
    public static final String SUCCESS              =   "SUCCESS";
    public static final String FAIL                 =   "FAIL";

    // player stopped constants and method
    public static final String PLAYER_STOPPED       =   "Player is stopped.";
    public static final String REST_STOP            =   "/stop";

    @RequestMapping(REST_STOP)
    ReturnValue stop() {
        ReturnValue ReturnValue = new ReturnValue();
        ReturnValue.setStatus(SUCCESS);
        ReturnValue.setMessage(PLAYER_STOPPED);
        return ReturnValue;
    }

    // volume constants and method
    public static final String VOLUME_TURNED_UP     =   "Volume is turned up.";
    public static final String VOLUME_TURNED_DOWN   =   "Volume is turned down.";
    public static final String REST_VOLUME_UP       =   "/volumeUp";
    public static final String REST_VOLUME_DOWN     =   "/volumeDown";

    @RequestMapping(REST_VOLUME_UP)
    ReturnValue volumUp() {
        ReturnValue ReturnValue = new ReturnValue();
        ReturnValue.setStatus(SUCCESS);
        ReturnValue.setMessage(VOLUME_TURNED_UP);
        return ReturnValue;
    }

    @RequestMapping(REST_VOLUME_DOWN)
    ReturnValue volumeDown() {
        ReturnValue ReturnValue = new ReturnValue();
        ReturnValue.setStatus(SUCCESS);
        ReturnValue.setMessage(VOLUME_TURNED_DOWN);
        return ReturnValue;
    }

    // radio constants and methods
    public static final String RADIO_STATION_SET         =   "Radio station set to {0}.";
    public static final String REST_PLAY_RADIO_STATION =   "/playRadioStation";

    @RequestMapping(REST_PLAY_RADIO_STATION)
    ReturnValue playRadioStation(@RequestParam(value="radioStation", required = true) Integer radioStation) {
        if (airplayService.isAirplayOn()) {
            airplayService.turnAirplayOff();
        }
        if (radioService.isRadioOn()) {
            radioService.stopRadio();
        }
        String url = persistanceService.findRadioStationUrlByIndex(radioStation);
        try {
            radioService.playRadioChannel(url);
        } catch (RadioException e) {
            airplayService.turnAirplayOn(persistanceService.findAirplayServiceName());
        }

        ReturnValue ReturnValue = new ReturnValue();
        ReturnValue.setStatus(SUCCESS);
        ReturnValue.setMessage(RADIO_STATION_SET.replace("{0}", String.valueOf(radioStation)));
        return ReturnValue;
    }

    // airplay constants and methods
    public static final String AIRPLAY_STARTED      =   "Airplay is started.";
    public static final String REST_START_AIRPLAY   =   "/startAirplay";

    @RequestMapping(REST_START_AIRPLAY)
    ReturnValue startAirplay() {
        ReturnValue ReturnValue = new ReturnValue();
        ReturnValue.setStatus(SUCCESS);
        ReturnValue.setMessage(AIRPLAY_STARTED);
        return ReturnValue;
    }

    public AirplayService getAirplayService() {
        return airplayService;
    }

    public void setAirplayService(AirplayService airplayService) {
        this.airplayService = airplayService;
    }

    public RadioService getRadioService() {
        return radioService;
    }

    public void setRadioService(RadioService radioService) {
        this.radioService = radioService;
    }

    public PersistanceService getPersistanceService() {
        return persistanceService;
    }

    public void setPersistanceService(PersistanceService persistanceService) {
        this.persistanceService = persistanceService;
    }

    /**
     * Starts web server, for test purpose.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RemoteController.class, args);
    }

}