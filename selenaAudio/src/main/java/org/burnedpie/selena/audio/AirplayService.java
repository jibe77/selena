package org.burnedpie.selena.audio;

import org.burnedpie.selena.audio.exception.AirplayException;

/**
 * Created by jibe on 02/08/16.
 */
public interface AirplayService {
    public void turnAirplayOn(String serviceName) throws AirplayException;
    public void turnAirplayOff();
    public boolean isAirplayOn();
}
