package org.burnedpie.selena.audio.exception;

/**
 * Created by jibe on 03/08/16.
 */
public class VolumeException extends RuntimeException {
    public VolumeException(Exception e) {
        super(e);
    }

    public VolumeException(String message) {
        super(message);
    }
}
