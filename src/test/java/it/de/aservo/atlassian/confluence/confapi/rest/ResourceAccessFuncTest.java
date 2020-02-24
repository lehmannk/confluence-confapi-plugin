package it.de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.atlassian.user.EntityException;
import com.atlassian.user.User;
import com.atlassian.user.UserManager;
import com.atlassian.user.security.password.Credential;
import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.BasicAuthSecurityHandler;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import static de.aservo.atlassian.confluence.confapi.rest.SettingsResource.SETTINGS_PATH;
import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class ResourceAccessFuncTest {

    @ComponentImport
    private final UserManager userManager;

    @Inject
    public ResourceAccessFuncTest(
            final UserManager userManager) {

        this.userManager = userManager;
    }

    @Test
    public void testAccessForbiddenAsAnonymousUser() throws EntityException {
        final String baseUrl = System.getProperty("baseurl");
        final String resourceUrl = baseUrl + "/rest/confapi/1/" + SETTINGS_PATH;

        final RestClient client = new RestClient();
        final Resource resource = client.resource(resourceUrl);

        assertEquals(Status.UNAUTHORIZED.getStatusCode(), resource.get().getStatusCode());
    }

    @Test
    public void testAccessForbiddenAsNormalUser() throws EntityException {
        User user = userManager.getUser("test");

        if (user == null) {
            user = userManager.createUser(
                    new ConfluenceUserImpl("test", "test", "test@localhost"),
                    Credential.unencrypted("test"));
        }

        final String baseUrl = System.getProperty("baseurl");
        final String resourceUrl = baseUrl + "/rest/confapi/1/" + SETTINGS_PATH;

        final BasicAuthSecurityHandler basicAuthHandler = new BasicAuthSecurityHandler();
        basicAuthHandler.setUserName(user.getName());
        basicAuthHandler.setPassword(user.getName());

        final ClientConfig config = new ClientConfig();
        config.handlers(basicAuthHandler);

        final RestClient client = new RestClient(config);
        final Resource resource = client.resource(resourceUrl);

        assertEquals(Status.FORBIDDEN.getStatusCode(), resource.get().getStatusCode());

        userManager.removeUser(user);
    }

    @Test
    public void testAccessSuccessfulAsSysAdmin() throws EntityException {
        final String baseUrl = System.getProperty("baseurl");
        final String resourceUrl = baseUrl + "/rest/confapi/1/" + SETTINGS_PATH;

        final BasicAuthSecurityHandler basicAuthHandler = new BasicAuthSecurityHandler();
        basicAuthHandler.setUserName("admin");
        basicAuthHandler.setPassword("admin");

        final ClientConfig config = new ClientConfig();
        config.handlers(basicAuthHandler);

        final RestClient client = new RestClient(config);
        final Resource resource = client.resource(resourceUrl);

        assertEquals(Status.OK.getStatusCode(), resource.get().getStatusCode());
    }

}
