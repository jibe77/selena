package org.burnedpie.selena.web.rest.controller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

/**
 * Created by jibe on 13/08/16.
 */
@Category(IntegrationTest.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestRestController {

    Logger logger = Logger.getLogger(TestRestController.class.getName());

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
    public void testRemoteController() {
        // given that
        String url = "http://localhost:8080/playRadioStation?radioStation=1";

        // when
        RestTemplate restTemplate = new RestTemplate();
        RestReturnValue returnValue = restTemplate.getForObject(url, RestReturnValue.class);

        // then
        Assert.assertEquals("OK", returnValue.getStatus());
        Assert.assertEquals("Radio station set to 1.", returnValue.getMessage());
    }
}
