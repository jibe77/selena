package org.burnedpie.selena.audio;

import org.burnedpie.selena.persistance.domain.RadioStation;

/**
 * Created by jibe on 02/08/16.
 */
public interface RadioService {
    void playRadioChannel(RadioStation radioStation);
    void stopRadio();
    boolean isRadioOn();
    void destroy();

    String getChannel();

    String getStation();
}
