package com.appworks.web.xml.less.services;

import com.opentext.otag.sdk.types.v3.TrustedProviders;
import com.opentext.otag.sdk.types.v3.apps.Runtimes;
import org.springframework.http.ResponseEntity;

public interface GatewaySdkBean {

    /**
     * Get the list of {@link com.opentext.otag.sdk.types.v3.apps.Runtime}s that the Gateway knows
     * about.
     *
     * @return runtimes wrapper
     */
    Runtimes getKnownRuntimes();

    TrustedProviders getTrustedProviders();

    ResponseEntity sendTestEmail(String from, String to, boolean sendAsync);

}
