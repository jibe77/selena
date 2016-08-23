package org.burnedpie.selena.web.rest.controller;

import org.burnedpie.selena.web.rest.controller.ReturnValue;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jibe on 13/08/16.
 */
public class RestReturnValueTest {

    @Test
    public void testReturnValueToString() {
        // given that
        String status = "OK";
        String message = "Radio station set to 1.";

        // when
        ReturnValue ReturnValue = new ReturnValue();
        ReturnValue.setStatus(status);
        ReturnValue.setMessage(message);

        // then
        Assert.assertEquals(
                "ReturnValue{status="+ status+", message="+ message+"}",
                ReturnValue.toString());
    }

    @Test
    public void testReturnValueToString2() {
        // given that
        String status = "OK";
        String message = "Radio station set to 1.";

        // when
        ReturnValue ReturnValue = new ReturnValue(status, message);
        // then
        Assert.assertEquals(
                "ReturnValue{status="+ status+", message="+ message+"}",
                ReturnValue.toString());
    }

    @Test
    public void testReturnValueToString3() {
        // given that
        String status = "OK";
        String message = "Radio station set to {0}.";
        String station = "1";

        // when
        ReturnValue ReturnValue = new ReturnValue(status, message, station);
        // then
        Assert.assertEquals(
                "ReturnValue{status="+ status+", message="+ message.replace("{0}", station)+"}",
                ReturnValue.toString());
    }
}
