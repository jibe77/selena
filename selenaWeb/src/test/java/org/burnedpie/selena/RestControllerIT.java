package org.burnedpie.selena;

import org.burnedpie.selena.audio.impl.RadioServiceImpl;
import org.burnedpie.selena.audio.impl.ShairportSyncImpl;
import org.burnedpie.selena.audio.impl.VolumeServiceImpl;
import org.burnedpie.selena.audio.util.impl.NativeCommandImpl;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.dao.RadioStationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.burnedpie.selena.web.rest.controller.RemoteController;
import org.burnedpie.selena.web.rest.controller.ReturnValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestExecutionListeners;
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
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@SpringBootTest(classes = {
        RemoteController.class,
        VolumeServiceImpl.class,
        NativeCommandImpl.class,
        ShairportSyncImpl.class,
        RadioServiceImpl.class,
        ConfigurationRepository.class,
        RadioStationRepository.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class RestControllerIT {

    Logger logger = Logger.getLogger(RestControllerIT.class.getName());

    @LocalServerPort
    private int port;

    private URL base;
    private TestRestTemplate template;

    @Autowired
    RadioStationRepository radioStationDAO;

    @Autowired
    ConfigurationRepository configurationDAO;

    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        template = new TestRestTemplate();

        if (radioStationDAO.findByChannel(1) == null) {
            RadioStation radioStation = new RadioStation();
            radioStation.setName("Europe1");
            radioStation.setUrl("http://e1-live-mp3-128.scdn.arkena.com/europe1.mp3");
            radioStation.setChannel(1);
            radioStationDAO.save(radioStation);
        }

        if (configurationDAO.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME) == null) {
            Configuration configuration = new Configuration();
            configuration.setConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME);
            configuration.setConfigValue("[selena]integration-test");
            configurationDAO.save(configuration);
        }

        if (configurationDAO.findByConfigKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND) == null) {
            Configuration configuration = new Configuration();
            configuration.setConfigKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND);
            configuration.setConfigValue("echo volume_up");
            configurationDAO.save(configuration);
        }

        if (configurationDAO.findByConfigKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND) == null) {
            Configuration configuration = new Configuration();
            configuration.setConfigKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND);
            configuration.setConfigValue("echo volume_down");
            configurationDAO.save(configuration);
        }

        Assert.assertNotNull(configurationDAO.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME));
        Assert.assertNotNull(configurationDAO.findByConfigKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND));
        Assert.assertNotNull(configurationDAO.findByConfigKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND));
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