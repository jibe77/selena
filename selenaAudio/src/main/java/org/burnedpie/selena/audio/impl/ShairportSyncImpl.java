package org.burnedpie.selena.audio.impl;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.audio.util.impl.NativeCommandImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.burnedpie.selena.persistance.PersistanceService;

import java.io.IOException;

/**
 * Created by jibe on 05/08/16.
 */
@Component
public class ShairportSyncImpl implements AirplayService {

    @Autowired
    private NativeCommand nativeCommand;

    @Autowired
    PersistanceService persistanceService;

    private Thread thread;

    public void turnAirplayOn() throws AirplayException {
        final String serviceName = persistanceService.findAirplayServiceName();
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
}
