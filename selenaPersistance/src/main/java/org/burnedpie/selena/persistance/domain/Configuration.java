package org.burnedpie.selena.persistance.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by jibe on 15/08/16.
 */
@Entity
@Table(name="CONFIGURATION")
public class Configuration {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;

    @Column(name = "CONFIG_KEY", nullable = false, unique = true)
    private ConfigurationKeyEnum configKey;

    @Column(name = "CONFIG_VALUE")
    private String configValue;

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public ConfigurationKeyEnum getConfigKey() {
        return configKey;
    }

    public void setConfigKey(ConfigurationKeyEnum configKey) {
        this.configKey = configKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
