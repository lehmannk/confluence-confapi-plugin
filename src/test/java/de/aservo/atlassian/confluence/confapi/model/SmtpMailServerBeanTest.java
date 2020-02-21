package de.aservo.atlassian.confluence.confapi.model;

import com.atlassian.mail.server.DefaultTestSmtpMailServerImpl;
import com.atlassian.mail.server.OtherTestSmtpMailServerImpl;
import com.atlassian.mail.server.SMTPMailServer;
import de.aservo.atlassian.confluence.confapi.exception.NotConfiguredException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static de.aservo.atlassian.confluence.confapi.model.PopMailServerBean.DEFAULT_TIMEOUT;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SmtpMailServerBeanTest {

    @Test
    public void testDefaultConstructor() {
        final SmtpMailServerBean bean = new SmtpMailServerBean();

        assertNull(bean.getName());
        assertNull(bean.getDescription());
        assertNull(bean.getFrom());
        assertNull(bean.getPrefix());
        assertNull(bean.getProtocol());
        assertNull(bean.getHost());
        assertNull(bean.getPort());
        assertFalse(bean.isTls());
        assertEquals(DEFAULT_TIMEOUT, bean.getTimeout());
        assertNull(bean.getUsername());
        assertNull(bean.getPassword());
    }

    @Test
    public void testFromConstructor() throws Exception {
        final SMTPMailServer server = new DefaultTestSmtpMailServerImpl();
        final SmtpMailServerBean bean = SmtpMailServerBean.from(server);

        assertEquals(bean.getName(), server.getName());
        assertEquals(bean.getDescription(), server.getDescription());
        assertEquals(bean.getFrom(), server.getDefaultFrom());
        assertEquals(bean.getPrefix(), server.getPrefix());
        assertEquals(bean.getProtocol(), server.getMailProtocol().getProtocol());
        assertEquals(bean.getHost(), server.getHostname());
        assertEquals(bean.getPort(), Integer.valueOf(server.getPort()));
        assertEquals(bean.isTls(), server.isTlsRequired());
        assertEquals(bean.getTimeout(), server.getTimeout());
        assertEquals(bean.getUsername(), server.getUsername());
        // assertEquals(bean.getPassword(), server.getPassword());
    }

    @Test
    public void testFromConstructorHideEmptyDescription() throws Exception {
        final SMTPMailServer server = new DefaultTestSmtpMailServerImpl();
        server.setDescription("");
        final SmtpMailServerBean bean = SmtpMailServerBean.from(server);

        assertNull(bean.getDescription());
    }

    @Test
    public void testEqualsHashcode() throws NotConfiguredException {
        final SMTPMailServer server = new DefaultTestSmtpMailServerImpl();
        final SmtpMailServerBean bean1 = SmtpMailServerBean.from(server);
        final SmtpMailServerBean bean2 = SmtpMailServerBean.from(server);
        assertEquals(bean1, bean2);
        assertEquals(bean1.hashCode(), bean2.hashCode());
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "SimplifiableJUnitAssertion"})
    public void testEqualsHashcodeNull() {
        final SmtpMailServerBean bean = new SmtpMailServerBean();
        assertFalse(bean.equals(null));
        // cannot test hashCode of null
    }

    @Test
    public void testEqualsHashcodeSameInstance() {
        final SmtpMailServerBean bean = new SmtpMailServerBean();
        assertEquals(bean, bean);
        assertEquals(bean.hashCode(), bean.hashCode());
    }

    @Test
    @SuppressWarnings("SimplifiableJUnitAssertion")
    public void testEqualsHashcodeOtherType() {
        final SmtpMailServerBean bean = new SmtpMailServerBean();
        assertFalse(bean.equals(new Object()));
        assertFalse(bean.hashCode() == new Object().hashCode());
    }

    @Test
    @SuppressWarnings("SimplifiableJUnitAssertion")
    public void testEqualsHashcodeOtherInstance() throws NotConfiguredException {
        final SMTPMailServer server1 = new DefaultTestSmtpMailServerImpl();
        final SMTPMailServer server2 = new OtherTestSmtpMailServerImpl();
        final SmtpMailServerBean bean1 = SmtpMailServerBean.from(server1);
        final SmtpMailServerBean bean2 = SmtpMailServerBean.from(server2);
        assertFalse(bean1.equals(bean2));
        assertFalse(bean1.hashCode() == bean2.hashCode());
    }

}
