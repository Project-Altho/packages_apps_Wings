/*
 * Copyright (C) 2017-2019 The PixelDust Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.altho.settings.fragments;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.content.DialogInterface.OnCancelListener;
import android.provider.SearchIndexableResource;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.search.BaseSearchIndexProvider;

import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;
import com.android.internal.logging.nano.MetricsProto;
import com.altho.settings.preference.SecureSettingListPreference;
import com.altho.settings.preference.SecureSettingListPreference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuickSettings extends SettingsPreferenceFragment {

    private static final String KEY_STATUS_BAR_AM_PM = "status_bar_am_pm";
    private static final String KEY_STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";
    private static final String KEY_STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";

    private SecureSettingListPreference mStatusBarAmPm;
    private SecureSettingListPreference mBatteryStyle;
    private SystemSettingListPreference mShowPercentage;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.altho_settings_quicksettings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mStatusBarAmPm = findPreference(KEY_STATUS_BAR_AM_PM);

        mBatteryStyle = findPreference(KEY_STATUS_BAR_BATTERY_STYLE);
        mShowPercentage = findPreference(KEY_STATUS_BAR_SHOW_BATTERY_PERCENT);

        mBatteryStyle.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (DateFormat.is24HourFormat(requireContext())) {
            mStatusBarAmPm.setEnabled(false);
            mStatusBarAmPm.setSummary(R.string.status_bar_am_pm_unavailable);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ((ListPreference)preference).setValue((String)newValue);
        updateStates();
        return false;
    }

    private void updateStates() {
        if ("2".equals(mBatteryStyle.getValue()))
            mShowPercentage.setEnabled(false);
        else
            mShowPercentage.setEnabled(true);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ALTHO;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                    boolean enabled) {
                final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                final SearchIndexableResource sir = new SearchIndexableResource(context);
                sir.xmlResId = R.xml.altho_settings_quicksettings;
                result.add(sir);
                return result;
            }
            @Override
            public List<String> getNonIndexableKeys(Context context) {
                final List<String> keys = super.getNonIndexableKeys(context);
                return keys;
            }
    };
}
