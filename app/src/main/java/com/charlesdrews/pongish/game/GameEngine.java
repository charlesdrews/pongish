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
         * Draw a vertical line in the game area.
         *
         * @param x is the x coordinate of the vertical line.
         * @param topY is the y coordinate of the top of the line.
         * @param bottomY is the y coordinate of the bottom of the line.
         * @param color is the color of the line as an int.
         * @param dashed indicates whether line should be solid (false) or dashed (true).
         */
        void drawVerticalLine(float x, float topY, float bottomY, int color, boolean dashed);

        /**
         * Draw a player's score on the game board.
         * @param scoreText is the score to be displayed.
         * @param x is the left x coordinate of the text if rightAlign is false, else the right x
         *          coordinate of the text.
         * @param topY is the y coordinate for the TOP of the text.
         * @param textSize is the height of the text.
         * @param color is the color of the text as an int.
         * @param rightAlign indicates whether the text should be right or left aligned.
         */
        void drawScore(@NonNull String scoreText, float x, float topY, float textSize, int color,
                       boolean rightAlign);

        /**
         * Draw a circle in the game area.
         * beginDrawing MUST be called prior, and commitDrawing() MUST be called after.
         *
         * @param centerX is the x coordinate of the circle's center.
         * @param centerY is the y coordinate of the circle's center.
         * @param radius of the circle in pixels
         * @param color of the circle as an int
         */
        void drawCircle(float centerX, float centerY, float radius, int color);

        /**
         * Draw a rectangle in the game area.
         * beginDrawing MUST be called prior, and commitDrawing() MUST be called after.
         *
         * @param leftX is the x coordinate of the rectangle's left edge.
         * @param topY is the y coordinate of the rectangles's top edge.
         * @param rightX is the x coordinate of the rectangle's right edge.
         * @param bottomY is the y coordinate of the rectangle's bottom edge.
         * @param color of the rectangle as an int.
         */
        void drawRect(float leftX, float topY, float rightX, float bottomY, int color);

        /**
         * Draw a countdown number in the center of the screen.
         *
         * @param countDownText is the text to display.
         * @param textSize is the desired text size.
         * @param textColor is the textColor of the text as an int.
         * @param backgroundColor is the color, as an int, to show behind the text.
         */
        void drawCountDown(@NonNull String countDownText, float textSize, int textColor,
                           int backgroundColor);

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
        void drawFramesPerSecond(@NonNull String fpsText, float x, float y, float textSize,
                                 int color);
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
    interface RectangleToRender {
        float getLeftX();
        float getTopY();
        float getRightX();
        float getBottomY();
        int getColor();
    }

    /**
     * Simple interface any vertical line in the game must implement in order to be drawn on the screen.
     */
    interface VerticalLineToRender {
        float getX();
        float getTopY();
        float getBottomY();
        int getColor();
        boolean isDashed();
    }

    interface ScoreToRender {
        String getScoreText();
        float getX();
        float getTopY();
        float getTextSize();
        int getColor();
        boolean isRightAligned();
    }
}
