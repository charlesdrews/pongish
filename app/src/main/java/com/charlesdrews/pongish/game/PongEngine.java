package com.charlesdrews.pongish.game;

import android.support.annotation.NonNull;
import android.util.Log;

import com.charlesdrews.pongish.game.objects.GameObjects;

/**
 * Provide game engine functionality, including the main update/draw loop.
 *
 * Created by charlie on 9/10/16.
 */
public class PongEngine implements GameEngine.Engine {

    private static final String TAG = "PongEngine";
    

    // ================================== Member variables =====================================

    private GameEngine.Renderer mRenderer;
    private GameObjects.Scene mScene;

    private Thread mGameThread;
    private long mLastFrameRenderTimeInMillis;
    private boolean mPlaying;


    // ==================================== Constructor =========================================

    public PongEngine() {
        //TODO

    }


    // ============================== GameEngine.Engine methods ==================================
    @Override
    public void bindRenderer(@NonNull GameEngine.Renderer renderer) {
        mRenderer = renderer;
    }

    @Override
    public void unbindRenderer() {
        mRenderer = null;
    }

    @Override
    public void setScene(@NonNull GameObjects.Scene scene) {
        mScene = scene;
    }

    @Override
    public void startGameExecution() {
        mPlaying = true;

        mGameThread = new Thread(this);
        mGameThread.start();
    }

    @Override
    public void stopGameExecution() {
        mPlaying = false;

        if (mGameThread != null) {
            try {
                mGameThread.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "Error joining game thread", e);
            }
        }
    }

    @Override
    public void drawFrame() {
        //TODO

    }

    @Override
    public void run() {
        while (mPlaying) {

            // Save frame render start time
            long renderStartTimeInMillis = System.currentTimeMillis();

            // Update item positions. Use the last frame's rendering time as an estimate for how
            // long it will take to render this frame.
            mScene.updateGameObjectPositions(mLastFrameRenderTimeInMillis);

            // Draw the frame
            drawFrame();

            // Track frame rendering time
            mLastFrameRenderTimeInMillis = System.currentTimeMillis() - renderStartTimeInMillis;
        }
    }
}
