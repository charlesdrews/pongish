package com.charlesdrews.pongish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.View;

import com.charlesdrews.pongish.game.PongActivity;
import com.charlesdrews.pongish.game.objects.GameObjects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String VERSION_UPDATE_DIALOG_SHOWN_KEY = "v1.1_dialog_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.two_player_game_button).setOnClickListener(this);
        findViewById(R.id.one_player_right_game_button).setOnClickListener(this);
        findViewById(R.id.one_player_left_game_button).setOnClickListener(this);
        findViewById(R.id.demo_mode_button).setOnClickListener(this);

        // Show version update dialog, but only if it hasn't already been shown.
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean versionUpdateDialogShown = prefs.getBoolean(VERSION_UPDATE_DIALOG_SHOWN_KEY, false);

        if (!versionUpdateDialogShown) {
            launchVersionUpdateDialog();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(VERSION_UPDATE_DIALOG_SHOWN_KEY, true);
            editor.apply();
        }
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(MainActivity.this, PongActivity.class);

        switch (view.getId()) {

            case R.id.two_player_game_button:
                intent.putExtra(PongActivity.COMPUTER_CONTROLLED_PADDLE_KEY,
                        GameObjects.Scene.NEITHER_PADDLE);
                break;

            case R.id.one_player_right_game_button:
                intent.putExtra(PongActivity.COMPUTER_CONTROLLED_PADDLE_KEY,
                        GameObjects.Scene.LEFT_PADDLE);
                break;

            case R.id.one_player_left_game_button:
                intent.putExtra(PongActivity.COMPUTER_CONTROLLED_PADDLE_KEY,
                        GameObjects.Scene.RIGHT_PADDLE);
                break;

            case R.id.demo_mode_button:
                intent.putExtra(PongActivity.COMPUTER_CONTROLLED_PADDLE_KEY,
                        GameObjects.Scene.BOTH_PADDLES);
        }

        startActivity(intent);
    }

    private void launchVersionUpdateDialog() {
        String title = String.format(getString(R.string.version_update_dialog_title),
                getString(R.string.version));

        String bodyHtml = getString(R.string.version_update_dialog_body);
        Spanned message;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            message = Html.fromHtml(bodyHtml, 0);
        }
        else {
            message = Html.fromHtml(bodyHtml);
        }

        String positiveButtonText = getString(R.string.version_update_dialog_positive_button_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, null);

        builder.show();
    }
}
