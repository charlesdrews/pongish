package com.charlesdrews.pongish.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Contracts for the view, view's activity, and presenter
 *
 * Created by charlie on 9/10/16.
 */
public interface GameContract {

    interface View {

        /**
         * Set a reference to the given Presenter in the View
         *
         * @param presenter is the instance to which a reference will be saved in the View.
         */
        void bindPresenter(@NonNull final Presenter presenter);

        /**
         * Remove the reference to the Presenter from the View
         */
        void unbindPresenter();

        /**
         * Indicate whether the View is setup and ready to draw frames.
         *
         * @return true if View is ready for game rendering, else false.
         */
        boolean isGameViewReady();

    }

    interface ViewActivity {

        /**
         * Retrieve the saved game state bundle from the Activity.
         *
         * @return the saved instance state bundle if present, else null.
         */
        Bundle getSavedGameState();

        /**
         * If game is restarted, any previously saved state should be deleted.
         */
        void clearSavedGameState();

        /**
         * Toggle the play/pause icon to show play. Call from the GameContract.View object.
         */
        void showPlayIcon();

        /**
         * Toggle the play/pause icon to show pause. Call from the GameContract.View object.
         */
        void showPauseIcon();

    }

    /**
     * The Presenter will instantiate GameEngine.Engine and GameObjects.PongScene instances. It will
     * hold references to View, ViewActivity, and GameEngine.Renderer instances.
     */
    interface Presenter {

        /**
         * Set a reference to the given View in the Presenter.
         *
         * @param view is the instance to which a reference will be saved in the Presenter.
         */
        void bindViews(@NonNull final View view, @NonNull final ViewActivity activity);

        /**
         * Remove the reference to the View instance from the Presenter.
         */
        void unbindViews();

        /**
         * Set a reference to the given Renderer in the Presenter.
         *
         * @param renderer is the instance to which a reference will be saved in the Presenter.
         */
        void bindRenderer(@NonNull final GameEngine.Renderer renderer);

        /**
         * Remove the reference to the Renderer instance from the Presenter.
         */
        void unbindRenderer();

        /**
         * Set the dimensions of the game board when the SurfaceView is changed.
         */
        void setGameBoardDimensions(final int width, final int height);

        /**
         * Save all necessary game state data to the provided bundle.
         *
         * @param gameStateBundle is the bundle to which the data will be saved.
         */
        void saveGameStateToBundle(@NonNull final Bundle gameStateBundle);

        /**
         * When the View is ready to draw the game, do 1 of 3 things:
         *   1. If game state still in memory in Presenter, pause game & redraw last frame
         *   2. If game state was saved in a Bundle, restore it, pause game, & redraw last frame
         *   3. Otherwise, initiate a new game & start the game rendering loop
         */
        void onGameViewReady(@Nullable final Bundle savedGameStateBundle);

        /**
         * Pause the game play on activity pause.
         */
        void onActivityPause();

        /**
         * Resume the game rendering.
         */
        void onPlayButtonClick();

        /**
         * Pause the game rendering.
         */
        void onPauseButtonClick();

        /**
         * Quit current game, reset game state, then start a new game.
         */
        void onRestartButtonClick();

        /**
         * Communicate the change in left paddle position to the PongScene.
         *
         * @param deltaY the change in position requested by the user since the last event.
         */
        void onLeftSidePointerMove(float deltaY);

        /**
         * Communicate the change in right paddle position to the PongScene.
         *
         * @param deltaY the change in position requested by the user since the last event.
         */
        void onRightSidePointerMove(float deltaY);
    }
}
