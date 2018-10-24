package com.appworks.web.xml.less.services.settings;

import com.opentext.otag.sdk.handlers.SettingChangeHandler;
import com.opentext.otag.sdk.types.v3.message.SettingsChangeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responds to changes made to our sole setting via the AppWorks SDK. Has to be a top level (non-nested)
 * class, the SDK is responsible for its instantiation too.
 */
public class SettingChangeLogger implements SettingChangeHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SettingChangeLogger.class);

    @Override
    public String getSettingKey() {
        return SettingsBean.SETTING_KEY;
    }

    @Override
    public void onSettingChanged(SettingsChangeMessage settingsChangeMessage) {
        LOG.info("We were informed of a change to setting {}, new value={}",
                settingsChangeMessage.getKey(), settingsChangeMessage.getNewValue());
    }

    @Override
    public void handle(SettingsChangeMessage settingsChangeMessage) {
        LOG.info("Handling settings change message: {}", settingsChangeMessage);
    }

}
