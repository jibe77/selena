package org.burnedpie.selena.audio.util;

import org.apache.commons.exec.*;
import org.burnedpie.selena.audio.AirplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jibe on 08/08/16.
 */
@Component()
public class NativeCommand {

    @Autowired
    AirplayService airplayService;

    private final Logger logger = LoggerFactory.getLogger(NativeCommand.class);

    public Executor launchNativeCommandAndReturnExecutor(String command, String ... args) throws IOException {
        return launch(false, command, args);
    }

    public Executor launchNativeCommandAndReturnExecutorAndTurnOnAirplayOnStopped(String command, String ... args) throws IOException {
        return launch(true, command, args);
    }

    private Executor launch(boolean launchAirplayOnStop, String command, String ... args) throws IOException {
        if (command == null) {
            throw new IOException("Command is null.");
        }
        CommandLine cmdLine = new CommandLine(command);
        for (String arg : args) {
            cmdLine.addArgument(arg);
        }
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(-1);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(new PumpStreamHandler() {

            boolean isStopped = false;

            @Override
            public void stop() throws IOException {
                super.stop();
                logger.info("The command " + command + " is stopped.");
                isStopped = true;
                if (launchAirplayOnStop) {
                    logger.info("Service is stopped, then starting airplay automatically ...");
                    airplayService.turnAirplayOn();
                    logger.info("... done");
                }
            }
        });

        executor.execute(cmdLine, resultHandler);
        return executor;
    }


    public String launchNativeCommandAndReturnOutput(String command, String ... args) throws IOException {
        if (command == null) {
            throw new IOException("Command is null.");
        }
        CommandLine cmdLine = new CommandLine(command);
        for (String arg : args) {
            cmdLine.addArgument(arg);
        }
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(3000); // 3 seconds
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ExecuteStreamHandler handler = new PumpStreamHandler(bos);
        executor.setStreamHandler(handler);

        executor.execute(cmdLine, resultHandler);
        try {
            resultHandler.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String output;
        output = bos.toString();
        return output.trim();
    }

    public int launchCommandAndReturnExitValue(String command, String ... args) throws IOException {
        if (command == null) {
            throw new IOException("Command is null.");
        }
        CommandLine cmdLine = new CommandLine(command);
        for (String arg : args) {
            cmdLine.addArgument(arg);
        }
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(-1);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PumpStreamHandler handler = new PumpStreamHandler(bos);
        executor.setStreamHandler(handler);

        executor.execute(cmdLine, resultHandler);
        try {
            resultHandler.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int exitValue = resultHandler.getExitValue();
        return exitValue;
    }


}