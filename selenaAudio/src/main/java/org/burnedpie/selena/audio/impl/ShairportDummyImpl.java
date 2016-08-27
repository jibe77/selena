package org.burnedpie.selena.audio.impl;

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

    private Thread thread;
    Process process;

    @Override
    public void turnAirplayOn() throws AirplayException {
        final String serviceName = configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME).getConfigValue();
        if (serviceName == null) {
            throw new RadioException("Service name should not be null");
        }
        logger.info("Starting airplay with name " + serviceName + "...");
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    String commandLine = "shairport -a " + serviceName + " -- -c \"PCM\"";
                    logger.info("launching shairport with command " + commandLine + " ...");
                    // command is different because shairport-sync doesn't exist on my local machine
                    process = nativeCommand.launchNativeCommandAndReturnProcess(commandLine);
                    String returnValue = nativeCommand.readProcessAndReturnInputStreamValue(process);
                    logger.info("shairport is finished with return value " + returnValue + ".");
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    logger.error(ExceptionUtils.getStackTrace(e));
                    throw new AirplayException(e);
                }
            }
        };
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void turnAirplayOff() {
        process.destroy();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (process.isAlive()) {
            process.destroyForcibly();
        }
        thread = null;
    }

    public boolean isAirplayOn() {
        if (thread == null) {
            logger.info("Airplay is off.");
            return false;
        } else {
            if (thread.isAlive()) {
                logger.info("Airplay is on.");
                return true;
            } else {
                logger.info("Airplay is off.");
                return false;
            }

        }
    }
}
