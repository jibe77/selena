package org.burnedpie.selena.audio.impl;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationDAO;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jibe on 05/08/16.
 */
@Component
public class ShairportSyncImpl implements AirplayService {

    @Autowired
    private NativeCommand nativeCommand;

    @Autowired
    ConfigurationDAO configurationDAO;

    private Thread thread;

    public void turnAirplayOn() throws AirplayException {
        final String serviceName = configurationDAO.findByKey(ConfigurationKeyEnum.AIRPLAY_NAME);
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    nativeCommand.launchNativeCommandAndReturnInputStreamValue("shairport-sync -a " + serviceName + " -- -c \"PCM\"");
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

    public ConfigurationDAO getConfigurationDAO() {
        return configurationDAO;
    }

    public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    public NativeCommand getNativeCommand() {
        return nativeCommand;
    }

    public void setNativeCommand(NativeCommand nativeCommand) {
        this.nativeCommand = nativeCommand;
    }

}
