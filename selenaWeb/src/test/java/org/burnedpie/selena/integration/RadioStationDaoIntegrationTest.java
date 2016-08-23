package org.burnedpie.selena.integration;

import org.burnedpie.selena.persistance.dao.RadioStationDAO;
import org.burnedpie.selena.persistance.domain.RadioStation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.logging.Logger;

/**
 * Created by jibe on 15/08/16.
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = "classpath:spring-context-db-integration.xml")
public class RadioStationDaoIntegrationTest {

    Logger logger = Logger.getLogger(RadioStationDaoIntegrationTest.class.getName());

    private SessionFactory sessionFactory;

    @Autowired
    private RadioStationDAO radioStationDAO;

    @Before
    public void setUp() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            radioStationDAO = new RadioStationDAO();
            radioStationDAO.setSessionFactory(sessionFactory);
            populateDb();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    private void populateDb() {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        RadioStation radioStation = new RadioStation();
        radioStation.setName("Europe1");
        radioStation.setUrl("http://e1-live-mp3-128.scdn.arkena.com/europe1.mp3");
        radioStation.setChannel(1);
        session.save(radioStation);

        t.commit();
        session.close();
    }

    @Test
    public void testFindById() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        RadioStation radioStation = session.get(RadioStation.class, 1);

        Assert.assertNotNull(radioStation);
        Assert.assertEquals("Europe1", radioStation.getName());
        session.close();
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