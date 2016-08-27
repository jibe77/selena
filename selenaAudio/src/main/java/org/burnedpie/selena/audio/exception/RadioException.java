package org.burnedpie.selena.audio.exception;

import org.burnedpie.selena.persistance.domain.RadioStation;

/**
 * Created by jibe on 03/08/16.
 */
public class RadioException extends RuntimeException {
    public RadioException(Exception e) {
        super(e);
    }

    public RadioException(String message) {
        super(message);
    }
}
