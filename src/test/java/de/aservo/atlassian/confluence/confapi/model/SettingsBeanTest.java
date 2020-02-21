package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.confluence.settings.setup.DefaultTestSettings;
import com.atlassian.confluence.settings.setup.OtherTestSettings;
import com.atlassian.confluence.setup.settings.Settings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SettingsBeanTest {

    @Test
    public void testDefaultConstructor() {
        final SettingsBean bean = new SettingsBean();

        assertNull(bean.getBaseurl());
        assertNull(bean.getTitle());
    }

    @Test
    public void testFromConstructor() throws Exception {
        final Settings settings = new DefaultTestSettings();
        final SettingsBean bean = SettingsBean.from(settings);

        assertEquals(bean.getBaseurl(), settings.getBaseUrl());
        assertEquals(bean.getTitle(), settings.getSiteTitle());
    }

    @Test
    public void testEqualsHashcode() throws Exception {
        final Settings settings = new DefaultTestSettings();
        final SettingsBean bean1 = SettingsBean.from(settings);
        final SettingsBean bean2 = SettingsBean.from(settings);
        assertEquals(bean1, bean2);
        assertEquals(bean1.hashCode(), bean2.hashCode());
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "SimplifiableJUnitAssertion"})
    public void testEqualsHashcodeNull() {
        final SettingsBean bean = new SettingsBean();
        assertFalse(bean.equals(null));
        // cannot test hashCode of null
    }

    @Test
    public void testEqualsHashcodeSameInstance() {
        final SettingsBean bean = new SettingsBean();
        assertEquals(bean, bean);
        assertEquals(bean.hashCode(), bean.hashCode());
    }

    @Test
    @SuppressWarnings("SimplifiableJUnitAssertion")
    public void testEqualsHashcodeOtherType() {
        final SettingsBean bean = new SettingsBean();
        assertFalse(bean.equals(new Object()));
        assertFalse(bean.hashCode() == new Object().hashCode());
    }

    @Test
    @SuppressWarnings("SimplifiableJUnitAssertion")
    public void testEqualsHashcodeOtherInstance() throws Exception {
        final Settings settings1 = new DefaultTestSettings();
        final Settings settings2 = new OtherTestSettings();
        final SettingsBean bean1 = SettingsBean.from(settings1);
        final SettingsBean bean2 = SettingsBean.from(settings2);
        assertFalse(bean1.equals(bean2));
        assertFalse(bean1.hashCode() == bean2.hashCode());
    }

}
