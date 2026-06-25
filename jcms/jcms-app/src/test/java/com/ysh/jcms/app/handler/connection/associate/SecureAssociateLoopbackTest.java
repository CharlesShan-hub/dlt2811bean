package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseLoopbackTest;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.utils.security.GmAuthenticator;
import com.ysh.jcms.utils.security.GmCredentialManager;
import com.ysh.jcms.utils.security.GmSignature;
import com.ysh.jcms.utils.security.GmTrustManager;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.junit.Test;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

import static org.junit.Assert.*;

/**
 * Loopback test for Associate service with GM authentication.
 *
 * <p>SM2 keys are generated at runtime (no cert files required).
 */
public class SecureAssociateLoopbackTest extends BaseLoopbackTest {

    @Override
    protected void registerServers(CmsNode node) throws Exception {
        // Server generates its own SM2 key and self-signed certificate
        KeyPair serverKeyPair = GmSignature.generateKeyPair();
        X509Certificate serverCert = GmSignature.generateSelfSignedCertificate(serverKeyPair);

        regServer(node, new AssociateServer()
            .enableSecurity(new GmAuthenticator(new GmTrustManager().trustAll()), serverCert));
    }

    @Override
    protected void registerClients(CmsNode node) throws Exception {
        // Client generates its own SM2 key and self-signed certificate
        KeyPair clientKeyPair = GmSignature.generateKeyPair();
        X509Certificate clientCert = GmSignature.generateSelfSignedCertificate(clientKeyPair);
        GmCredentialManager clientCredential = GmCredentialManager.fromKeyAndCert(
            clientKeyPair.getPrivate(), clientCert);

        regClient(node, new AssociateClient(node)
            .credentialManager(clientCredential));
    }

    @Test
    public void associate_with_security() throws Exception {
        AssociateClientDao dao = new AssociateClientDao()
            .sapRef("IED1/AP1").secure(true);

        CmsAssociateResponse resp = exec(clientNode(), AssociateClient.class, dao);

        assertNotNull(resp);
        assertEquals(0, resp.serviceError.value());
        assertEquals(64, resp.assocId.len);
        assertEquals(SessionState.ASSOCIATED, clientNode().getClient().getSession().getState());
        assertNotNull(clientNode().getClient().getSession().getAssociationId());
    }

    @Test
    public void associate_secure_reject_when_already_associated() throws Exception {
        AssociateClientDao dao = new AssociateClientDao()
            .sapRef("IED1/AP1").secure(true);

        exec(clientNode(), AssociateClient.class, dao);

        try {
            exec(clientNode(), AssociateClient.class, dao);
            fail("Should throw on second associate");
        } catch (java.io.IOException e) {
            assertTrue(e.getMessage().contains("error=2"));
        }
    }
}
