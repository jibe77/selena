package org.burnedpie.selena.audio;

import org.burnedpie.selena.audio.exception.AirplayException;

/**
 * Created by jibe on 02/08/16.
 */
public interface AirplayService {
    void turnAirplayOn() throws AirplayException;
    void turnAirplayOff();
    boolean isAirplayOn();
    void destroy();
}
