package com.aritmetica.rober.arithmetik;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.aritmetica.roliveira.arithmetik.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.util.Objects;


public class Entrada extends Activity implements
        View.OnClickListener{

    // request codes we use when invoking an external activity
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int GPLAY_UPDATE=3;
    private static final String TAG="SIGNIN";
    private Intent jogar, myPreferences;
    private ImageButton btnTreinar;
    private ImageButton btnRanking;
    private ImageButton btnConquistas;
    private ImageButton btnSignIn;
    private ImageButton btnSignOut;
    private SharedPreferences myPrefsFile;

    // Client used to sign in with Google APIs
    GoogleSignInClient mGoogleSignInClient;

    // Client variables
    private AchievementsClient mAchievementsClient;
    private LeaderboardsClient mLeaderboardClient;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ImageButton btnJogar = findViewById(R.id.btnJogar);
        btnJogar.setOnClickListener(this);
        btnTreinar = findViewById(R.id.btnTreinar);
        btnTreinar.setOnClickListener(this);
        ImageButton btnOpcoes =  findViewById(R.id.btnOpcoes);
        btnOpcoes.setOnClickListener(this);

        jogar = new Intent(this, Jogo.class);
        myPreferences = new Intent(this, SettingsActivity.class);

        btnRanking =  findViewById(R.id.imgBtnRanking);
        btnRanking.setOnClickListener(this);
        btnConquistas=(ImageButton) findViewById(R.id.imgBtnConquistas);
        btnConquistas.setOnClickListener(this);
        btnSignIn = findViewById(R.id.imgBtnSignIn);
        btnSignIn.setOnClickListener(this);
        btnSignOut =  findViewById(R.id.imgBtnSignOut);
        btnSignOut.setOnClickListener(this);


        // Como apagar valores do ficheiro de preferencias
        //PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();

        myPrefsFile = PreferenceManager.getDefaultSharedPreferences(this);

        // Create the client used to sign in to Google services.
        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

        /**
         DisplayMetrics dm = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(dm);
         int width = dm.widthPixels;
         int height=dm.heightPixels;
         int densityDpi = dm.densityDpi;

         Toast.makeText(getBaseContext(),Integer.toString(width)+ " " + Integer.toString(height)+" \n"+densityDpi,Toast.LENGTH_SHORT).show();
         **/

    }

    public void onStart() {
        super.onStart();
        File file = getBaseContext().getFileStreamPath(Jogo.NOME_FICHEIRO);
        if (file.exists()) {
            btnTreinar.setVisibility(View.VISIBLE);
        } else {
            btnTreinar.setVisibility(View.INVISIBLE);
        }
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        updateUI(GoogleSignIn.getLastSignedInAccount(this)!=null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtnSignIn:
                startSignInIntent();
                break;

            case R.id.imgBtnSignOut:
                signOut();
                break;

            case R.id.btnJogar:
                jogar.putExtra("treinar", Boolean.FALSE);
                startActivityForResult(jogar,GPLAY_UPDATE);
                break;

            case R.id.btnTreinar:
                jogar.putExtra("treinar", Boolean.TRUE);
                startActivity(jogar);
                updateUI(true);
                break;
            case R.id.imgBtnRanking:
                onShowLeaderboardsRequested();
                break;
            case R.id.imgBtnConquistas:
                onShowAchievementsRequested();
                break;
            case R.id.btnOpcoes:
                startActivity(myPreferences);
                break;
        }
    }

    private void updateUI (Boolean connected) {
        if (connected) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRanking.setVisibility(View.VISIBLE);
            btnConquistas.setVisibility(View.VISIBLE);

        } else{
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRanking.setVisibility(View.GONE);
            btnConquistas.setVisibility(View.GONE);
        }
    }



    private void startSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(),RC_SIGN_IN);
    }


    private void signOut() {
        Log.d(TAG, "signOut()");
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        Log.d(TAG, "signOut(): " + (successful ? "success" : "failed"));
                        if (successful) onDisconnected();
                    }
                });
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected(): connected to Google APIs");
        //Atualiza score
        if (myPrefsFile.getBoolean("LeaderChanged", false)) {
            updateLeaderboards();
        }

        if (myPrefsFile.getBoolean("UnlockAchiev", false)) {
            unlockAchievements();
        }
        updateUI(Boolean.TRUE);


    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");
        mAchievementsClient = null;
        updateUI(Boolean.FALSE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        Log.w("ERRO", "Vou dar Erro"+requestCode);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            handleSignInResult(task);
        }
        if (requestCode==GPLAY_UPDATE && GoogleSignIn.getLastSignedInAccount(getApplicationContext())!=null){
            unlockAchievements();
            updateLeaderboards();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            //GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            onConnected(completedTask.getResult(ApiException.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            String message = e.getMessage();
            if (message == null || message.isEmpty()) {
                message = getString(R.string.signin_other_error);
                new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
                onDisconnected();
            }
        }

    }


    /*Disconnect accounts
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }*/


    public void onShowLeaderboardsRequested () {
            Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .getLeaderboardIntent(getString(R.string.leaderboard_best_score))
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_LEADERBOARD_UI);
                        }
                    });
    }

    /*private void handleException(Exception e, String details) {
        int status = 0;

        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            status = apiException.getStatusCode();
        }

        String message = getString(R.string.status_exception_error, details, status, e);

        new AlertDialog.Builder(Entrada.this)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }*/


    public void onShowAchievementsRequested() {
        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                });
    }

    // Update leaderboards with the user's score.
    private void updateLeaderboards() {
        Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .submitScore(getString(R.string.leaderboard_best_score), myPrefsFile.getInt("score", 0));
        myPrefsFile.edit().putBoolean("LeaderChanged", false).apply();

    }
    // atualizar achievements
    private void unlockAchievements () {
        SharedPreferences.Editor edit = myPrefsFile.edit();
        if (myPrefsFile.getBoolean("Ac1Unlocked", Boolean.FALSE)) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.achievement_the_beginning));
            edit.putBoolean("Ac1Updated", Boolean.TRUE);
            edit.apply();
        }

        if (myPrefsFile.getBoolean("Ac2Unlocked", Boolean.FALSE)) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.achievement_dont_stop));
            edit.putBoolean("Ac2Updated", Boolean.TRUE);
            edit.apply();
        }
        if (myPrefsFile.getBoolean("Ac3Unlocked", Boolean.FALSE)) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.achievement_ten_in_each_option));
            edit.putBoolean("Ac3Updated", Boolean.TRUE);
            edit.apply();
        }
        if (myPrefsFile.getBoolean("Ac4Unlocked", Boolean.FALSE)) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.achievement_ten_in_a_row));
            edit.putBoolean("Ac4Updated", Boolean.TRUE);
            edit.apply();
        }
        if (myPrefsFile.getBoolean("Ac5Unlocked", Boolean.FALSE)) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.achievement_addicted));
            edit.putBoolean("Ac5Updated", Boolean.TRUE);
            edit.apply();
        }
    }
}