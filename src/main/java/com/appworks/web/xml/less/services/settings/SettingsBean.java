package com.appworks.web.xml.less.services.settings;

import com.appworks.web.xml.less.AppWorksSpringMVCService;
import com.opentext.otag.sdk.client.v3.SettingsClient;
import com.opentext.otag.sdk.types.v3.api.SDKResponse;
import com.opentext.otag.sdk.types.v3.settings.Setting;
import com.opentext.otag.sdk.types.v3.settings.SettingType;
import com.opentext.otag.sdk.types.v3.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Collections.singletonList;

@Component
public class SettingsBean {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsBean.class);

    static final String SETTING_KEY = "sdk.setting.key";

    @Autowired
    private SettingsClient settingsClient;

    public Settings createAndGetServiceSettings() {
        Setting setting = settingsClient.getSetting(SETTING_KEY);
        if (setting != null) {
            updateSetting(setting);
        } else {
            createSetting();
        }

        return new Settings(singletonList(settingsClient.getSetting(SETTING_KEY)));
    }

    private void updateSetting(Setting setting) {
        LOG.info("A setting with key {} already existed - {}", SETTING_KEY, setting);
        setting.setValue(UUID.randomUUID().toString());

        SDKResponse updateResponse = settingsClient.updateSetting(setting);

        if (updateResponse.isSuccess()) {
            LOG.info("Updated setting {} value to {} - response={}", SETTING_KEY, setting.getValue(), updateResponse);
        } else {
            LOG.warn("Failed to update setting {} - response={}", SETTING_KEY, updateResponse);
        }
    }

    private void createSetting() {
        LOG.info("Creating new setting for key {}", SETTING_KEY);
        Setting setting = new Setting(SETTING_KEY, AppWorksSpringMVCService.APP_NAME, SettingType.string,
                "Random String", UUID.randomUUID().toString(), "", "A setting with a random String value",
                false /* readOnly  */, "0" /* seqNo */);

        SDKResponse createResponse = settingsClient.createSetting(setting);
        if (createResponse.isSuccess()) {
            LOG.info("Created setting {} - response={}", SETTING_KEY, createResponse);
        } else {
            LOG.warn("Failed to create setting {} - response={}", SETTING_KEY, createResponse);
        }
    }

}
