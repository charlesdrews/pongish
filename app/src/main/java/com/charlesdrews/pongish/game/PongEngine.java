package com.charlesdrews.pongish.game;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.charlesdrews.pongish.game.objects.GameObjects;

import java.util.Locale;

/**
 * Provide game engine functionality, including the main update/draw loop.
 *
 * Created by charlie on 9/10/16.
 */
public class PongEngine implements GameEngine.Engine {

    // ==================================== Constants ============================================
    private static final String TAG = "PongEngine";

    private static final String FPS_TEMPLATE = "FPS: %d";
    private static final int FPS_TEXT_COLOR = Color.WHITE;
    private static final float FPS_TEXT_SIZE = 40f;
    private static final float FPS_X_COORDINATE = 40f;
    private static final float FPS_Y_COORDINATE = 80f;


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
    public long getLastFrameRenderTimeInMillis() {
        return mLastFrameRenderTimeInMillis;
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

        // Lock the canvas. If not successful, do not proceed.
        if (!mRenderer.beginDrawing()) {
            Log.d(TAG, "drawFrame: unable to lock canvas!");
            return;
        }

        // Wipe everything by re-drawing background color
        mRenderer.drawBackground(mScene.getBackgroundColor());

        // Draw each game item
        for (GameEngine.CircleToRender circle : mScene.getCirclesToRender()) {
            mRenderer.drawCircle(circle.getCenterX(), circle.getCenterY(), circle.getRadius(),
                    circle.getColor());
        }

        for (GameEngine.RectToRender rect : mScene.getRectsToRender()) {
            mRenderer.drawRect(rect.getLeftX(), rect.getTopY(), rect.getRightX(), rect.getBottomY(),
                    rect.getColor());
        }

        // Draw frames per second text
        long framesPerSecond = 0L;
        if (mLastFrameRenderTimeInMillis > 0L) {
            framesPerSecond = 1_000L / mLastFrameRenderTimeInMillis;
        }

        mRenderer.drawFramesPerSecond(
                String.format(Locale.getDefault(), FPS_TEMPLATE, framesPerSecond),
                FPS_X_COORDINATE, FPS_Y_COORDINATE, FPS_TEXT_SIZE, FPS_TEXT_COLOR);

        // Unlock canvas and post drawings
        mRenderer.commitDrawing();
    }

    @Override
    public void run() {
        while (mPlaying) {

            // Save frame render start time
            long renderStartTimeInMillis = System.currentTimeMillis();

            // Update item positions. Use the last frame's rendering time as an estimate for how
            // long it will take to render this frame.
            boolean pointScored = mScene.updateGameObjectPositions(mLastFrameRenderTimeInMillis);

            // Draw the frame
            drawFrame();

            // Track frame rendering time
            mLastFrameRenderTimeInMillis = System.currentTimeMillis() - renderStartTimeInMillis;

            // If a point was scored, pause the game temporarily
            if (pointScored) {
                Log.d(TAG, "run: point scored");
                mScene.resetAfterPointScored();

                try {
                    Log.d(TAG, "run: sleeping...");
                    Thread.sleep(2_000L);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Exception while sleeping game loop in run() after point scored", e);
                }
            }
        }
    }
}
