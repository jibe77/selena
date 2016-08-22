package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jibe on 18/08/16.
 */
@Repository
public class ConfigurationDAO {
    public String findByKey(ConfigurationKeyEnum key) {
        Session session = sessionFactory.openSession();
        Query confQuery = session.createQuery("FROM Configuration WHERE configKey=:key");
        confQuery.setParameter("key", key);
        List<Configuration> c = confQuery.list();
        session.close();
        if (c != null && !c.isEmpty()) {
            return c.iterator().next().getConfigValue();
        } else {
            return null;
        }
    }

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Configuration saveConfiguration(Configuration configuration) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Configuration conf = (Configuration) session.merge(configuration);
        transaction.commit();
        session.close();
        return conf;
    }
}
