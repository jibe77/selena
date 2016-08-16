package org.burnedpie.selena.audio;

/**
 * Created by jibe on 02/08/16.
 */
public interface RadioService {
    void playRadioChannel(String urlStream);
    void stopRadio();
    boolean isRadioOn();
}
