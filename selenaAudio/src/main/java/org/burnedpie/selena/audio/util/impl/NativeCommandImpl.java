package org.burnedpie.selena.audio.util.impl;

import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created by jibe on 08/08/16.
 */
@Component()
public class NativeCommandImpl implements NativeCommand {

    private final Logger logger = Logger.getLogger(NativeCommandImpl.class.getName());

    public String launchNativeCommandAndReturnInputStreamValue(String s) throws IOException {
        Process process = Runtime.getRuntime().exec(s);
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        bufferedReader.close();
        return line;
    }


    public int launchNativeCommandAndReturnExitValue(String s) throws IOException {
        Process process = Runtime.getRuntime().exec(s);
        try {
            process.waitFor();
            return process.exitValue();
        } catch (InterruptedException e) {
            process.destroy();
            try {
                logger.info("waiting for command to finish ...");
                process.waitFor();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            int exitValue = process.exitValue();
            logger.info("Interrupted with exit value " + exitValue);
            return exitValue;
        }
    }

}
