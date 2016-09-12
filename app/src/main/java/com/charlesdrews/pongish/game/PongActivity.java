package com.charlesdrews.pongish.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.charlesdrews.pongish.R;
import com.charlesdrews.pongish.game.objects.GameObjects;

public class PongActivity extends AppCompatActivity implements GameContract.ViewActivity,
        View.OnClickListener {

    public static final String COMPUTER_CONTROLLED_PADDLE_KEY = "computer_controlled_paddle_key";


    // ==================================== Member variables =====================================

    private GameContract.Presenter mPresenter;
    private GameContract.View mGameView;

    private boolean mBindingsEstablished = false;
    private Bundle mSavedGameState;
    private ImageView mPlayPauseButton;
    private boolean mPlayIconShown = false;


    // =============================== Activity lifecycle methods ================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pong);

        // Keep a reference to savedInstanceState so it can be retrieved later.
        mSavedGameState = savedInstanceState;

        // Instantiate the presenter and give it a reference to this ViewActivity.
        mPresenter = new PongPresenter(getIntent()
                .getIntExtra(COMPUTER_CONTROLLED_PADDLE_KEY, GameObjects.Scene.NEITHER_PADDLE));
        mPresenter.bindViewActivity(this);

        // Give the presenter a reference to the game view, which is pulling double duty as
        // both GameContract.View and as GameEngine.Renderer.
        mGameView = (PongView) findViewById(R.id.game_view);
        mPresenter.bindRenderer((GameEngine.Renderer) mGameView);

        // Give the game view a reference to the presenter.
        mGameView.bindPresenter(mPresenter);

        mBindingsEstablished = true;

        // Set up play/pause and restart buttons
        mPlayPauseButton = (ImageView) findViewById(R.id.play_pause_button);
        mPlayPauseButton.setOnClickListener(this);

        ImageView restart = (ImageView) findViewById(R.id.restart_button);
        restart.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If returning after activity was paused, re-establish bindings
        if (!mBindingsEstablished) {
            mPresenter.bindViewActivity(this);
            mPresenter.bindRenderer((GameEngine.Renderer) mGameView);
            mGameView.bindPresenter(mPresenter);
            mBindingsEstablished = true;
        }

        // If GameView is already setup, notify presenter.
        // Otherwise, GameView will notify presenter when it's ready.
        if (mGameView.isGameViewReady()) {
            mPresenter.onGameViewReady(mSavedGameState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPresenter.onActivityPause();

        mPresenter.unbindViewActivity();
        mPresenter.unbindRenderer();
        mGameView.unbindPresenter();
        mBindingsEstablished = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Have the presenter put the necessary game state data into the bundle.
        mPresenter.saveGameStateToBundle(outState);

        super.onSaveInstanceState(outState);
    }


    // =========================== GameContract.ViewActivity methods =============================

    @Override
    public Bundle getSavedGameState() {
        return mSavedGameState;
    }

    @Override
    public void clearSavedGameState() {
        mSavedGameState = null;
    }

    @Override
    public void showPlayIcon() {
        mPlayPauseButton.setImageResource(R.drawable.play_icon);
        mPlayIconShown = true;
    }

    @Override
    public void showPauseIcon() {
        mPlayPauseButton.setImageResource(R.drawable.pause_icon);
        mPlayIconShown = false;
    }


    // ============================= View.OnClickListener method =================================

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_pause_button: {
                if (mPlayIconShown) {
                    mPresenter.onPlayButtonClick();
                }
                else {
                    mPresenter.onPauseButtonClick();
                }
                break;
            }
            case R.id.restart_button: {
                mPresenter.onRestartButtonClick();
                break;
            }
        }
    }
}
