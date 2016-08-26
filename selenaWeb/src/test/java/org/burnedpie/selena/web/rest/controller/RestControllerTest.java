package org.burnedpie.selena.web.rest.controller;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.VolumeService;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.exception.VolumeException;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.dao.RadioStationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.IOException;

/**
 * Created by jibe on 13/08/16.
 *
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class RestControllerTest {

    RemoteController remoteController;
    AirplayService      mockAirplayService;
    RadioService        mockRadioService;
    ConfigurationRepository mockConfigurationRepository;
    RadioStationRepository mockRadioStationDAO;
    VolumeService       mockVolumeService;

    @Before
    public void setup() {
        remoteController = new RemoteController();

        mockAirplayService      = Mockito.mock(AirplayService.class);
        mockRadioService        = Mockito.mock(RadioService.class);
        mockRadioStationDAO     = Mockito.mock(RadioStationRepository.class);
        mockConfigurationRepository = Mockito.mock(ConfigurationRepository.class);
        mockVolumeService       = Mockito.mock(VolumeService.class);

        remoteController.setAirplayService(mockAirplayService);
        remoteController.setRadioService(mockRadioService);
        remoteController.setRadioStationDAO(mockRadioStationDAO);
        remoteController.setVolumeService(mockVolumeService);
    }

    @Test
    public void testRemoteControllerPlayRadio() {
        // given that
        int station = 1;
        RadioStation radioStation = new RadioStation();
        radioStation.setUrl("http://test");
        Mockito.when(mockAirplayService.isAirplayOn()).thenReturn(true);
        Mockito.when(mockRadioService.isRadioOn()).thenReturn(true);
        Mockito.when(mockRadioStationDAO.findByChannel(Mockito.anyInt())).thenReturn(radioStation);

        // when
        ReturnValue returnValue = remoteController.playRadioStation(station);

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.RADIO_STATION_SET.replace("{0}", String.valueOf(station)), returnValue.getMessage());
        // 1. si airplay, couper airplay
        Mockito.verify(mockAirplayService, Mockito.times(1)).isAirplayOn();
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOff();
        // 2. si radio, couper radio
        Mockito.verify(mockRadioService, Mockito.times(1)).isRadioOn();
        Mockito.verify(mockRadioService, Mockito.times(1)).stopRadio();
        // 3. lancer radio
        Mockito.verify(mockRadioService, Mockito.times(1)).playRadioChannel(radioStation);
    }

    @Test
    public void testRemoteControllerPlayRadioFail() {
        // given that
        int station = 1;
        RadioStation radioStation = new RadioStation();
        radioStation.setUrl("http://test");
        Mockito.when(mockAirplayService.isAirplayOn()).thenReturn(true);
        Mockito.when(mockRadioService.isRadioOn()).thenReturn(true);
        Mockito.when(mockRadioStationDAO.findByChannel(Mockito.anyInt())).thenReturn(radioStation);
        Mockito.when(mockConfigurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME)).thenReturn(
                new Configuration(ConfigurationKeyEnum.AIRPLAY_NAME, "[selena]test"));
        Mockito.doThrow(new RadioException(new Exception())).when(mockRadioService).playRadioChannel(radioStation);

        // when
        ReturnValue returnValue = remoteController.playRadioStation(station);

        // then
        Assert.assertEquals(RemoteController.FAIL, returnValue.getStatus());
        Assert.assertEquals(RemoteController.RADIO_STATION_SET_FAIL.replace("{0}", String.valueOf(station)), returnValue.getMessage());
        // 1. si airplay, couper airplay
        Mockito.verify(mockAirplayService, Mockito.times(1)).isAirplayOn();
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOff();
        // 2. si radio, couper radio
        Mockito.verify(mockRadioService, Mockito.times(1)).isRadioOn();
        Mockito.verify(mockRadioService, Mockito.times(1)).stopRadio();
        // 3. lancer radio
        Mockito.verify(mockRadioService, Mockito.times(1)).playRadioChannel(radioStation);
        // 4. si échoue, lancer airplay
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOn();
    }

    @Test
    public void testRemoteControllerPlayRadio10Fail() {
        // given that
        int channel = 10;
        RadioStation radioStation = new RadioStation();
        radioStation.setUrl("http://test");
        Mockito.when(mockAirplayService.isAirplayOn()).thenReturn(true);
        Mockito.when(mockRadioService.isRadioOn()).thenReturn(true);
        Mockito.when(mockRadioStationDAO.findByChannel(Mockito.anyInt())).thenReturn(null);
        Mockito.when(mockConfigurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME)).thenReturn(
                new Configuration(ConfigurationKeyEnum.AIRPLAY_NAME, "[selena]test"));
        Mockito.doThrow(new RadioException(new Exception())).when(mockRadioService).playRadioChannel(radioStation);

        // when
        ReturnValue returnValue = remoteController.playRadioStation(channel);

        // then
        Assert.assertEquals(RemoteController.FAIL, returnValue.getStatus());
        Assert.assertEquals(RemoteController.RADIO_STATION_UNDEFINED_FAIL.replace("{0}", String.valueOf(channel)), returnValue.getMessage());
        // 1. si airplay, couper airplay
        Mockito.verify(mockAirplayService, Mockito.times(1)).isAirplayOn();
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOff();
        // 2. si radio, couper radio
        Mockito.verify(mockRadioService, Mockito.times(1)).isRadioOn();
        Mockito.verify(mockRadioService, Mockito.times(1)).stopRadio();
        // 3. lancer radio
        Mockito.verify(mockRadioService, Mockito.times(0)).playRadioChannel(radioStation);
        // 4. si échoue, lancer airplay
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOn();
    }


    @Test
    public void testRemoteControllerStop() {
        // given that
        Mockito.when(mockAirplayService.isAirplayOn()).thenReturn(true);
        Mockito.when(mockRadioService.isRadioOn()).thenReturn(true);

        // when
        ReturnValue returnValue = remoteController.stop();

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.PLAYER_STOPPED, returnValue.getMessage());
        // 1. si airplay, couper airplay
        Mockito.verify(mockAirplayService, Mockito.times(1)).isAirplayOn();
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOff();
        // 2. si radio, couper radio
        Mockito.verify(mockRadioService, Mockito.times(1)).isRadioOn();
        Mockito.verify(mockRadioService, Mockito.times(1)).stopRadio();
    }

    @Test
    public void testRemoteControllerVoumeUp() {
        // given that
        Mockito.doNothing().when(mockVolumeService).volumeUp();

        // when
        ReturnValue returnValue = remoteController.volumUp();

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_UP, returnValue.getMessage());
        Mockito.verify(mockVolumeService, Mockito.times(1)).volumeUp();
    }

    @Test
    public void testRemoteControllerVoumeUpFail() {
        // given that
        Mockito.doThrow(new VolumeException(new IOException())).when(mockVolumeService).volumeUp();

        // when
        ReturnValue returnValue = remoteController.volumUp();

        // then
        Assert.assertEquals(RemoteController.FAIL, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_UP_FAIL, returnValue.getMessage());
        Mockito.verify(mockVolumeService, Mockito.times(1)).volumeUp();
    }


    @Test
    public void testRemoteControllerVoumeDown() {
        // given that
        Mockito.doNothing().when(mockVolumeService).volumeDown();

        // when
        ReturnValue returnValue = remoteController.volumeDown();

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_DOWN, returnValue.getMessage());
        Mockito.verify(mockVolumeService, Mockito.times(1)).volumeDown();
    }

    @Test
    public void testRemoteControllerVoumeDownFail() {
        // given that
        Mockito.doThrow(new VolumeException(new IOException())).when(mockVolumeService).volumeDown();

        // when
        ReturnValue returnValue = remoteController.volumeDown();

        // then
        Assert.assertEquals(RemoteController.FAIL, returnValue.getStatus());
        Assert.assertEquals(RemoteController.VOLUME_TURNED_DOWN_FAIL, returnValue.getMessage());
        Mockito.verify(mockVolumeService, Mockito.times(1)).volumeDown();
    }

    @Test
    public void testRemoteControllerStartAirplay() {
        // given that
        Mockito.when(mockRadioService.isRadioOn()).thenReturn(false);
        Mockito.when(mockAirplayService.isAirplayOn()).thenReturn(false);

        // when
        ReturnValue returnValue = remoteController.startAirplay();

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_STARTED, returnValue.getMessage());
        Mockito.verify(mockRadioService, Mockito.times(1)).isRadioOn();
        Mockito.verify(mockAirplayService, Mockito.times(1)).isAirplayOn();
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOn();
    }

    @Test
    public void testRemoteControllerStartAirplayAlreadyOn() {
        // given that
        Mockito.when(mockRadioService.isRadioOn()).thenReturn(false);
        Mockito.when(mockAirplayService.isAirplayOn()).thenReturn(true);

        // when
        ReturnValue returnValue = remoteController.startAirplay();

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_ALREADY_STARTED, returnValue.getMessage());
        Mockito.verify(mockRadioService, Mockito.times(1)).isRadioOn();
        Mockito.verify(mockRadioService, Mockito.times(0)).stopRadio();
        Mockito.verify(mockAirplayService, Mockito.times(1)).isAirplayOn();
        Mockito.verify(mockAirplayService, Mockito.times(0)).turnAirplayOn();
    }


    @Test
    public void testRemoteControllerStartAirplayAlreadyPlayingRadio() {
        // given that
        Mockito.when(mockRadioService.isRadioOn()).thenReturn(true);
        Mockito.when(mockAirplayService.isAirplayOn()).thenReturn(false);

        // when
        ReturnValue returnValue = remoteController.startAirplay();

        // then
        Assert.assertEquals(RemoteController.SUCCESS, returnValue.getStatus());
        Assert.assertEquals(RemoteController.AIRPLAY_STARTED, returnValue.getMessage());
        Mockito.verify(mockRadioService, Mockito.times(1)).isRadioOn();
        Mockito.verify(mockRadioService, Mockito.times(1)).stopRadio();

        Mockito.verify(mockAirplayService, Mockito.times(1)).isAirplayOn();
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOn();
    }
}
