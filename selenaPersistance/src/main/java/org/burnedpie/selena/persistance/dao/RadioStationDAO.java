package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.RadioStation;
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
public class RadioStationDAO {

    public RadioStation findByChannel(int i) {
        Session session = sessionFactory.openSession();
        Query radioStationQuery = session.createQuery("FROM RadioStation WHERE channel=:channel");
        radioStationQuery.setParameter("channel", i);
        List<RadioStation> c = radioStationQuery.list();
        session.close();
        if (c != null && !c.isEmpty()) {
            return c.iterator().next();
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

    public RadioStation saveRadioStation(RadioStation radioStation) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        RadioStation persistedRadioStation = (RadioStation) session.merge(radioStation);
        transaction.commit();
        session.close();
        return persistedRadioStation;
    }
}
