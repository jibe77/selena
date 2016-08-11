package org.burnedpie.selena.audio.integration;

import org.burnedpie.selena.audio.AirplayService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
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
public class TestAirplayIntegration {

    private static final Logger s_logger = Logger.getLogger(TestAirplayIntegration.class.getName());

    @Autowired
    AirplayService airplayService;

    @After
    public void tearDown() {
        airplayService.turnAirplayOff();
    }

    @Test
    public void testRadio() throws Exception {
        // given that
        String serviceName = "[selena]test-integration";
        // when
        airplayService.turnAirplayOn(serviceName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // then
        Assert.assertTrue(airplayService.isAirplayOn());
    }

}