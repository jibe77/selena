package org.burnedpie.selena.audio.impl;

import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by jibe on 05/08/16.
 */
@Component
@Scope("singleton")
public class ShairportSyncImpl implements AirplayService {

    private Logger logger = Logger.getLogger(ShairportSyncImpl.class.getName());

    @Autowired
    private NativeCommand nativeCommand;

    @Autowired
    ConfigurationRepository configurationRepository;

    private Executor executor;

    public void turnAirplayOn() throws AirplayException {
        final String serviceName = configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME).getConfigValue();
        if (serviceName == null) {
            throw new RadioException("Service name should not be null");
        }
        logger.info("Starting airplay with name " + serviceName + "...");
        try {
            logger.info("Starting shairport-sync ...");
            this.executor = nativeCommand.launchNativeCommandAndReturnExecutor("shairport-sync", "-a", serviceName, "--", "\"PCM\"");
            logger.info("... shairport-sync is running.");
        } catch (IOException | NullPointerException e) {
            logger.severe(e.getMessage());
            logger.severe(ExceptionUtils.getStackTrace(e));
            throw new RadioException(e);
        }
    }

    public void turnAirplayOff() {
        if (isAirplayOn()) {
            logger.info("Turning off shairport-sync ...");
            executor.getWatchdog().destroyProcess();
            logger.info("... done");
        } else {
            logger.info("Can't turn off shairport-sync, it is already off.");
        }
    }

    public boolean isAirplayOn() {
        if (executor == null) {
            logger.info("Airplay is off.");
            return false;
        } else {
            if (executor.getWatchdog() != null && executor.getWatchdog().isWatching() && executor.getWatchdog().killedProcess() == false) {
                logger.info("Airplay is on.");
                return true;
            } else {
                logger.info("Airplay is off.");
                return false;
            }

        }
    }
}
