package org.burnedpie.selena.audio.impl;

import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jibe on 05/08/16.
 */
@Component
@Scope("singleton")
@Profile("raspbian")
public class ShairportSyncImpl extends AbstractServiceAudio implements AirplayService {

    private Logger logger = LoggerFactory.getLogger(ShairportSyncImpl.class);

    @Autowired
    NativeCommand nativeCommand;

    @Autowired
    RadioService radioService;

    @Autowired
    ConfigurationRepository configurationRepository;


    public ShairportSyncImpl() {
        super("shairport-sync");
    }

    public synchronized void turnAirplayOn() throws AirplayException {
        if (isAirplayOn()) {
            logger.warn("Can't start " + command + " because it's already on.");
            return;
        } else if (radioService.isRadioOn()) {
            radioService.stopRadio();
        }

        final Configuration configuration = configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME.name());
        String serviceName;
        if (configuration == null || configuration.getConfigValue() == null) {
            serviceName = "SELENA Airplay";
        } else {
            serviceName = configuration.getConfigValue();
        }
        logger.info("Starting airplay with name " + serviceName + "...");
        try {
            logger.info("Starting " + command + " ...");
            this.executor = startCommand(serviceName);
            logger.info("... " + command + " is running.");
        } catch (IOException | NullPointerException e) {
            logger.warn(e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
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
