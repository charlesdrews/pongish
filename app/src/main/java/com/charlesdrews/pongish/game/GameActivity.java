package com.charlesdrews.pongish.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.charlesdrews.pongish.R;

public class GameActivity extends AppCompatActivity {

    private GameContract.Presenter mPresenter;
    private GameContract.View mView;

    private boolean mBindingsEstablished = false;
    private Bundle mSavedGameState;
    private ImageView mPlayPauseButton;
    private boolean mPlayIconShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
