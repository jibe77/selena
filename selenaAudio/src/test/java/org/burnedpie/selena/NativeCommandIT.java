package org.burnedpie.selena;

import org.apache.commons.lang3.StringUtils;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.audio.util.impl.NativeCommandImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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
    private NativeCommand nativeCommand;

    @Test
    public void testNullCommand() {
        // given that
        String command = null;
        // when
        String result ;
        try {
            Process process = nativeCommand.launchNativeCommandAndReturnProcess(null);
            result = nativeCommand.readProcessAndReturnInputStreamValue(process);
        } catch (IOException e) {
            // then
            e.printStackTrace();
            return;
        }

        Assert.fail();
    }

    @Test
    public void testDateCommand() {
        // given that
        LocalDateTime timePoint = LocalDateTime.now();     // The current date and time
        int expectedDayOfTheMonth = timePoint.getDayOfMonth();
        String expectedDayOfTheMonthWithTwoCaracters = StringUtils.leftPad(expectedDayOfTheMonth + "", 2, '0');

        // when
        String nativeDayOfTheMonth = null;
        try {
            Process process = nativeCommand.launchNativeCommandAndReturnProcess("date +%d");
            nativeDayOfTheMonth = nativeCommand.readProcessAndReturnInputStreamValue(process);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }

        // then
        Assert.assertEquals(expectedDayOfTheMonthWithTwoCaracters, nativeDayOfTheMonth);
    }

    @Test()
    public void testMplayerCommand() throws IOException {
        // given that
        URL url = getClass().getClassLoader().getResource("sample.Wav");
        String command = "mplayer " + url.getPath();
        logger.info("player file " + url.getPath());

        // when
        Process process = nativeCommand.launchNativeCommandAndReturnProcess(command);
        int returnValue = nativeCommand.readProcessAndReturnExitValue(process);

                // then
        Assert.assertTrue(new File(url.getPath()).exists());
        Assert.assertEquals(0, returnValue);
    }
}