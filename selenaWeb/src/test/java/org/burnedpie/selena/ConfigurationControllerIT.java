package org.burnedpie.selena;

import org.burnedpie.selena.audio.impl.RadioServiceImpl;
import org.burnedpie.selena.audio.impl.ShairportDummyImpl;
import org.burnedpie.selena.audio.impl.VolumeServiceImpl;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.dao.RadioStationRepository;
import org.burnedpie.selena.persistance.domain.Configuration;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.burnedpie.selena.web.rest.WebSecurityConfig;
import org.burnedpie.selena.web.rest.controller.CustomUserDetailsService;
import org.burnedpie.selena.web.rest.controller.RemoteController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jibe on 13/08/16.
 *
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@SpringBootTest(classes = {
        RemoteController.class,
        VolumeServiceImpl.class,
        NativeCommand.class,
        ShairportDummyImpl.class,
        RadioServiceImpl.class,
        ConfigurationRepository.class,
        RadioStationRepository.class,
        WebSecurityConfig.class,
        CustomUserDetailsService.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("macos")
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ConfigurationControllerIT {

    Logger logger = LoggerFactory.getLogger(ConfigurationControllerIT.class.getName());

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    RadioStationRepository radioStationDAO;

    @Autowired
    ConfigurationRepository configurationDAO;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    WebSecurityConfig webSecurityConfig;

    private String airplayName = "[selena]integration-test";

    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        if (configurationDAO.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME.name()) == null) {
            Configuration configuration = new Configuration();
            configuration.setConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME);
            configuration.setConfigValue(airplayName);
            configurationDAO.save(configuration);
        }
        if (configurationDAO.findByConfigKey(ConfigurationKeyEnum.ADMIN_PASSWORD.name()) == null) {
            Configuration configuration = new Configuration();
            configuration.setConfigKey(ConfigurationKeyEnum.ADMIN_PASSWORD);
            configuration.setConfigValue("password");
            configurationDAO.save(configuration);
        }

        Assert.assertNotNull(configurationDAO.findByConfigKey(ConfigurationKeyEnum.AIRPLAY_NAME.name()));

        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    /**
     * Testing
     * http://localhost:8080/configurations/search/findByConfigKey?configKey=AIRPLAY_NAME
     * */
    @Test
    public void testConfigurationController_01_FindAirplayNameWithPassword() throws Exception {
        this.mvc.perform(
                get("/configurations/search/findByConfigKey?configKey=AIRPLAY_NAME"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("selena]integration-test")));
    }

}