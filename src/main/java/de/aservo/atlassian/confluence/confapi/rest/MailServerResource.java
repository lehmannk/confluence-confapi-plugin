package de.aservo.atlassian.confluence.confapi.rest;

import com.atlassian.mail.MailException;
import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.SMTPMailServer;
import com.atlassian.mail.server.impl.PopMailServerImpl;
import com.atlassian.mail.server.impl.SMTPMailServerImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.atlassian.confluence.confapi.helper.WebAuthenticationHelper;
import de.aservo.atlassian.confluence.confapi.model.PopMailServerBean;
import de.aservo.atlassian.confluence.confapi.model.SmtpMailServerBean;
import de.aservo.atlassian.confluence.confapi.model.ErrorCollection;
import de.aservo.atlassian.confluence.confapi.util.MailProtocolUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static de.aservo.atlassian.confluence.confapi.rest.MailServerResource.MAIL_PATH;

/**
 * Resource to set mail server configuration.
 */
@Path(MAIL_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Named
public class MailServerResource {

    public static final String MAIL_PATH = "mail";
    public static final String MAIL_SMTP_PATH = "smtp";
    public static final String MAIL_POP_PATH = "pop";

    private static final Logger log = LoggerFactory.getLogger(MailServerResource.class);

    @ComponentImport
    private final MailServerManager mailServerManager;

    private final WebAuthenticationHelper webAuthenticationHelper;

    /**
     * Constructor.
     *
     * @param mailServerManager the injected {@link MailServerManager}
     */
    @Inject
    public MailServerResource(
            final MailServerManager mailServerManager,
            final WebAuthenticationHelper webAuthenticationHelper) {

        this.mailServerManager = mailServerManager;
        this.webAuthenticationHelper = webAuthenticationHelper;
    }

    @GET
    @Path(MAIL_SMTP_PATH)
    public Response getSmtpMailServer() {
        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = new ErrorCollection();

        try {
            final SMTPMailServer smtpMailServer = mailServerManager.getDefaultSMTPMailServer();
            final SmtpMailServerBean bean = SmtpMailServerBean.from(smtpMailServer);
            return Response.ok(bean).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.NO_CONTENT).entity(errorCollection).build();
    }

    @PUT
    @Path(MAIL_SMTP_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putSmtpMailServer(
            final SmtpMailServerBean bean) {

        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = new ErrorCollection();

        final SMTPMailServer smtpMailServer = mailServerManager.isDefaultSMTPMailServerDefined()
                ? mailServerManager.getDefaultSMTPMailServer()
                : new SMTPMailServerImpl();

        assert smtpMailServer != null;

        if (StringUtils.isNotBlank(bean.getName())) {
            smtpMailServer.setName(bean.getName());
        }

        if (StringUtils.isNotBlank(bean.getDescription())) {
            smtpMailServer.setDescription(bean.getDescription());
        }

        if (StringUtils.isNotBlank(bean.getFrom())) {
            smtpMailServer.setDefaultFrom(bean.getFrom());
        }

        if (StringUtils.isNotBlank(bean.getPrefix())) {
            smtpMailServer.setPrefix(bean.getPrefix());
        }

        smtpMailServer.setMailProtocol(MailProtocolUtil.find(bean.getProtocol(), MailProtocol.SMTP));

        if (StringUtils.isNotBlank(bean.getHost())) {
            smtpMailServer.setHostname(bean.getHost());
        }

        if (bean.getPort() != null) {
            smtpMailServer.setPort(String.valueOf(bean.getPort()));
        } else {
            smtpMailServer.setPort(smtpMailServer.getMailProtocol().getDefaultPort());
        }

        smtpMailServer.setTlsRequired(bean.isTls());

        if (StringUtils.isNotBlank(bean.getUsername())) {
            smtpMailServer.setUsername(bean.getUsername());
        }

        smtpMailServer.setTimeout(bean.getTimeout());

        try {
            if (mailServerManager.isDefaultSMTPMailServerDefined()) {
                mailServerManager.update(smtpMailServer);
            } else {
                smtpMailServer.setId(mailServerManager.create(smtpMailServer));
            }

            return Response.ok(bean).build();
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

    @GET
    @Path(MAIL_POP_PATH)
    public Response getPopMailServer() {
        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = new ErrorCollection();

        try {
            final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer();
            final PopMailServerBean bean = PopMailServerBean.from(popMailServer);
            return Response.ok(bean).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.NO_CONTENT).entity(errorCollection).build();
    }

    @PUT
    @Path(MAIL_POP_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPopMailServer(
            final PopMailServerBean bean) {

        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = new ErrorCollection();

        final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer() != null
                ? mailServerManager.getDefaultPopMailServer()
                : new PopMailServerImpl();

        assert popMailServer != null;

        if (StringUtils.isNotBlank(bean.getName())) {
            popMailServer.setName(bean.getName());
        }

        if (StringUtils.isNotBlank(bean.getDescription())) {
            popMailServer.setDescription(bean.getDescription());
        }

        popMailServer.setMailProtocol(MailProtocolUtil.find(bean.getProtocol(), MailProtocol.POP));

        if (StringUtils.isNotBlank(bean.getHost())) {
            popMailServer.setHostname(bean.getHost());
        }

        if (bean.getPort() != null) {
            popMailServer.setPort(String.valueOf(bean.getPort()));
        } else {
            popMailServer.setPort(popMailServer.getMailProtocol().getDefaultPort());
        }

        if (StringUtils.isNotBlank(bean.getUsername())) {
            popMailServer.setUsername(bean.getUsername());
        }

        popMailServer.setTimeout(bean.getTimeout());

        try {
            if (mailServerManager.getDefaultPopMailServer() != null) {
                mailServerManager.update(popMailServer);
            } else {
                popMailServer.setId(mailServerManager.create(popMailServer));
            }

            return Response.ok(bean).build();
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

}
