package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jibe on 18/08/16.
 */
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {

    Configuration findByConfigKey(@Param("configKey") String configKey);

}
