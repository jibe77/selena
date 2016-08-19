package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by jibe on 18/08/16.
 */
@Repository
public class ConfigurationDAO {
    public String findByKey(ConfigurationKeyEnum key) {
        Session session = sessionFactory.openSession();
        Query<Configuration> confQuery = session.createQuery("FROM Configuration WHERE configKey=?1");
        confQuery.setParameter(1, key.name());
        Configuration c = confQuery.getSingleResult();
        if (c != null) {
            return c.getConfigValue();
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

}
