package org.burnedpie.selena.persistance;

import org.burnedpie.selena.persistance.dao.RadioStationRepository;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Created by jibe on 15/08/16.
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@EnableAutoConfiguration
public class RadioStationDaoIntegrationTest {

    Logger logger = LoggerFactory.getLogger(RadioStationDaoIntegrationTest.class);

    @Autowired
    private RadioStationRepository radioStationDAO;

    static boolean initDb = false;

    @Before
    public void setUp() {
        populateDb();
    }

    private void populateDb() {
        if (!initDb) {
            RadioStation radioStation = new RadioStation();
            radioStation.setName("Europe1");
            radioStation.setUrl("http://e1-live-mp3-128.scdn.arkena.com/europe1.mp3");
            radioStation.setChannel(1);
            radioStationDAO.save(radioStation);
            initDb = true;
        }
    }

    @Test
    public void testFindById() {
        int channel = 1;

        RadioStation radioStation = radioStationDAO.findByChannel(channel);

        Assert.assertNotNull(radioStation);
        Assert.assertEquals("Europe1", radioStation.getName());
    }


    @Test
    public void testFindByChannel() {
        int channel = 1;
        String name = "Europe1";

        RadioStation radioStation = radioStationDAO.findByChannel(channel);

        Assert.assertNotNull(radioStation);
        Assert.assertEquals(name, radioStation.getName());
    }
}