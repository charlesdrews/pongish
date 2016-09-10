package com.charlesdrews.pongish.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.charlesdrews.pongish.game.objects.GameObjects;
import com.charlesdrews.pongish.game.objects.PongScene;

/**
 * Provide data to the views and handle user touch events and activity lifecycle events.
 *
 * Created by charlie on 9/10/16.
 */
public class PongPresenter implements GameContract.Presenter {

    private static final String TAG = "PongPresenter";

    private static final String SCENE_PARCEL_KEY = "scene_parcel_key";


    // ===================================== Member variables ====================================

    private GameContract.View mGameView;
    private GameContract.ViewActivity mViewActivity;
    private GameEngine.Renderer mRenderer;
    private GameEngine.Engine mEngine;
    private GameObjects.Scene mScene;

    private int mGameBoardWidth = 0;
    private int mGameBoardHeight = 0;

    // ====================================== Constructor ========================================

    public PongPresenter() {

        mEngine = new PongEngine();

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
        mEngine.bindRenderer(mRenderer);
    }

    @Override
    public void unbindRenderer() {
        mRenderer = null;
        mEngine.unbindRenderer();
    }

    @Override
    public void setGameBoardDimensions(int width, int height) {
        mGameBoardWidth = width;
        mGameBoardHeight = height;
    }

    @Override
    public void saveGameStateToBundle(@NonNull Bundle gameStateBundle) {
        gameStateBundle.putParcelable(SCENE_PARCEL_KEY, mScene);

        //TODO - anything else to save?
    }

    @Override
    public void onGameViewReady(@Nullable Bundle savedGameStateBundle) {

        // Make sure game not running until we want it to
        mEngine.stopGameExecution();

        // If game state still in memory in Presenter, pause game & redraw last frame
        if (mScene != null) {
            Log.d(TAG, "onGameViewReady: game still in memory");
            mEngine.setScene(mScene);
            mEngine.drawFrame();
        }
        // If game state was saved in a Bundle, restore it, pause game, & redraw last frame
        else if (savedGameStateBundle != null) {
            Log.d(TAG, "onGameViewReady: game saved in bundle");
            mScene = savedGameStateBundle.getParcelable(SCENE_PARCEL_KEY);
            if (mScene != null) {
                Log.d(TAG, "onGameViewReady: scene successfully retrieved from bundle");
                mEngine.setScene(mScene);
                mEngine.drawFrame();
            }
        }
        // Otherwise, initiate a new game & start the game rendering loop
        else {
            mScene = new PongScene(mGameBoardWidth, mGameBoardHeight);
            mEngine.setScene(mScene);
            mEngine.startGameExecution();
        }
    }

    @Override
    public void onActivityPause() {
        mEngine.stopGameExecution();
    }

    @Override
    public void onPlayButtonClick() {
        mEngine.startGameExecution();
    }

    @Override
    public void onPauseButtonClick() {
        mEngine.stopGameExecution();
    }

    @Override
    public void onRestartButtonClick() {

        // Stop the current game and clear any saved state
        mEngine.stopGameExecution();
        mViewActivity.clearSavedGameState();

        // Initialize and start a new game
        mScene = new PongScene(mGameBoardWidth, mGameBoardHeight);
        mEngine.setScene(mScene);
        mEngine.startGameExecution();
    }

    @Override
    public void onLeftSidePointerMove(float deltaY) {
        mScene.movePaddle(GameObjects.Scene.LEFT_PADDLE, deltaY,
                mEngine.getLastFrameRenderTimeInMillis());
    }

    @Override
    public void onRightSidePointerMove(float deltaY) {
        mScene.movePaddle(GameObjects.Scene.RIGHT_PADDLE, deltaY,
                mEngine.getLastFrameRenderTimeInMillis());
    }
}