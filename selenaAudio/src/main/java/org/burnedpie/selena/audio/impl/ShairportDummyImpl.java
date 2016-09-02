package org.burnedpie.selena.audio.impl;

import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.burnedpie.selena.audio.AirplayService;
import org.burnedpie.selena.audio.exception.AirplayException;
import org.burnedpie.selena.audio.exception.RadioException;
import org.burnedpie.selena.audio.util.NativeCommand;
import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jibe on 11/08/16.
 */
@Component
@Scope("singleton")
public class ShairportDummyImpl extends ShairportSyncImpl implements AirplayService {

    private Logger logger = LoggerFactory.getLogger(ShairportDummyImpl.class);

    public ShairportDummyImpl() {
        this.command = "shairport";
    }

    protected synchronized Executor startCommand(String serviceName) throws IOException {
        return nativeCommand.launchNativeCommandAndReturnExecutor(command, "-a", serviceName, "--", "-c", "\"PCM\"");
    }

}
