package org.burnedpie.selena.persistance.test.integration;

import org.burnedpie.selena.persistance.dao.ConfigurationDAO;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
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
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.logging.Logger;

/**
 * Created by jibe on 15/08/16.
 */
@Category(IntegrationTest.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = "classpath:org/burnedpie/selena/persistance/test/integration/spring-context-db-integration.xml")
public class ConfigurationDaoIntegrationTest {

    Logger logger = Logger.getLogger(ConfigurationDaoIntegrationTest.class.getName());

    private SessionFactory sessionFactory;

    @Autowired
    private ConfigurationDAO configurationDAO;

    @Before
    public void setUp() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            configurationDAO = new ConfigurationDAO();
            configurationDAO.setSessionFactory(sessionFactory);
            populateDb();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    private void populateDb() {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        Configuration configuration = new Configuration();
        configuration.setConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME.name());
        configuration.setConfigValue("[selena]test");
        session.save(configuration);

        t.commit();
        session.close();
    }

    @Test
    public void testFindByKey() {
        String expected = "[selena]test";

        String actual =
                configurationDAO.findByKey(ConfigurationKeyEnum.AIRPLAY_NAME);

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }
}