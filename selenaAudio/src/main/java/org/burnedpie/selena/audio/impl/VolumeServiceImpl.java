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
import java.util.logging.Logger;

/**
 * Created by jibe on 14/08/16.
 */
@Component
@Scope("singleton")
public class VolumeServiceImpl implements VolumeService {

    private Logger logger = Logger.getLogger(RadioServiceImpl.class.getName());

    @Autowired
    NativeCommand nativeCommand;

    @Autowired
    ConfigurationRepository configurationRepository;

    public void volumeUp() {
        try {
            String value = configurationRepository.findByConfigKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND).getConfigValue();
            if (value == null) {
                throw new VolumeException("Volume up command is undefined.");
            }
            nativeCommand.launchNativeCommandAndReturnProcess(value);
        } catch (IOException | NullPointerException e) {
            logger.severe(e.getMessage());
            logger.severe(ExceptionUtils.getStackTrace(e));
            throw new VolumeException(e);
        }
    }

    public void volumeDown() {
        try {
            String value = configurationRepository.findByConfigKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND).getConfigValue();
            if (value == null) {
                throw new VolumeException("Volume down command is undefined.");
            }
            nativeCommand.launchNativeCommandAndReturnProcess(value);
        } catch (IOException | NullPointerException e) {
            logger.severe(e.getMessage());
            logger.severe(ExceptionUtils.getStackTrace(e));
            throw new VolumeException(e);
        }
    }
}
