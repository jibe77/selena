package org.burnedpie.selena.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by jibe on 11/09/16.
 */
@Component
public class SelenaAirplayInfoContributor implements InfoContributor {

    @Autowired
    AirplayService airplayService;

    @Autowired
    ConfigurationRepository configurationRepository;

    private static final String SERVICE_NAME = "airplay";
    private static final String STATUS = "status";
    private static final String ACTUAL_NAME = "actualName";
    private static final String CONFIGURED_NAME = "configuredName";
    private static final String ON = "on";
    private static final String OFF = "off";


    @Override
    public void contribute(Info.Builder builder) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (airplayService.isAirplayOn()) {
            hashMap.put(STATUS, ON);
        } else {
            hashMap.put(STATUS, OFF);
        }
        hashMap.put(ACTUAL_NAME, airplayService.getName());


        Configuration configuration = configurationRepository.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME.name());
        if (configuration != null && StringUtils.isNotEmpty(configuration.getConfigValue())) {
            hashMap.put(CONFIGURED_NAME, configuration.getConfigValue());
        } else {
            hashMap.put(CONFIGURED_NAME, "");
        }

        builder.withDetail(SERVICE_NAME, hashMap);
    }
}
