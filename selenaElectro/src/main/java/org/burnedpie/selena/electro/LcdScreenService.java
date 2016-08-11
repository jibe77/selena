package org.burnedpie.selena.electro;

/**
 * Created by jibe on 02/08/16.
 */
public interface LcdScreenService {
    public void turnBrightnessOff();
    public void turnBrightnessOn(int percent);
    public void setBrightness(int percent);

    public void setText(String text);
}
