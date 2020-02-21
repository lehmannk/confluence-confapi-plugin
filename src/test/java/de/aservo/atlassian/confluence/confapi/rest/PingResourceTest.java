package de.aservo.atlassian.confluence.confapi.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static de.aservo.atlassian.confluence.confapi.rest.PingResource.PING_PATH;
import static de.aservo.atlassian.confluence.confapi.rest.PingResource.PONG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class PingResourceTest {

    private PingResource pingResource;

    @Before
    public void setup() {
        pingResource = new PingResource();
    }

    @Test
    public void testPingAnnotation() {
        final Class<? extends PingResource> pingResourceClass = pingResource.getClass();

        final Path pingResourceClassAnnotation = pingResourceClass.getAnnotation(Path.class);
        assertNotNull(pingResourceClassAnnotation);
        assertEquals(PING_PATH, pingResourceClassAnnotation.value());
    }

    @Test
    public void testGetPing() {
        final Response pingResponse = pingResource.get();
        assertEquals(PONG, pingResponse.getEntity().toString());
    }

}
