package org.burnedpie.selena.persistance;

import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Created by jibe on 15/08/16.
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@EnableAutoConfiguration
public class ConfigurationRepositoryIT {

    Logger logger = LoggerFactory.getLogger(ConfigurationRepositoryIT.class);

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Before
    public void setUp() {
        populateDb();
    }

    private void populateDb() {
        Configuration configuration = new Configuration();
        configuration.setConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME);
        configuration.setConfigValue("[selena]test");
        configurationRepository.save(configuration);
    }

    @Test
    public void testFindByKey() {
        String expected = "[selena]test";
        String actual =
                configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME).getConfigValue();
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }
}