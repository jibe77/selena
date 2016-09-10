package org.burnedpie.selena.persistance.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jibe on 15/08/16.
 */
@Entity
@Table(name="CONFIGURATION")
public class Configuration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name = "CONFIG_KEY", nullable = false, unique = true)
    private String configKey;

    @Column(name = "CONFIG_VALUE")
    private String configValue;

    public Configuration() {
    }

    public Configuration(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public Configuration(ConfigurationKeyEnum keyEnum, String configValue) {
        setConfigKey(keyEnum);
        this.configValue = configValue;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public void setConfigKey(ConfigurationKeyEnum configKey) {
        if (configKey != null)
            this.configKey = configKey.name();
        else
            this.configKey = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", configKey=" + configKey +
                ", configValue='" + configValue + '\'' +
                '}';
    }
}
