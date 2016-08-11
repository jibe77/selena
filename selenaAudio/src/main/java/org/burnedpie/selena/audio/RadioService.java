package org.burnedpie.selena.audio;

/**
 * Created by jibe on 02/08/16.
 */
public interface RadioService {
    public void playRadioChannel(String urlStream);
    public void stopRadio();
    public boolean isRadioOn();
}
