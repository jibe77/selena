package org.burnedpie.selena.audio.util.impl;

import org.apache.commons.exec.*;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by jibe on 08/08/16.
 */
@Component()
public class NativeCommandImpl implements NativeCommand {

    private final Logger logger = Logger.getLogger(NativeCommandImpl.class.getName());

    public Executor launchNativeCommandAndReturnExecutor(String command, String ... args) throws IOException {
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
        ExecuteStreamHandler handler = new PumpStreamHandler(bos);
        executor.setStreamHandler(handler);

        executor.execute(cmdLine, resultHandler);
        return executor;
    }

    @Override
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
        ExecuteStreamHandler handler = new PumpStreamHandler(bos);
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