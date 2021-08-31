package com.aritmetica.rober.arithmetik;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by rober_000 on 26/06/2015.
 */
public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();
    }


}
