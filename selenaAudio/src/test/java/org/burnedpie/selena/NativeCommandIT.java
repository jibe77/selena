package org.burnedpie.selena;

import org.apache.commons.exec.Executor;
import org.burnedpie.selena.audio.util.impl.NativeCommandImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by jibe on 08/08/16.
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {NativeCommandImpl.class})
public class NativeCommandIT {

    private final Logger logger = Logger.getLogger(NativeCommandIT.class.getName());

    @Autowired
    private org.burnedpie.selena.audio.util.NativeCommand nativeCommand;

    @Test
    public void testNullCommand() {
        // given that
        String command = null;
        // when
        String result ;
        try {
            Executor executor = nativeCommand.launchNativeCommandAndReturnExecutor(null);
        } catch (IOException e) {
            // then
            e.printStackTrace();
            return;
        }

        Assert.fail();
    }

    /*
    @Test
    public void testDateCommand() {
        // given that
        LocalDateTime timePoint = LocalDateTime.now();     // The current date and time
        int expectedDayOfTheMonth = timePoint.getDayOfMonth();
        String expectedDayOfTheMonthWithTwoCaracters = StringUtils.leftPad(expectedDayOfTheMonth + "", 2, '0');

        // when
        String nativeDayOfTheMonth = null;
        try {
            Executor executor = nativeCommand.launchNativeCommandAndReturnExecutor("date +%d");
            ExecuteStreamHandlerWithReader executeStreamHandlerWithReader = (ExecuteStreamHandlerWithReader) executor.getStreamHandler();
            String result = executeStreamHandlerWithReader.readFromOutputStream();
            logger.info("result = " + result);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }

        // then
        Assert.assertEquals(expectedDayOfTheMonthWithTwoCaracters, nativeDayOfTheMonth);
    }*/

    @Test()
    public void testMplayerCommand() throws IOException {
        // given that
        URL url = getClass().getClassLoader().getResource("sample.wav");
        String command = "mplayer";
        logger.info("playing file " + url.getPath());

        // when
        int returnValue = nativeCommand.launchCommandAndReturnExitValue(command, url.getPath());

        // then
        Assert.assertTrue(new File(url.getPath()).exists());
        Assert.assertEquals(0, returnValue);
    }
}