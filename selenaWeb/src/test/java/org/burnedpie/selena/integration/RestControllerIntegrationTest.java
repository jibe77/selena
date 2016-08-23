package org.burnedpie.selena.integration;

import org.burnedpie.selena.persistance.dao.ConfigurationDAO;
import org.burnedpie.selena.persistance.dao.RadioStationDAO;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.burnedpie.selena.web.rest.controller.RemoteController;
import org.burnedpie.selena.web.rest.controller.ReturnValue;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by jibe on 13/08/16.
 *
 */
@Category(IntegrationTest.class)
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = "classpath:spring-context-db-integration.xml")
@SpringBootTest(classes = RemoteController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestControllerIntegrationTest {

    Logger logger = Logger.getLogger(RestControllerIntegrationTest.class.getName());

    ConfigurableApplicationContext configurableApplicationContext;

    @LocalServerPort
    private int port;

    private URL base;
    private TestRestTemplate template;

    @Autowired
    RadioStationDAO radioStationDAO;

    @Autowired
    ConfigurationDAO configurationDAO;

    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        template = new TestRestTemplate();

        if (radioStationDAO.findByChannel(1) == null) {
            RadioStation radioStation = new RadioStation();
            radioStation.setName("Europe1");
            radioStation.setUrl("http://e1-live-mp3-128.scdn.arkena.com/europe1.mp3");
            radioStation.setChannel(1);
            radioStationDAO.saveRadioStation(radioStation);
        }

        if (configurationDAO.findByKey(ConfigurationKeyEnum.AIRPLAY_NAME) == null) {
            Configuration configuration = new Configuration();
            configuration.setConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME);
            configuration.setConfigValue("[selena]integration-test");
            configurationDAO.saveConfiguration(configuration);
        }

        if (configurationDAO.findByKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND) == null) {
            Configuration configuration = new Configuration();
            configuration.setConfigKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND);
            configuration.setConfigValue("echo volume_up");
            configurationDAO.saveConfiguration(configuration);
        }

        if (configurationDAO.findByKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND) == null) {
            Configuration configuration = new Configuration();
            configuration.setConfigKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND);
            configuration.setConfigValue("echo volume_down");
            configurationDAO.saveConfiguration(configuration);
        }

        Assert.assertNotNull(configurationDAO.findByKey(ConfigurationKeyEnum.AIRPLAY_NAME));
        Assert.assertNotNull(configurationDAO.findByKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND));
        Assert.assertNotNull(configurationDAO.findByKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND));
    }

    @After
    public void tearDown() {
        if (configurableApplicationContext != null)
            configurableApplicationContext.close();

    }

    @Test
    public void testRemoteControllerPlayRadio() {
        // given that
        int channel = 1;
        String url = base + RemoteController.REST_PLAY_RADIO_STATION + "?radioStation=" + channel;

        // when
        RestTemplate restTemplate = new RestTemplate();
        ReturnValue returnValue = restTemplate.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(
                RemoteController.RADIO_STATION_SET.replace("{0}", String.valueOf(channel)),
                returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerPlayRadio10() {
        // given that
        int channel = 10;
        String url = base + RemoteController.REST_PLAY_RADIO_STATION + "?radioStation=" + channel;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.FAIL, returnValue.getStatus());
        Assert.assertEquals(
                RemoteController.RADIO_STATION_UNDEFINED_FAIL.replace("{0}", String.valueOf(channel)),
                returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerStartAirplay() {
        // given that
        String url = base + RemoteController.REST_START_AIRPLAY;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_STARTED, returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerStop() {
        // given that
        String url = base + RemoteController.REST_STOP;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.PLAYER_STOPPED, returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerVolumeUp() {
        // given that
        String url = base + RemoteController.REST_VOLUME_UP;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_UP, returnValue.getMessage());
    }

    @Test
    public void testRemoteControllerVolumeDown() {
        // given that
        String url = base + RemoteController.REST_VOLUME_DOWN;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_DOWN, returnValue.getMessage());
    }
}