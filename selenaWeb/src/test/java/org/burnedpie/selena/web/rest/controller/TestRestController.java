package org.burnedpie.selena.web.rest.controller;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.persistance.PersistanceService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    RemoteController remoteController;
    AirplayService mockAirplayService;
    RadioService mockRadioService;
    PersistanceService mockPersistanceService;

    @Before
    public void setup() {
        remoteController = new RemoteController();

        mockAirplayService = Mockito.mock(AirplayService.class);
        mockRadioService = Mockito.mock(RadioService.class);
        mockPersistanceService = Mockito.mock(PersistanceService.class);

        remoteController.setAirplayService(mockAirplayService);
        remoteController.setRadioService(mockRadioService);
        remoteController.setPersistanceService(mockPersistanceService);
    }

    @Test
    public void testRemoteControllerPlayRadio() {
        // given that
        int station = 1;
        Mockito.when(mockAirplayService.isAirplayOn()).thenReturn(true);
        Mockito.when(mockRadioService.isRadioOn()).thenReturn(true);
        Mockito.when(mockPersistanceService.findRadioStationUrlByIndex(Mockito.anyInt())).thenReturn("http://test");
        Mockito.when(mockPersistanceService.findAirplayServiceName()).thenReturn("[selena]test");
        Mockito.doThrow(new RadioException(new Exception())).when(mockRadioService).playRadioChannel(Mockito.anyString());

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
        Mockito.verify(mockRadioService, Mockito.times(1)).playRadioChannel(Mockito.anyString());
        // 4. si échoue, lancer airplay
        Mockito.verify(mockPersistanceService, Mockito.times(1)).findRadioStationUrlByIndex(Mockito.anyInt());
        Mockito.verify(mockPersistanceService, Mockito.times(1)).findAirplayServiceName();
        Mockito.verify(mockAirplayService, Mockito.times(1)).turnAirplayOn(Mockito.anyString());
    }

}
