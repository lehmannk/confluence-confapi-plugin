package it.de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response.Status;

import static de.aservo.atlassian.confluence.confapi.rest.PingResource.PING_PATH;
import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class PingResourceFuncTest {

    @Test
    public void testPing() {
        final String baseUrl = System.getProperty("baseurl");
        final String resourceUrl = baseUrl + "/rest/confapi/1/" + PING_PATH;

        final RestClient client = new RestClient();
        final Resource resource = client.resource(resourceUrl);

        assertEquals(Status.OK.getStatusCode(), resource.get().getStatusCode());
    }

}
