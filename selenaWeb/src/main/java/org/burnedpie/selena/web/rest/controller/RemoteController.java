package org.burnedpie.selena.web.rest.controller;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.VolumeService;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.exception.VolumeException;
import org.burnedpie.selena.persistance.dao.RadioStationDAO;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages = {"org.burnedpie.selena.audio", "org.burnedpie.selena.persistance.dao"})

public class RemoteController {

    Logger logger = Logger.getLogger(RemoteController.class.getName());

    @Autowired
    private AirplayService      airplayService;
    @Autowired
    private RadioService        radioService;
    @Autowired
    private RadioStationDAO     radioStationDAO;
    @Autowired
    private VolumeService       volumeService;

    // global constants
    public static final String SUCCESS              =   "SUCCESS";
    public static final String FAIL                 =   "FAIL";

    // player stopped constants and method
    public static final String PLAYER_STOPPED       =   "Player is stopped.";
    public static final String REST_STOP            =   "/stop";

    @RequestMapping(REST_STOP)
    ReturnValue stop() {
        if (radioService.isRadioOn()) {
            radioService.stopRadio();
        }
        if (airplayService.isAirplayOn()) {
            airplayService.turnAirplayOff();
        }
        return new ReturnValue(SUCCESS, PLAYER_STOPPED);
    }

    // volume constants and method
    public static final String VOLUME_TURNED_UP         =   "Volume is turned up.";
    public static final String VOLUME_TURNED_DOWN       =   "Volume is turned down.";
    public static final String VOLUME_TURNED_UP_FAIL    =   "Failed turning up the volume.";
    public static final String VOLUME_TURNED_DOWN_FAIL  =   "Failed turning down the volume.";
    public static final String REST_VOLUME_UP           =   "/volumeUp";
    public static final String REST_VOLUME_DOWN         =   "/volumeDown";

    @RequestMapping(REST_VOLUME_UP)
    ReturnValue volumUp() {
        try {
            volumeService.volumeUp();
            return new ReturnValue(SUCCESS, VOLUME_TURNED_UP);
        } catch (VolumeException e) {
            logger.severe(e.getMessage());
            return new ReturnValue(FAIL, VOLUME_TURNED_UP_FAIL);
        }
    }

    @RequestMapping(REST_VOLUME_DOWN)
    ReturnValue volumeDown() {
        try {
            volumeService.volumeDown();
            return new ReturnValue(SUCCESS, VOLUME_TURNED_DOWN);
        } catch (VolumeException e) {
            logger.severe(e.getMessage());
            return new ReturnValue(FAIL, VOLUME_TURNED_DOWN_FAIL);
        }
    }

    // radio constants and methods
    public static final String RADIO_STATION_SET            =   "Radio station set to {0}.";
    public static final String REST_PLAY_RADIO_STATION      =   "/playRadioStation";
    public static final String RADIO_STATION_SET_FAIL       =   "Failed setting radio station to {0}";
    public static final String RADIO_STATION_UNDEFINED_FAIL =   "Failed setting radio station to {0}";

    @RequestMapping(REST_PLAY_RADIO_STATION)
    ReturnValue playRadioStation(@RequestParam(value="radioStation", required = true) Integer channel) {
        if (airplayService.isAirplayOn()) {
            airplayService.turnAirplayOff();
        }
        if (radioService.isRadioOn()) {
            radioService.stopRadio();
        }
        RadioStation radioStationResult = radioStationDAO.findByChannel(channel);
        try {
            if (radioStationResult == null) {
                logger.info("Radio station not found :" + channel);
                airplayService.turnAirplayOn();
                return new ReturnValue(FAIL, RADIO_STATION_UNDEFINED_FAIL.replace("{0}", String.valueOf(channel)));
            }
            radioService.playRadioChannel(radioStationResult);
            return new ReturnValue(SUCCESS,
                    RADIO_STATION_SET.replace("{0}", String.valueOf(channel)));
        } catch (RadioException e) {
            logger.severe(e.getMessage());
            airplayService.turnAirplayOn();
            return new ReturnValue(FAIL,
                    RADIO_STATION_SET_FAIL.replace("{0}", String.valueOf(channel)));
        }
    }

    // airplay constants and methods
    public static final String AIRPLAY_STARTED          =   "Airplay is started.";
    public static final String AIRPLAY_ALREADY_STARTED  =   "Airplay is already started.";
    public static final String REST_START_AIRPLAY       =   "/startAirplay";

    @RequestMapping(REST_START_AIRPLAY)
    ReturnValue startAirplay() {
        if (radioService.isRadioOn()) {
            radioService.stopRadio();
        }
        if (airplayService.isAirplayOn()) {
            return new ReturnValue(SUCCESS, AIRPLAY_ALREADY_STARTED);
        } else {
            airplayService.turnAirplayOn();
            return new ReturnValue(SUCCESS, AIRPLAY_STARTED);
        }
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

    public RadioStationDAO getRadioStationDAO() {
        return radioStationDAO;
    }

    public void setRadioStationDAO(RadioStationDAO radioStationDAO) {
        this.radioStationDAO = radioStationDAO;
    }

    public VolumeService getVolumeService() {
        return volumeService;
    }

    public void setVolumeService(VolumeService volumeService) {
        this.volumeService = volumeService;
    }

    /**
     * Starts web server, for test purpose.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RemoteController.class, args);
    }

    @Bean
    public SessionFactory sessionFactory(){
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        return new MetadataSources( registry ).buildMetadata().buildSessionFactory();
    }
}