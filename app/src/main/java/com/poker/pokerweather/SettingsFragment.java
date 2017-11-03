package com.poker.pokerweather;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import static android.os.Build.*;

/**
 * Created by Poker on 2017/10/11.
 */

public class SettingsFragment extends PreferenceFragment{

    private CheckBoxPreference weatherUpdateCheckBox;

    private CheckBoxPreference bingUpdateCheckBox;

    private ListPreference updateTimeList;

    private ListPreference tempUnitList;

    private EditTextPreference apiKeyEditText;

    private EditTextPreference cityApiEditText;

    private EditTextPreference bingApiEditText;

    private PreferenceScreen aboutScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        initPreferenceViews();
    }

    private void initPreferenceViews() {
        weatherUpdateCheckBox = (CheckBoxPreference)findPreference("check_box_auto_weather");
        bingUpdateCheckBox = (CheckBoxPreference)findPreference("check_box_auto_bing");
        updateTimeList = (ListPreference)findPreference("list_update_time");
        tempUnitList = (ListPreference)findPreference("list_temp_unit");
        apiKeyEditText = (EditTextPreference)findPreference("edit_text_api_key");
        cityApiEditText = (EditTextPreference)findPreference("edit_text_city_api");
        bingApiEditText = (EditTextPreference)findPreference("edit_text_bing_api");
        aboutScreen = (PreferenceScreen)findPreference("about_version");
        try {
            aboutScreen.setSummary("Version " + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getVersionName() throws Exception
    {
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if ("check_box_auto_weather".equals(preference.getKey()) || "check_box_auto_bing".equals(preference.getKey())) {
            if (!weatherUpdateCheckBox.isChecked() && !bingUpdateCheckBox.isChecked()) {
                updateTimeList.setEnabled(false);
            } else {
                updateTimeList.setEnabled(true);
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
