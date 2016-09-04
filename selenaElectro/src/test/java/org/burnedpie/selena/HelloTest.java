package org.burnedpie.selena;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jibe on 29/08/16.
 */
public class HelloTest {

    Logger logger = LoggerFactory.getLogger(HelloTest.class);

    @Test
    public void testHello() {
        logger.info("Unit test.");
        Assert.assertTrue(true);
    }
}
