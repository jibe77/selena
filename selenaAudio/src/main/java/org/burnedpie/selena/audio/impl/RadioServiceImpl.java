package org.burnedpie.selena.audio.impl;

import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jibe on 08/08/16.
 */
@Component
public class RadioServiceImpl implements RadioService {

    @Autowired
    NativeCommand nativeCommand;

    Thread thread;

    public void playRadioChannel(final String urlStream) {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    nativeCommand.launchNativeCommandAndReturnExitValue("mplayer -playlist " + urlStream);
                } catch (IOException e) {
                    throw new RadioException(e);
                }
            }
        };
        thread.start();
    }

    public void stopRadio() {
        thread.interrupt();
        thread = null;

    }

    public boolean isRadioOn() {
        return thread != null && thread.isAlive();
    }
}
