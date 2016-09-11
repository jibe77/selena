package org.burnedpie.selena.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.RadioService;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by jibe on 11/09/16.
 */
@Component
public class SelenaRadioInfoContributor implements InfoContributor {

    @Autowired
    RadioService radioService;

    private static final String SERVICE_NAME = "radio";
    private static final String STATUS = "status";
    private static final String CHANNEL = "channel";
    private static final String STATION = "station";
    private static final String ON = "on";
    private static final String OFF = "off";

    @Override
    public void contribute(Info.Builder builder) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (radioService.isRadioOn()) {
            hashMap.put(STATUS, ON);
        } else {
            hashMap.put(STATUS, OFF);
        }
        hashMap.put(CHANNEL, radioService.getChannel());
        hashMap.put(STATION, radioService.getStation());

        builder.withDetail(SERVICE_NAME, hashMap);
    }
}
