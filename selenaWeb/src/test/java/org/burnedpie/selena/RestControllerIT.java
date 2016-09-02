package org.burnedpie.selena;

import org.burnedpie.selena.audio.impl.RadioServiceImpl;
import org.burnedpie.selena.audio.impl.ShairportDummyImpl;
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
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
        ShairportDummyImpl.class,
        RadioServiceImpl.class,
        ConfigurationRepository.class,
        RadioStationRepository.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

        if (radioStationDAO.findByChannel(2) == null) {
            RadioStation radioStation = new RadioStation();
            radioStation.setName("Sample");
            radioStation.setUrl("http://www.wavsource.com/snds_2016-08-21_1204101428963685/animals/bird.wav");
            radioStation.setChannel(2);
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

        // TOOD : channel 2 on wav file

        Assert.assertNotNull(configurationDAO.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME));
        Assert.assertNotNull(configurationDAO.findByConfigKey(ConfigurationKeyEnum.VOLUME_UP_COMMAND));
        Assert.assertNotNull(configurationDAO.findByConfigKey(ConfigurationKeyEnum.VOLUME_DOWN_COMMAND));
    }

    @After
    public void tearDown() {
        String url = base + RemoteController.REST_STOP;
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);
    }

    @Test
    public void testRemoteController_00_AirplayStarted() {
        // given that
        String url = base + RemoteController.REST_IS_AIRPLAY_ON;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.TRUE, returnValue.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_IS_STARTED, returnValue.getMessage());
    }


    @Test
    public void testRemoteController_01_StartAirplay() {
        // given that
        String url = base + RemoteController.REST_START_AIRPLAY;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_ALREADY_STARTED, returnValue.getMessage());
    }

    @Test
    public void testRemoteController_02_PlayRadio() {
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
    public void testRemoteController_03_PlayRadio10() {
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
    public void testRemoteController_04_StartAirplayAlreadyStartedButShutdownByTearDownMethod() {
        // given that
        String url = base + RemoteController.REST_START_AIRPLAY;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_ALREADY_STARTED, returnValue.getMessage());
    }

    @Test
    public void testRemoteController_05_Stop() {
        // given that
        String url = base + RemoteController.REST_STOP;
        String urlIsAirplayOn = base + RemoteController.REST_IS_AIRPLAY_ON;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);
        ReturnValue returnValueIsAirplayOn = template.getForObject(urlIsAirplayOn, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.PLAYER_STOPPED, returnValue.getMessage());
        Assert.assertEquals(RemoteController.TRUE, returnValueIsAirplayOn.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_IS_STARTED, returnValueIsAirplayOn.getMessage());
    }

    @Test
    public void testRemoteController_06_VolumeUp() {
        // given that
        String url = base + RemoteController.REST_VOLUME_UP;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_UP, returnValue.getMessage());
    }

    @Test
    public void testRemoteController_07_VolumeDown() {
        // given that
        String url = base + RemoteController.REST_VOLUME_DOWN;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_DOWN, returnValue.getMessage());
    }

    @Test
    public void testRemoteController_08_StopAirPlay() {
        // given that
        String url = base + RemoteController.REST_STOP;

        // when
        ReturnValue returnValue = template.getForObject(url, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.PLAYER_STOPPED, returnValue.getMessage());
    }

    @Test
    public void testRemoteController_09_StartAirplayAlreadyStartedButShutdownByTearDownMethod() {
        // given that
        String urlStart = base + RemoteController.REST_START_AIRPLAY;
        String urlStop  = base + RemoteController.REST_STOP;

        // when
        ReturnValue returnValueStart = template.getForObject(urlStart, ReturnValue.class);
        ReturnValue returnValueStop  = template.getForObject(urlStop, ReturnValue.class);
        ReturnValue returnValueAlreadyStop  = template.getForObject(urlStop, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValueStart.getStatus());
        Assert.assertEquals(RemoteController.SUCCESS, returnValueStop.getStatus());
        Assert.assertEquals(RemoteController.SUCCESS, returnValueAlreadyStop.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_ALREADY_STARTED, returnValueStart.getMessage());
        Assert.assertEquals(RemoteController.PLAYER_STOPPED, returnValueStop.getMessage());
        Assert.assertEquals(RemoteController.PLAYER_STOPPED, returnValueAlreadyStop.getMessage());
    }

    @Test
    public void testRemoteController_10_StartRadioAndAirplayShouldBeOnAtTheEnd() throws InterruptedException {
        // given that
        int channel = 2;
        String urlStart = base + RemoteController.REST_PLAY_RADIO_STATION + "?radioStation=" + channel;;
        String urlAirplay  = base + RemoteController.REST_IS_AIRPLAY_ON;

        // when
        ReturnValue returnValueStart = template.getForObject(urlStart, ReturnValue.class);
        Thread.sleep(5000);
        ReturnValue returnValueAirplay  = template.getForObject(urlAirplay, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValueStart.getStatus());
        Assert.assertEquals(RemoteController.TRUE, returnValueAirplay.getStatus());
        Assert.assertEquals(
                RemoteController.RADIO_STATION_SET.replace("{0}", String.valueOf(channel)),
                returnValueStart.getMessage());
        Assert.assertEquals(RemoteController.AIRPLAY_IS_STARTED, returnValueAirplay.getMessage());
    }

    @Test
    public void testRemoteController_10_StartRadioThenStopAndAirplayShouldBeOnAtTheEnd() throws InterruptedException {
        // given that
        int channel = 1;
        String urlStart = base + RemoteController.REST_PLAY_RADIO_STATION + "?radioStation=" + channel;;
        String urlStop  = base + RemoteController.REST_STOP;
        String urlAirplay  = base + RemoteController.REST_IS_AIRPLAY_ON;

        // when
        ReturnValue returnValueStart = template.getForObject(urlStart, ReturnValue.class);
        Thread.sleep(5000);
        ReturnValue returnValueStop  = template.getForObject(urlStop, ReturnValue.class);
        ReturnValue returnValueAirplay  = template.getForObject(urlAirplay, ReturnValue.class);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValueStart.getStatus());
        Assert.assertEquals(RemoteController.SUCCESS, returnValueStop.getStatus());
        Assert.assertEquals(RemoteController.TRUE, returnValueAirplay.getStatus());
        Assert.assertEquals(
                RemoteController.RADIO_STATION_SET.replace("{0}", String.valueOf(channel)),
                returnValueStart.getMessage());
        Assert.assertEquals(RemoteController.PLAYER_STOPPED, returnValueStop.getMessage());
        Assert.assertEquals(RemoteController.AIRPLAY_IS_STARTED, returnValueAirplay.getMessage());
    }

}