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

    private static final String APPLICATION_URL = "http://localhost:8080";

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
        int station = 1;
        String url = APPLICATION_URL + RemoteController.REST_PLAY_RADIO_STATION + "?radioStation=" + station;

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(
                RemoteController.RADIO_STATION_SET.replace("{0}", String.valueOf(station)),
                returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerPlayRadio10() {
        // given that
        int station = 10;
        String url = APPLICATION_URL + RemoteController.REST_PLAY_RADIO_STATION + "?radioStation=" + station;

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(
                RemoteController.RADIO_STATION_SET.replace("{0}", String.valueOf(station)),
                returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerStartAirplay() {
        // given that
        String url = APPLICATION_URL + RemoteController.REST_START_AIRPLAY;

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_STARTED, returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerStop() {
        // given that
        String url = APPLICATION_URL + RemoteController.REST_STOP;

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.PLAYER_STOPPED, returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerVolumeUp() {
        // given that
        String url = APPLICATION_URL + RemoteController.REST_VOLUME_UP;

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_UP, returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerVolumeDown() {
        // given that
        String url = APPLICATION_URL + RemoteController.REST_VOLUME_DOWN;

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_DOWN, returnValue.getMessage());
    }
}
