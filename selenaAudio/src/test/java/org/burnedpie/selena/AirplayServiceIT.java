package org.burnedpie.selena;

import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.audio.impl.RadioServiceImpl;
import org.burnedpie.selena.audio.impl.ShairportDummyImpl;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jibe on 25/07/16.
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@SpringBootTest(classes = {
        ShairportDummyImpl.class,
        RadioServiceImpl.class,
        ConfigurationRepository.class,
        NativeCommand.class})
@EnableAutoConfiguration
@DataJpaTest
public class AirplayServiceIT {

    private static final Logger s_logger = LoggerFactory.getLogger(AirplayServiceIT.class);

    @Autowired
    AirplayService airplayService;

    @Autowired
    ConfigurationRepository configurationRepository;

    @Before
    public void setUp() {
        configurationRepository.save(new Configuration(ConfigurationKeyEnum.AIRPLAY_NAME, "selena"));
    }

    @After
    public void tearDown() {
        airplayService.turnAirplayOff();
    }

    @Test
    public void testAirplay() throws Exception {
        // given that

        // when
        airplayService.turnAirplayOn();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }
        // then
        Assert.assertTrue(airplayService.isAirplayOn());
    }

}