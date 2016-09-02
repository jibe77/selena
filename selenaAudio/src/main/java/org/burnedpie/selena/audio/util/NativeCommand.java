package org.burnedpie.selena.audio.util;

import org.apache.commons.exec.Executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jibe on 09/08/16.
 */
public interface NativeCommand {
    Executor launchNativeCommandAndReturnExecutor(String command, String ... param) throws IOException;
    Executor launchNativeCommandAndReturnExecutorAndTurnOnAirplayOnStopped(String command, String ... param) throws IOException;
    int launchCommandAndReturnExitValue(String command, String ... param) throws IOException;
    String launchNativeCommandAndReturnOutput(String command, String ... args) throws IOException;
}
