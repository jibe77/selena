package org.burnedpie.selena;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jibe on 29/08/16.
 */
public class HelloIT {

    Logger logger = LoggerFactory.getLogger(HelloTest.class);

    @Test
    public void test() {
        logger.info("Integration test");
        org.junit.Assert.assertTrue(true);
    }
}
