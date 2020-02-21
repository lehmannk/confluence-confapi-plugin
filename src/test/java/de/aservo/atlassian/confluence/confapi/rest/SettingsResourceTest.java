package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.confluence.settings.setup.DefaultTestSettings;
import com.atlassian.confluence.settings.setup.OtherTestSettings;
import com.atlassian.confluence.setup.settings.Settings;
import com.atlassian.confluence.setup.settings.SettingsManager;
import de.aservo.atlassian.confluence.confapi.model.SettingsBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static de.aservo.atlassian.confluence.confapi.rest.SettingsResource.SETTINGS_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SettingsResourceTest {

    public static final String OTHER_BASEURL = "http://localhost:1990/confluence";
    public static final String OTHER_TITLE = "Other title";

    @Mock
    private SettingsManager settingsManager;

    private SettingsResource settingsResource;

    @Before
    public void setup() {
        settingsResource = new SettingsResource(settingsManager);
    }

    @Test
    public void testSettingsAnnotation() {
        final Class<? extends SettingsResource> settingsResourceClass = settingsResource.getClass();

        final Path settingsResourceClassAnnotation = settingsResourceClass.getAnnotation(Path.class);
        assertNotNull(settingsResourceClassAnnotation);
        assertEquals(SETTINGS_PATH, settingsResourceClassAnnotation.value());
    }

    @Test
    public void testGetSettings() {
        final Settings settings = new DefaultTestSettings();
        doReturn(settings).when(settingsManager).getGlobalSettings();

        final Response response = settingsResource.getSettings();
        final SettingsBean bean = (SettingsBean) response.getEntity();

        assertEquals(SettingsBean.from(settings), bean);
    }

    @Test
    public void testPutSettings() {
        final Settings defaultSettings = new DefaultTestSettings();
        doReturn(defaultSettings).when(settingsManager).getGlobalSettings();

        final Settings updateSettings = new OtherTestSettings();
        final SettingsBean requestBean = SettingsBean.from(updateSettings);
        final Response response = settingsResource.putSettings(requestBean);
        final SettingsBean responseBean = (SettingsBean) response.getEntity();

        final ArgumentCaptor<Settings> settingsCaptor = ArgumentCaptor.forClass(Settings.class);
        verify(settingsManager).updateGlobalSettings(settingsCaptor.capture());
        final Settings settings = settingsCaptor.getValue();

        assertEquals(SettingsBean.from(updateSettings), SettingsBean.from(settings));
        assertEquals(requestBean, responseBean);
    }

}
