package org.burnedpie.selena.audio.impl;

import org.burnedpie.selena.audio.VolumeService;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.exception.VolumeException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by jibe on 14/08/16.
 */
public class VolumeServiceImpl implements VolumeService {

    @Autowired
    NativeCommand nativeCommand;

    public void volumeUp() {
        try {
            // TODO : vérifier la commande.
            nativeCommand.launchNativeCommandAndReturnInputStreamValue("aumix +1%");
        } catch (IOException e) {
            throw new VolumeException(e);
        }
    }

    public void volumeDown() {
        try {
            // TODO : vérifier la commande.
            nativeCommand.launchNativeCommandAndReturnInputStreamValue("aumix -1%");
        } catch (IOException e) {
            throw new VolumeException(e);
        }
    }
}
