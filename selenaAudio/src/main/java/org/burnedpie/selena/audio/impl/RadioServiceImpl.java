package org.burnedpie.selena.audio.impl;

import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jibe on 08/08/16.
 */
@Component
@Scope("singleton")
public class RadioServiceImpl extends AbstractServiceAudio implements RadioService {

    private Logger logger = LoggerFactory.getLogger(RadioServiceImpl.class);

    @Autowired
    NativeCommand nativeCommand;

    @Autowired
    AirplayService airplayService;

    private String channel = "";
    private String station = "";

    public RadioServiceImpl() {
        super("mplayer");
    }

    @Override
    public synchronized void playRadioChannel(final RadioStation radioStation) {
        if (isRadioOn()) {
            logger.info("Stop currently played radio");
            stopRadio();
        } else if (airplayService.isAirplayOn()) {
            logger.info("Stop airplay before starting radio");
            airplayService.turnAirplayOff();
        }
        logger.info("Starting radio " + radioStation.getName() + " ...");
        try {
            logger.info("launching mplayer ...");
            channel = String.valueOf(radioStation.getChannel());
            station = radioStation.getName();
            Executor executor = nativeCommand.launchNativeCommandAndReturnExecutorAndTurnOnAirplayOnStopped(
                    command,
                    radioStation.getUrl().endsWith("wav") ? "" : "-playlist",
                    radioStation.getUrl());
            this.executor = executor;
        } catch (IOException | NullPointerException e) {
            logger.error(e.getMessage());
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RadioException(e);
        }
    }

    public synchronized void stopRadio() {
        channel = ""; station = ""; stopService();
    }

    public boolean isRadioOn() {
        return isServiceOn();
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public String getStation() {
        return station;
    }


}
