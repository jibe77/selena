package org.burnedpie.selena.web.rest.controller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

/**
 * Created by jibe on 13/08/16.
 *
 */
@Category(IntegrationTest.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestIntegrationRestController {

    Logger logger = Logger.getLogger(TestIntegrationRestController.class.getName());

    ConfigurableApplicationContext configurableApplicationContext;

    @Before
    public void setUp() {
        configurableApplicationContext =
                SpringApplication.run(RemoteController.class);
    }

    @After
    public void tearDown() {
        if (configurableApplicationContext != null)
            configurableApplicationContext.close();

    }

    @Test
    public void testRemoteControllerPlayRadio() {
        // given that
        String url = "http://localhost:8080/playRadioStation?radioStation=1";

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals("OK", returnValue.getStatus());
        Assert.assertEquals("Radio station set to 1.", returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerPlayRadio10() {
        // given that
        String url = "http://localhost:8080/playRadioStation?radioStation=10";

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals("OK", returnValue.getStatus());
        Assert.assertEquals("Radio station set to 10.", returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerStartAirplay() {
        // given that
        String url = "http://localhost:8080/startAirplay";

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals("OK", returnValue.getStatus());
        Assert.assertEquals("Airplay is started.", returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerStop() {
        // given that
        String url = "http://localhost:8080/stop";

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals("OK", returnValue.getStatus());
        Assert.assertEquals("Player is stopped.", returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerVolumeUp() {
        // given that
        String url = "http://localhost:8080/volumeUp";

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals("OK", returnValue.getStatus());
        Assert.assertEquals("Volume is turned up.", returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerVolumeDown() {
        // given that
        String url = "http://localhost:8080/volumeDown";

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals("OK", returnValue.getStatus());
        Assert.assertEquals("Volume is turned down.", returnValue.getMessage());
    }
}
