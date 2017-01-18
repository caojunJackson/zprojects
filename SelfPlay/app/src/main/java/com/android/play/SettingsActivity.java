package com.android.play;

import android.os.RemoteException;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;


import com.android.entity.Constant;


public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener ,Preference.OnPreferenceClickListener {
    private String TAG = Constant.TAG;

    private String GESTURE_CHANGE_SONG = "getsture_change_song";
    private String SWICH_CHANGE_SONG_STATE = "switch_change_song";
    private SwitchPreference mSwitchPrefer;

    private String LOGOUT_MAIN = "main_logout";
    private Preference mLogoutPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.setting_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("设置");


        getActionBar().setHomeAsUpIndicator(R.drawable.ic_ab_back);


        mSwitchPrefer = (SwitchPreference) findPreference(GESTURE_CHANGE_SONG);
        mSwitchPrefer.setOnPreferenceChangeListener(SettingsActivity.this);
        mSwitchPrefer.setChecked(Settings.System.getInt(getContentResolver(),SWICH_CHANGE_SONG_STATE,0) == 1);

        mLogoutPre = findPreference(LOGOUT_MAIN);
        mLogoutPre.setOnPreferenceClickListener(SettingsActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Log.i(TAG," SettingActivity : preference = "+preference);

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }



    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.i(TAG,"SettingsActivity onPreferenceChange : newValue = "+newValue);
        boolean isChecked =  (Boolean) newValue;
        if(preference.getKey().equals(GESTURE_CHANGE_SONG)){
            Settings.System.putInt(getContentResolver(), SWICH_CHANGE_SONG_STATE, isChecked ? 1 : 0);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.i(TAG,"SettingsActivity onPreferenceClick : preference = "+preference);
        return true;
    }
}
