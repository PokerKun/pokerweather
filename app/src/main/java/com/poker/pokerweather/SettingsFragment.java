package com.poker.pokerweather;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

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
