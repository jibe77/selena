package org.burnedpie.selena.audio.impl;

import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
public class RadioServiceImpl implements RadioService {

    private Logger logger = LoggerFactory.getLogger(RadioServiceImpl.class);

    @Autowired
    NativeCommand nativeCommand;

    Executor executor;

    @Override
    public synchronized void playRadioChannel(final RadioStation radioStation) {
        logger.info("Starting radio " + radioStation.getName() + " ...");
        try {
            logger.info("launching mplayer ...");
            Executor executor = nativeCommand.launchNativeCommandAndReturnExecutor("mplayer", "-playlist", radioStation.getUrl());
            this.executor = executor;
            // int returnValue = nativeCommand.readProcessAndReturnExitValue(process);
        } catch (IOException | NullPointerException e) {
            logger.error(e.getMessage());
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RadioException(e);
        }
    }

    public synchronized void stopRadio() {
        logger.info("Stopping radio ...");
        if (isRadioOn()) {
            executor.getWatchdog().destroyProcess();
            logger.info("... done");
        } else {
            logger.info("... Radio is already interrupted.");
        }
        executor = null;
    }

    public boolean isRadioOn() {
        return executor != null && executor.getWatchdog() != null && executor.getWatchdog().isWatching() && executor.getWatchdog().killedProcess() == false;
    }
}
