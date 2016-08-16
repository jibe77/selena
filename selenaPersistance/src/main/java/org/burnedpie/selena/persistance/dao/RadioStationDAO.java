package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.RadioStation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jibe on 15/08/16.
 */
public class RadioStationDAO {

    @Autowired
    SessionFactory sessionFactory;

    public RadioStation findByChannel(int i) {
        Session session = sessionFactory.openSession();
        Query<RadioStation> radioStationQuery = session.createQuery("FROM RadioStation WHERE channel=?1");
        radioStationQuery.setParameter(1, i);
        return radioStationQuery.getSingleResult();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
