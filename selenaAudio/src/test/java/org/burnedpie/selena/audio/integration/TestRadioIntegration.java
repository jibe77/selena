package org.burnedpie.selena.audio.integration;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.logging.Logger;

/**
 * Created by jibe on 25/07/16.
 */
@Category(IntegrationTest.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "spring.xml")
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestRadioIntegration {

    private static final Logger s_logger = Logger.getLogger(TestRadioIntegration.class.getName());

    @Autowired
    RadioService radioService;

    @After
    public void tearDown() {
        radioService.stopRadio();
    }

    @Test
    public void testRadio() throws Exception {
        // given that
        String playlist = "http://www.listenlive.eu/europe1.m3u";
        // when
        radioService.playRadioChannel(playlist);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // then
        Assert.assertTrue(radioService.isRadioOn());
    }

    @Test
    public void testRadioStop() throws Exception {
        // given that
        String playlist = "http://www.listenlive.eu/europe1.m3u";
        // when
        radioService.playRadioChannel(playlist);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        radioService.stopRadio();

        // then
        Assert.assertFalse(radioService.isRadioOn());
    }
}