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
public class ShairportSyncImpl extends AbstractServiceAudio implements AirplayService {

    private Logger logger = Logger.getLogger(ShairportSyncImpl.class.getName());

    @Autowired
    protected NativeCommand nativeCommand;

    @Autowired
    ConfigurationRepository configurationRepository;


    public ShairportSyncImpl() {
        super("shairport-sync");
    }

    public void turnAirplayOn() throws AirplayException {
        final String serviceName = configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME).getConfigValue();
        if (serviceName == null) {
            throw new RadioException("Service name should not be null");
        }
        logger.info("Starting airplay with name " + serviceName + "...");
        try {
            logger.info("Starting " + command + " ...");
            this.executor = startCommand(serviceName);
            logger.info("... " + command + " is running.");
        } catch (IOException | NullPointerException e) {
            logger.severe(e.getMessage());
            logger.severe(ExceptionUtils.getStackTrace(e));
            throw new RadioException(e);
        }
    }

    protected Executor startCommand(String serviceName) throws IOException {
        return nativeCommand.launchNativeCommandAndReturnExecutor(command, "-a", serviceName, "--", "\"PCM\"");
    }

    public void turnAirplayOff() {
        super.stopService();
    }

    public boolean isAirplayOn() {
        return isServiceOn();
    }
}
