package org.burnedpie.selena.audio.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.VolumeService;
import org.burnedpie.selena.audio.exception.VolumeException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jibe on 14/08/16.
 */
@Component
@Scope("singleton")
public class VolumeServiceImpl implements VolumeService {

    private Logger logger = LoggerFactory.getLogger(RadioServiceImpl.class);

    @Autowired
    NativeCommand nativeCommand;

    @Autowired
    ConfigurationRepository configurationRepository;

    public void volumeUp() {
        controlVolume(ConfigurationKeyEnum.VOLUME_UP_COMMAND);
    }

    public void volumeDown() {
        controlVolume(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND);
    }

    private void controlVolume(ConfigurationKeyEnum configurationKeyEnum) {
        try {
            String value = configurationRepository.findByConfigKey(configurationKeyEnum).getConfigValue();
            if (value == null) {
                throw new VolumeException("command is undefined :" + configurationKeyEnum.name());
            }

            int exitValue = nativeCommand.launchCommandAndReturnExitValue(value);
            // exit value is not reliable.
        } catch (IOException | NullPointerException e) {
            logger.warn(e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
            throw new VolumeException(e);
        }
    }
}
