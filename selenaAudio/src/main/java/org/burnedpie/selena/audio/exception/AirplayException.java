package org.burnedpie.selena.audio.exception;

import java.net.UnknownHostException;

/**
 * Exception during Airplay methods calls or execution.
 *
 * Created by jibe on 03/08/16.
 */
public class AirplayException extends RuntimeException {
    public AirplayException(Exception e) {
        super(e);
    }
}
