package org.burnedpie.selena.audio.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jibe on 09/08/16.
 */
public interface NativeCommand {
    Process launchNativeCommandAndReturnProcess(String s) throws IOException;
    String readProcessAndReturnInputStreamValue(Process process) throws IOException;
    int readProcessAndReturnExitValue(Process process) throws IOException;
}
