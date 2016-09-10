package com.charlesdrews.pongish.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Provide data to the views and handle user touch events and activity lifecycle events.
 *
 * Created by charlie on 9/10/16.
 */
public class GamePresenter implements GameContract.Presenter {

    // ===================================== Member variables ====================================

    private GameContract.View mGameView;
    private GameContract.ViewActivity mViewActivity;
    private GameEngine.Engine mEngine;
    private GameEngine.Renderer mRenderer;

    private int mGameBoardWidth = 0;
    private int mGameBoardHeight = 0;

    // ====================================== Constructor ========================================

    public GamePresenter() {
        //TODO

    }


    // ============================== GameContract.Presenter methods =============================

    @Override
    public void bindViews(@NonNull GameContract.View view,
                          @NonNull GameContract.ViewActivity activity) {
        mGameView = view;
        mViewActivity = activity;
    }

    @Override
    public void unbindViews() {
        mGameView = null;
        mViewActivity = null;
    }

    @Override
    public void bindRenderer(@NonNull GameEngine.Renderer renderer) {
        mRenderer = renderer;
    }

    @Override
    public void unbindRenderer() {
        mRenderer = null;
    }

    @Override
    public void setGameBoardDimensions(int width, int height) {
        mGameBoardWidth = width;
        mGameBoardHeight = height;
    }

    @Override
    public void saveGameStateToBundle(@NonNull Bundle gameStateBundle) {
        //TODO

    }

    @Override
    public void onGameViewReady(@Nullable Bundle savedGameStateBundle) {
        //TODO

    }

    @Override
    public void onActivityPause() {
        //TODO

    }

    @Override
    public void onPlayButtonClick() {
        //TODO

    }

    @Override
    public void onPauseButtonClick() {
        //TODO

    }

    @Override
    public void onRestartButtonClick() {
        //TODO

    }

    @Override
    public boolean onMotionEventDown(float x, float y) {
        //TODO
        return false;
    }

    @Override
    public void onMotionEventMove(float newYCoordinate) {
        //TODO

    }

    @Override
    public void onMotionEventUp() {
        //TODO

    }
}
