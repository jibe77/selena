package org.burnedpie.selena.web.rest.controller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@RunWith(value = SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestRestController {

    @Autowired
    RemoteController remoteController;

    @Test
    public void testRemoteControllerPlayRadio() {
        // given that
        String url = "http://localhost:8080/playRadioStation?radioStation=1";
        // ... mockito ...

        // when
        RestReturnValue returnValue = remoteController.playRadioStation(1);

        // then
        Assert.assertEquals("OK", returnValue.getStatus());
        Assert.assertEquals("Radio station set to 1.", returnValue.getMessage());
        // ... mockito ... :
        // 1. si airplay, couper airplay
        // 2. si radio, couper radio
        // 3. lancer radio
        // 4. si échoue, lancer airplay
    }

}
