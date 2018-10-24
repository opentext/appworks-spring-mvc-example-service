package com.appworks.web.xml.less.services;

import com.opentext.otag.sdk.client.v3.MailClient;
import com.opentext.otag.sdk.client.v3.RuntimesClient;
import com.opentext.otag.sdk.client.v3.TrustedProviderClient;
import com.opentext.otag.sdk.types.v3.MailRequest;
import com.opentext.otag.sdk.types.v3.MailResult;
import com.opentext.otag.sdk.types.v3.TrustedProvider;
import com.opentext.otag.sdk.types.v3.TrustedProviders;
import com.opentext.otag.sdk.types.v3.api.SDKResponse;
import com.opentext.otag.sdk.types.v3.apps.Runtimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;

@Component
public class GatewaySdkBeanImpl implements GatewaySdkBean {

    private static final Logger LOG = LoggerFactory.getLogger(GatewaySdkBeanImpl.class);

    private static final String TRUSTED_PROVIDER_NAME = "sdk-trusted-provider";

    @Autowired
    private MailClient mailClient;

    @Autowired
    private RuntimesClient runtimesClient;

    @Autowired
    private TrustedProviderClient trustedProviderClient;

    @Override
    public Runtimes getKnownRuntimes() {
        return runtimesClient.getAllRuntimes();
    }

    @Override
    public TrustedProviders getTrustedProviders() {
        TrustedProvider testProvider = trustedProviderClient.getOrCreate(TRUSTED_PROVIDER_NAME);
        if (testProvider != null) {
            LOG.info("Retrieved our test provider - {}", testProvider);
        }

        TrustedProviders allProviders = trustedProviderClient.getAllProviders();
        LOG.info("Returning {} trusted provider/s the Gateway is aware of");
        return allProviders;
    }

    @Override
    public ResponseEntity sendTestEmail(String from, String to, boolean sendAsync) {
        if (from == null || from.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        MailRequest mailReq = new MailRequest(from, singletonList(to),
                "SDK test subject", "An email was sent via the SDK test service");
        if (sendAsync) {
            SDKResponse mailSendResp = mailClient.sendMailAsync(mailReq);
            return new ResponseEntity<>(mailSendResp, mailSendResp.isSuccess() ?
                    HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            MailResult mailResult = mailClient.sendMail(mailReq);
            return new ResponseEntity<>(mailResult, mailResult.isSuccess() ?
                    HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
