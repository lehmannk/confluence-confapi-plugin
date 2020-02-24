package de.aservo.atlassian.confluence.confapi.helper;

import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebAuthenticationHelperTest {

    private final ConfluenceUser user = new ConfluenceUserImpl();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Mock
    private PermissionManager permissionManager;

    private WebAuthenticationHelper webAuthenticationHelper;

    @Before
    public void setup() {
        webAuthenticationHelper = new WebAuthenticationHelper(permissionManager);
    }

    @Test
    public void testNotAuthenticated() {
        final WebAuthenticationHelper webAuthenticationHelper = spy(this.webAuthenticationHelper);
        when(webAuthenticationHelper.getAuthenticatedUser()).thenReturn(null);
        exceptionRule.expect(WebApplicationException.class);
        webAuthenticationHelper.mustBeSysAdmin();
    }

    @Test
    public void testNotSysAdmin() {
        final WebAuthenticationHelper webAuthenticationHelper = spy(this.webAuthenticationHelper);
        when(webAuthenticationHelper.getAuthenticatedUser()).thenReturn(user);
        when(permissionManager.isSystemAdministrator(user)).thenReturn(false);
        exceptionRule.expect(WebApplicationException.class);
        webAuthenticationHelper.mustBeSysAdmin();
    }

    @Test
    @SuppressWarnings("java:S2699") // Ignore that no assertion is present
    public void testIsSysAdmin() {
        final WebAuthenticationHelper webAuthenticationHelper = spy(this.webAuthenticationHelper);
        when(webAuthenticationHelper.getAuthenticatedUser()).thenReturn(user);
        when(permissionManager.isSystemAdministrator(user)).thenReturn(true);
        webAuthenticationHelper.mustBeSysAdmin();
    }

}
