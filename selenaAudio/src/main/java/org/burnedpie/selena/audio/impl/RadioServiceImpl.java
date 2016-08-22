package org.burnedpie.selena.audio.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by jibe on 08/08/16.
 */
@Component
public class RadioServiceImpl implements RadioService {

    private Logger logger = Logger.getLogger(RadioServiceImpl.class.getName());

    @Autowired
    NativeCommand nativeCommand;

    Thread thread;

    public void playRadioChannel(final RadioStation radioStation) {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    nativeCommand.launchNativeCommandAndReturnExitValue("mplayer -playlist " + radioStation.getUrl());
                } catch (IOException | NullPointerException e) {
                    logger.severe(e.getMessage());
                    logger.severe(ExceptionUtils.getStackTrace(e));
                    throw new RadioException(e);
                }
            }
        };
        thread.start();
    }

    public void stopRadio() {
        thread.interrupt();
        thread = null;

    }

    public boolean isRadioOn() {
        return thread != null && thread.isAlive();
    }
}
