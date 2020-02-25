package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static de.aservo.atlassian.confluence.confapi.rest.PingResource.PING_PATH;

@Path(PING_PATH)
@AnonymousAllowed
@Produces({MediaType.APPLICATION_JSON})
public class PingResource {

    public static final String PING_PATH = "ping";

    public static final String PONG = "pong";

    /**
     * Simple ping method for probing the REST api. Returns 'pong' upon success
     *
     * @return response
     */
    @GET
    public Response get() {
        return Response.ok(PONG).build();
    }

}
