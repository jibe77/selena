package org.burnedpie.selena.web.rest.controller;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jibe on 13/08/16.
 */
public class TestRestReturnValue {

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
}
