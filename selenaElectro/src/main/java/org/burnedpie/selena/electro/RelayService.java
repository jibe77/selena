package org.burnedpie.selena.electro;

/**
 * Created by jibe on 02/08/16.
 */
public interface RelayService {
    public void turnRelayOn();
    public void turnRelayOff();

    public void startRelayManager();
    public void stopRelayManager();
}
