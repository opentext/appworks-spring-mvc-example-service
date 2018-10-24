package com.appworks.web.xml.less.services;

import com.opentext.otag.sdk.client.v3.MailClient;
import com.opentext.otag.sdk.client.v3.NotificationsClient;
import com.opentext.otag.sdk.client.v3.RuntimesClient;
import com.opentext.otag.sdk.client.v3.TrustedProviderClient;
import com.opentext.otag.sdk.types.v3.MailRequest;
import com.opentext.otag.sdk.types.v3.MailResult;
import com.opentext.otag.sdk.types.v3.TrustedProvider;
import com.opentext.otag.sdk.types.v3.TrustedProviders;
import com.opentext.otag.sdk.types.v3.api.SDKResponse;
import com.opentext.otag.sdk.types.v3.apps.Runtime;
import com.opentext.otag.sdk.types.v3.apps.Runtimes;
import com.opentext.otag.sdk.types.v3.notification.ClientPushNotificationRequest;
import com.opentext.otag.sdk.types.v3.notification.NotificationRequest;
import com.opentext.otag.sdk.types.v3.notification.NotificationSeqBounds;
import com.opentext.otag.sdk.types.v3.notification.fcm.FcmPushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.opentext.otag.sdk.util.StringUtil.isNullOrEmpty;
import static java.util.Collections.*;

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

    @Autowired
    private NotificationsClient notificationsClient;

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

    @Override
    public ResponseEntity sendTestNotification(String username, boolean sendPush) {
        LOG.info("Checking current notifications bounds");
        NotificationSeqBounds notificationsMinMaxSeq = notificationsClient.getNotificationsMinMaxSeq();
        LOG.info("Checking current notifications bounds - {}", notificationsMinMaxSeq);

        try {
            SDKResponse sdkResponse;
            if (sendPush) {
                Set<String> runtimeIds = getKnownRuntimes().getRuntimes().stream()
                        .map(Runtime::getName).collect(Collectors.toSet());

                FcmPushNotificationRequest notificationRequest = new FcmPushNotificationRequest(
                        new ClientPushNotificationRequest.Builder()
                                .addUser(username)
                                .addData("someKey", "someValue")
                                .runtimes(runtimeIds));
                notificationRequest.setTargetAppName("this-app-does-not-exist");

                LOG.info("Issuing push notification via the SDK - {}", notificationRequest);
                sdkResponse = notificationsClient.sendFcmPushNotification(notificationRequest);
            } else {
                NotificationRequest notificationRequest = new NotificationRequest(
                        "{ \"someKey\": \"someValue\" }", emptySet(), singleton(username));
                LOG.info("Issuing web notification via the SDK - {}", notificationRequest);
                sdkResponse = notificationsClient.sendNotification(notificationRequest);
            }

            return new ResponseEntity<>(sdkResponse, sdkResponse.isSuccess() ?
                    HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOG.error("Failed to send notification", e);
            String message = e.getMessage();
            if (!isNullOrEmpty(message)) {
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
