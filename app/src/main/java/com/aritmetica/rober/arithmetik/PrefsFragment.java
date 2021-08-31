package com.aritmetica.rober.arithmetik;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.aritmetica.roliveira.arithmetik.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.File;

public class PrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private Preference pref, disconnectAcount;
	private static PreferenceScreen screen;
	private static File file;
	private Boolean mconnect=false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		getPreferenceManager().findPreference("nivel_dificuldade");

		Preference tempo = findPreference("tempo");
		screen = getPreferenceScreen();
		pref=getPreferenceManager().findPreference("btnApagar");
		disconnectAcount=getPreferenceManager().findPreference("btnDisconnectAccount");
		file=getActivity().getBaseContext().getFileStreamPath(Jogo.NOME_FICHEIRO);



		//define time
		tempo.setOnPreferenceChangeListener(
				new Preference.OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference, Object newValue) {
						try {

							if (Integer.parseInt(newValue.toString()) == 1913){
								Toast.makeText(getActivity().getBaseContext(), "Merci Alex!", Toast.LENGTH_LONG).show();
								return false;
							}

							if (Integer.parseInt(newValue.toString()) < 0 || Integer.parseInt(newValue.toString()) > 60) {
								Toast.makeText(getActivity().getBaseContext(), R.string.AlertTempo, Toast.LENGTH_LONG).show();
								AlertDialog.Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setMessage(getActivity().getString(R.string.AlertTempo))
										.setCancelable(false)
										.setPositiveButton("Ok",
												new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,
																		int id) {
													}
												});
								AlertDialog alert = builder.create();
								alert.show();
								return false;
							}

						} catch (NumberFormatException e) {
							Toast.makeText(getActivity().getBaseContext(), R.string.AlertTempo, Toast.LENGTH_LONG).show();
							return false;
						}
						return true;
					}

				});
		
		
		pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				//code for what you want it to do
				if (file.exists()) {
					file.delete();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage(getActivity().getString(R.string.TreinoApagado))
							.setCancelable(false)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
					PrefsFragment.screen.removePreference(pref);
				}
				return true;
			}
		});

        /*disconnectAcount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                private void revokeAccess() {
                    mGoogleSignInClient.revokeAccess()
                            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // ...
                                }
                            });
                }
                    PrefsFragment.screen.removePreference(disconnectAcount);
                return true;
            }
        });*/



	}


	public void onStart() {
		super.onStart();

		// Check for existing Google Sign In account, if the user is already signed in
		// the GoogleSignInAccount will be non-null.
		GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
		mconnect = account != null;
	}
	
	public void onResume(){
		super.onResume();
		if (!file.exists()){
			PrefsFragment.screen.removePreference(pref);
		}
		if (!mconnect){
			PrefsFragment.screen.removePreference(disconnectAcount);
		}
		getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
		//a linha seguinte deve ser apagada para programar o revoke access
        PrefsFragment.screen.removePreference(disconnectAcount);
	}
	
	public void onPause(){
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
	}
}
