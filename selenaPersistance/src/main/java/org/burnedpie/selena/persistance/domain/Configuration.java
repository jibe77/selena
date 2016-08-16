package org.burnedpie.selena.persistance.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jibe on 15/08/16.
 */
@Entity
@Table(name="CONFIGURATION")
public class Configuration {

    @Id
    @Column(name = "CONFIG_KEY", nullable = false)
    private String configKey;

    @Column(name = "CONFIG_VALUE")
    private String configValue;

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
}
