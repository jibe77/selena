package org.burnedpie.selena.audio.impl;

import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jibe on 11/08/16.
 */
@Component
@Scope("singleton")
public class ShairportDummyImpl implements AirplayService {

    private Logger logger = LoggerFactory.getLogger(ShairportDummyImpl.class);

    @Autowired
    private NativeCommand nativeCommand;

    @Autowired
    ConfigurationRepository configurationRepository;

    private Executor executor;

    @Override
    public void turnAirplayOn() throws AirplayException {
        final String serviceName = configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME).getConfigValue();
        if (serviceName == null) {
            throw new RadioException("Service name should not be null");
        }
        logger.info("Starting airplay with name " + serviceName + "...");
        try {
            logger.info("launching shairport ...");
            // command is different because shairport-sync doesn't exist on my local machine
            this.executor = nativeCommand.launchNativeCommandAndReturnExecutor("shairport", "-a", serviceName, "--", "-c", "\"PCM\"");
            // String returnValue = nativeCommand.launchNativeCommandAndReturnExecutor(executor);
            logger.info("shairport is running ...");
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new AirplayException(e);
        }
    }

    public void turnAirplayOff() {
        if (isAirplayOn()) {
            logger.info("Turning off shairport ...");
            executor.getWatchdog().stop();
            logger.info("... done");
            executor = null;
        } else {
            logger.info("Can't turn off shairport, it is already off.");
        }
    }

    public boolean isAirplayOn() {
        if (executor == null) {
            logger.info("shairport is off.");
            return false;
        } else {
            logger.info("shairport is on.");
            return true;
        }
    }
}
