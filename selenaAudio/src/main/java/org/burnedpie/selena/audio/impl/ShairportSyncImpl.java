package org.burnedpie.selena.audio.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by jibe on 05/08/16.
 */
@Component
public class ShairportSyncImpl implements AirplayService {

    private Logger logger = Logger.getLogger(ShairportSyncImpl.class.getName());

    @Autowired
    private NativeCommand nativeCommand;

    @Autowired
    ConfigurationRepository configurationRepository;

    private Thread thread;

    public void turnAirplayOn() throws AirplayException {
        final String serviceName = configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME).getConfigValue();
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    nativeCommand.launchNativeCommandAndReturnInputStreamValue("shairport-sync -a " + serviceName + " -- -c \"PCM\"");
                } catch (IOException | NullPointerException e) {
                    logger.severe(e.getMessage());
                    logger.severe(ExceptionUtils.getStackTrace(e));
                    throw new RadioException(e);
                }
            }
        };
        thread.start();
    }

    public void turnAirplayOff() {
        thread.interrupt();
        thread = null;
    }

    public boolean isAirplayOn() {
        return thread != null && thread.isAlive();
    }

    public ConfigurationRepository getConfigurationRepository() {
        return configurationRepository;
    }

    public void setConfigurationRepository(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public NativeCommand getNativeCommand() {
        return nativeCommand;
    }

    public void setNativeCommand(NativeCommand nativeCommand) {
        this.nativeCommand = nativeCommand;
    }

}
