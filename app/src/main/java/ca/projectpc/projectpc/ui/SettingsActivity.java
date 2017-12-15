/*
 * ProjectPC
 *
 * Copyright (C) 2017 ProjectPC. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or any
 * later version. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package ca.projectpc.projectpc.ui;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import ca.projectpc.projectpc.R;

public class SettingsActivity extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState
     * Enable back button
     * Load settings fragment
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String stringValue = newValue.toString();

                    if (preference instanceof RingtonePreference) {
                        if (TextUtils.isEmpty(stringValue)) {
                            preference.setSummary(R.string.prompt_select_silent);
                        } else {
                            Ringtone ringtone = RingtoneManager.getRingtone(
                                    preference.getContext(), Uri.parse(stringValue));

                            if (ringtone == null) {
                                preference.setSummary(R.string.prompt_choose_ringtone);
                            } else {
                                String label = ringtone.getTitle(preference.getContext());
                                preference.setSummary(label);
                            }
                        }

                    }

                    return true;
                }
            };


    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            bindPreferenceSummaryToValue(findPreference("message_ringtone"));

            Preference cacheButton = findPreference("clear_cache");
            cacheButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // TODO: Clear cache
                    return true;
                }
            });

            Preference bookmarksButton = findPreference("clear_bookmarks");
            bookmarksButton.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            // TODO: Clear bookmarks
                            return true;
                        }
                    });
        }
    }
}
