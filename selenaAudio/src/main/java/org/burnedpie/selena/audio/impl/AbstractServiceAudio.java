package org.burnedpie.selena.audio.impl;

import org.apache.commons.exec.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jibe on 31/08/16.
 */
public class AbstractServiceAudio {

    private Logger logger = LoggerFactory.getLogger(AbstractServiceAudio.class);

    protected Executor executor;

    protected String command;

    protected AbstractServiceAudio(String command) {
        this.command = command;
    }

    protected synchronized void stopService() {
        logger.info("Stopping " + command + " ...");
        if (isServiceOn()) {
            executor.getWatchdog().destroyProcess();
            logger.info("... done");
        } else {
            logger.info("...  is already interrupted.");
        }
    }

    protected synchronized boolean isServiceOn() {
        if (executor != null) {
            if (executor.getWatchdog() != null) {
                if (executor.getWatchdog().isWatching()) {
                    if (executor.getWatchdog().killedProcess() == false) {
                        logger.info("Service " + command + " is on.");
                        return true;
                    }
                }
            }
        }
        logger.info("Service " + command + " is off.");
        return false;
    }
}
