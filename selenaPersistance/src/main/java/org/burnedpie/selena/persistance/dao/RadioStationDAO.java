package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.RadioStation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by jibe on 18/08/16.
 */
@Repository
public class RadioStationDAO {

    public RadioStation findByChannel(int i) {
        Session session = sessionFactory.openSession();
        Query<RadioStation> radioStationQuery = session.createQuery("FROM RadioStation WHERE channel=?1");
        radioStationQuery.setParameter(1, i);
        return radioStationQuery.getSingleResult();
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
