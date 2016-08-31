package org.burnedpie.selena;

import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.impl.RadioServiceImpl;
import org.burnedpie.selena.audio.util.impl.NativeCommandImpl;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.logging.Logger;

/**
 * Created by jibe on 25/07/16.
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {
        RadioServiceImpl.class,
        NativeCommandImpl.class})
@DataJpaTest
public class RadioIntegrationIT {

    private static final Logger s_logger = Logger.getLogger(RadioIntegrationIT.class.getName());

    @Autowired
    RadioService radioService;

    @After
    public void tearDown() {
        radioService.stopRadio();
    }

    @Test
    public void testRadio() throws Exception {
        // given that
        RadioStation radioStation = new RadioStation();
        radioStation.setUrl("http://www.listenlive.eu/europe1.m3u");

        // when
        radioService.playRadioChannel(radioStation);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }
        // then
        Assert.assertTrue(radioService.isRadioOn());
    }

    @Test
    public void testRadioStop() throws Exception {
        // given that
        RadioStation radioStation = new RadioStation();
        radioStation.setUrl("http://www.listenlive.eu/europe1.m3u");
        // when
        radioService.playRadioChannel(radioStation);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }
        radioService.stopRadio();

        // then
        Assert.assertFalse(radioService.isRadioOn());
    }
}