package org.burnedpie.selena.web.rest.controller;

import org.apache.commons.exec.util.StringUtils;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by jibe on 08/09/16.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private ConfigurationRepository configurationRepository;

    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        if (userName.equals("admin")) {
            Configuration configuration =
                    configurationRepository.findByConfigKey(ConfigurationKeyEnum.ADMIN_PASSWORD.name());
            String pwd = null;
            if (configuration == null ||
                    org.apache.commons.lang3.StringUtils.isEmpty(configuration.getConfigValue())) {
                pwd = "password";
                configurationRepository.save(new Configuration(ConfigurationKeyEnum.ADMIN_PASSWORD, pwd));
            } else {
                pwd = configuration.getConfigValue();
            }
            return new SecurityUser(new User(pwd));
        } else {
            throw new UsernameNotFoundException("User unknown.");
        }
    }
}