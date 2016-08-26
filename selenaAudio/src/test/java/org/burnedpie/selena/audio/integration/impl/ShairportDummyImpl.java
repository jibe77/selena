package org.burnedpie.selena.audio.integration.impl;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by jibe on 11/08/16.
 */
public class ShairportDummyImpl implements AirplayService {

    private String serviceName;

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
                    // command is different because shairport-sync doesn't exist on my local machine
                    nativeCommand.launchNativeCommandAndReturnInputStreamValue("shairport -a " + serviceName + " -- -c \"PCM\"");
                } catch (IOException e) {
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
}
