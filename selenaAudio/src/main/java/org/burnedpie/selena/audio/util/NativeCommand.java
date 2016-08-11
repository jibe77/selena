package org.burnedpie.selena.audio.util;

import java.io.IOException;

/**
 * Created by jibe on 09/08/16.
 */
public interface NativeCommand {

    public String launchNativeCommandAndReturnInputStreamValue(String s) throws IOException;

    public int launchNativeCommandAndReturnExitValue(String s) throws IOException;
}
