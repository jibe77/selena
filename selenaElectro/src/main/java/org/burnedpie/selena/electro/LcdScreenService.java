package org.burnedpie.selena.electro;

/**
 * Created by jibe on 02/08/16.
 */
public interface LcdScreenService {
    void turnBrightnessOff();
    void turnBrightnessOn(int percent);
    void setBrightness(int percent);
    void setText(String text);
}
