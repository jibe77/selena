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
        RestReturnValue restReturnValue = new RestReturnValue();
        restReturnValue.setStatus(status);
        restReturnValue.setMessage(message);

        // then
        Assert.assertEquals(
                "RestReturnValue{status="+ status+", message="+ message+"}",
                restReturnValue.toString());
    }
}
