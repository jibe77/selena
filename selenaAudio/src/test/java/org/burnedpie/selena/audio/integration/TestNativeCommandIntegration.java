package org.burnedpie.selena.audio.integration;

import org.apache.commons.lang3.StringUtils;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Created by jibe on 08/08/16.
 */
@Category(IntegrationTest.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "spring.xml")
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestNativeCommandIntegration {

    private final Logger logger = Logger.getLogger(TestNativeCommandIntegration.class.getName());

    @Autowired
    private NativeCommand nativeCommand;

    @Test
    public void testNullCommand() {
        // given that
        String command = null;
        // when
        try {
            nativeCommand.launchNativeCommandAndReturnInputStreamValue(null);
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
            nativeDayOfTheMonth = nativeCommand.launchNativeCommandAndReturnInputStreamValue("date +%d");
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
        int returnValue = nativeCommand.launchNativeCommandAndReturnExitValue(command);

        // then
        Assert.assertTrue(new File(url.getPath()).exists());
        Assert.assertEquals(0, returnValue);
    }
}