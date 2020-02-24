package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.setup.settings.Settings;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.atlassian.confluence.confapi.helper.WebAuthenticationHelper;
import de.aservo.atlassian.confluence.confapi.model.SettingsBean;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static de.aservo.atlassian.confluence.confapi.rest.SettingsResource.SETTINGS_PATH;

/**
 * Resource to set general configuration.
 */
@Path(SETTINGS_PATH)
@Produces({MediaType.APPLICATION_JSON})
@Named("SettingsResource")
public class SettingsResource {

    public static final String SETTINGS_PATH = "settings";

    @ComponentImport
    private final SettingsManager settingsManager;

    private final WebAuthenticationHelper webAuthenticationHelper;

    @Inject
    public SettingsResource(
            final SettingsManager settingsManager,
            final WebAuthenticationHelper webAuthenticationHelper) {

        this.settingsManager = settingsManager;
        this.webAuthenticationHelper = webAuthenticationHelper;
    }

    @GET
    public Response getSettings() {
        webAuthenticationHelper.mustBeSysAdmin();

        final Settings settings = settingsManager.getGlobalSettings();

        final SettingsBean settingsBean = new SettingsBean(
                settings.getBaseUrl(),
                settings.getSiteTitle()
        );

        return Response.ok(settingsBean).build();
    }

    @PUT
    public Response putSettings(
            final SettingsBean bean) {

        webAuthenticationHelper.mustBeSysAdmin();

        final Settings settings = settingsManager.getGlobalSettings();

        if (StringUtils.isNotBlank(bean.getBaseurl())) {
            settings.setBaseUrl(bean.getBaseurl());
        }

        if (StringUtils.isNotBlank(bean.getTitle())) {
            settings.setSiteTitle(bean.getTitle());
        }

        settingsManager.updateGlobalSettings(settings);

        return Response.ok(SettingsBean.from(settings)).build();
    }

}
