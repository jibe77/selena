package org.burnedpie.selena.audio.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.VolumeService;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.exception.VolumeException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationDAO;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by jibe on 14/08/16.
 */
@Component
public class VolumeServiceImpl implements VolumeService {

    private Logger logger = Logger.getLogger(RadioServiceImpl.class.getName());

    @Autowired
    NativeCommand nativeCommand;

    @Autowired
    ConfigurationDAO configurationDAO;

    public void volumeUp() {
        try {
            String value = configurationDAO.findByKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND);
            if (value == null) {
                throw new VolumeException("Volume up command is undefined.");
            }
            nativeCommand.launchNativeCommandAndReturnInputStreamValue(value);
        } catch (IOException | NullPointerException e) {
            logger.severe(e.getMessage());
            logger.severe(ExceptionUtils.getStackTrace(e));
            throw new VolumeException(e);
        }
    }

    public void volumeDown() {
        try {
            String value = configurationDAO.findByKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND);
            if (value == null) {
                throw new VolumeException("Volume down command is undefined.");
            }
            nativeCommand.launchNativeCommandAndReturnInputStreamValue(value);
        } catch (IOException | NullPointerException e) {
            logger.severe(e.getMessage());
            logger.severe(ExceptionUtils.getStackTrace(e));
            throw new VolumeException(e);
        }
    }
}
