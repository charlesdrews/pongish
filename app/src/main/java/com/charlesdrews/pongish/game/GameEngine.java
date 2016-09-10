package com.charlesdrews.pongish.game;

import android.support.annotation.NonNull;

import com.charlesdrews.pongish.game.objects.GameObjects;

/**
 * Contracts for the game engine and the game renderer
 *
 * Created by charlie on 9/10/16.
 */
public interface GameEngine {

    /**
     * Provides the functionality to control and manage the game execution loop.
     */
    interface Engine extends Runnable {

        /**
         * Bind a reference to the given Renderer to the Engine.
         *
         * @param renderer is the instance of Renderer to bind to the Engine.
         */
        void bindRenderer(@NonNull Renderer renderer);

        /**
         * Unbind the Renderer from the Engine by removing the reference.
         */
        void unbindRenderer();

        /**
         * Set the PongScene object upon which the Engine will act.
         *
         * @param scene is the PongScene instance the Engine will update and render.
         */
        void setScene(@NonNull GameObjects.Scene scene);

        /**
         * Retrieve the rendering time of the last frame in milliseconds for use by the Presenter
         * in updating the Paddle positions in response to user drag motions.
         *
         * @return the number of milliseconds required to update and draw the last frame.
         */
        long getLastFrameRenderTimeInMillis();

        /**
         * Create a new thread and initiate the run() method and its update/draw loop on the thread.
         */
        void startGameExecution();

        /**
         * Halt execution of the update/draw loop in run() and join the thread so it can complete.
         */
        void stopGameExecution();

        /**
         * Draw one frame using the current positions of all game objects.
         */
        void drawFrame();

        /**
         * Provide the main game update/draw loop, which will run in a separate thread.
         */
        @Override
        void run();
    }

    /**
     * Provides the functionality to draw game objects to the screen.
     */
    interface Renderer {

        /**
         * MUST be called before any of the draw___() methods for a given frame of animation
         *
         * @return true if surface is ready for drawing, else false
         */
        boolean beginDrawing();

        /**
         * MUST be called after all the draw___() methods for a given frame of animation
         */
        void commitDrawing();

        /**
         * Fill in the background color for the game area.
         * beginDrawing MUST be called prior, and commitDrawing() MUST be called after.
         *
         * @param color of the background as an int
         */
        void drawBackground(int color);

        /**
         * Draw a circle in the game area.
         * beginDrawing MUST be called prior, and commitDrawing() MUST be called after.
         *
         * @param x coordinate of the circle's center
         * @param y coordinate of the circle's center
         * @param radius of the circle in pixels
         * @param color of the circle as an int
         */
        void drawCircle(float centerX, float centerY, float radius, int color);

        /**
         * Draw a rectangle in the game area.
         * beginDrawing MUST be called prior, and commitDrawing() MUST be called after.
         *
         * @param left x coordinate of the rectangle
         * @param top y coordinate of the rectangle
         * @param right x coordinate of the rectangle
         * @param bottom y coordinate of the rectangle
         * @param color of the rectangle as an int
         */
        void drawRect(float leftX, float topY, float rightX, float bottomY, int color);

        /**
         * Draw the frames per second rate as text in the game area.
         * beginDrawing MUST be called prior, and commitDrawing() MUST be called after.
         *
         * @param fpsText a String of the FPS text
         * @param x coordinate of the top-left text corner
         * @param y coordinate of the top-left text corner
         * @param textSize in pixels
         * @param color of the text as an int
         */
        void drawFramesPerSecond(@NonNull String fpsText, float x, float y, float textSize, int color);
    }

    /**
     * Simple interface any circular game object must implement in order to be drawn to the screen.
     */
    interface CircleToRender {
        float getCenterX();
        float getCenterY();
        float getRadius();
        int getColor();
    }

    /**
     * Simple interface any rectangular game object must implement in order to be drawn to the screen.
     */
    interface RectToRender {
        float getLeftX();
        float getTopY();
        float getRightX();
        float getBottomY();
        int getColor();
    }
}
