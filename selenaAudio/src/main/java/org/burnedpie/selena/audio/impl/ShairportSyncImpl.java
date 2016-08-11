package org.burnedpie.selena.audio.impl;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.audio.util.impl.NativeCommandImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jibe on 05/08/16.
 */
@Component
public class ShairportSyncImpl implements AirplayService{

    private String serviceName;

    @Autowired
    private NativeCommand nativeCommand;

    Thread thread;

    public void turnAirplayOn(final String serviceName) throws AirplayException {
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
        return;

    }

    public void turnAirplayOff() {
        thread.interrupt();
        thread = null;
    }

    public boolean isAirplayOn() {
        return thread != null && thread.isAlive();
    }
}
