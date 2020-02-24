package de.aservo.atlassian.confluence.confapi.helper;

import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.user.User;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Named
public class WebAuthenticationHelper {

    @ComponentImport
    private final PermissionManager permissionManager;

    @Inject
    public WebAuthenticationHelper(
            final PermissionManager permissionManager) {

        this.permissionManager = permissionManager;
    }

    public User getAuthenticatedUser() {
        return AuthenticatedUserThreadLocal.get();
    }

    public void mustBeSysAdmin() {
        final User user = getAuthenticatedUser();

        if (user == null) {
            // NOSONAR: Ignore that WebApplicationException is a RuntimeException
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        final boolean isSysAdmin = permissionManager.isSystemAdministrator(user);

        if (!isSysAdmin) {
            // NOSONAR: Ignore that WebApplicationException is a RuntimeException
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

}
