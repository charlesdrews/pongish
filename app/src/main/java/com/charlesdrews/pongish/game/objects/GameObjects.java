package com.charlesdrews.pongish.game.objects;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.charlesdrews.pongish.game.GameEngine;

import java.util.List;

/**
 * Contracts for the objects that will appear in the game
 *
 * Created by charlie on 9/10/16.
 */
public interface GameObjects {

    /**
     * The PongScene is a container for the other game objects (paddles, balls) and will include the
     * logic to detect and handle collisions between game objects.
     */
    interface Scene extends Parcelable {

        int LEFT_PADDLE = 0;
        int RIGHT_PADDLE = 1;
        int NEITHER_PADDLE = 2;
        int BOTH_PADDLES = 3;

        float NO_PADDLE_HIT = -2f;

        int NO_WALL_HIT = 0;
        int LEFT_WALL_HIT = 1;
        int RIGHT_WALL_HIT = 2;

        /**
         * Move the specified paddle by the specified amount.
         *
         * @param paddle must be PongScene.LEFT_PADDLE (0), or PongScene.RIGHT_PADDLE (1).
         * @param deltaY is the amount to move up (negative) or down (positive).
         */
        void movePaddle(final int paddle, final float deltaY, final long millisSinceLastUpdate);

        /**
         * Move all game objects the distance they should travel in the specified amount of time.
         *
         * @param millisSinceLastUpdate is the time delta for the movement.
         * @return true if a point was scored and the game loop needs to pause, else false.
         */
        boolean updateGameObjects(final long millisSinceLastUpdate);

        /**
         * Retrieve the background color to use for this Scene.
         * @return an int representation of the background color.
         */
        int getBackgroundColor();

        /**
         * Retrieve a List of scores for the Renderer to draw as text.
         *
         * @return the scores included in the Scene.
         */
        List<GameEngine.ScoreToRender> getScoresToRender();

        /**
         * Retrieve a List of circles for the Renderer to draw.
         *
         * @return the circles included in the Scene.
         */
        List<GameEngine.CircleToRender> getCirclesToRender();

        /**
         * Retrieve a list of rectangles for the Renderer to draw.
         *
         * @return the rectangles included in the Scene.
         */
        List<GameEngine.RectangleToRender> getRectanglesToRender();

        /**
         * Retrieve a list of vertical lines for the Renderer to draw.
         *
         * @return the vertical lines includes in the Scene.
         */
        List<GameEngine.VerticalLineToRender> getVerticalLinesToRender();

        /**
         * Start a new normal game ball after a point is scored.
         */
        void resetAfterPointScored();

        /**
         * Notify the Scene if the game engine has initiated a countdown before game play. Use
         * this value to determine whether or not to allow paddle movements.
         *
         * @param countdownInProgress indicates whether a countdown is currently in progress.
         */
        void setCountdownInProgress(boolean countdownInProgress);
    }

    interface Score extends GameEngine.ScoreToRender, Parcelable {
        void incrementScoreByOne();
        void setScore(int score);
    }

    /**
     * Paddles will appear on the left and right of the game board and will reflect balls.
     */
    interface Paddle extends GameEngine.RectangleToRender, Parcelable {

        /**
         * Tell the caller whether this paddle is controlled by the computer.
         *
         * @return true if paddle is computer controlled, else false.
         */
        boolean isComputerControlled();

        /**
         * Move up (negative deltaY) or down (positive deltaY) and ensure paddle stays on screen.
         * The Paddle's top y cannot be < 0 and the bottom y cannot be > gameBoardHeight. Also,
         * ensure the movement does not exceed the maximum speed of the paddle.
         *
         * @param deltaY is the number of pixels to move up (negative) or down (positive).
         * @param gameBoardHeight is the maximum allowable y value for the game/scene.
         * @param millisecondsSinceLastUpdate will determine how far it is possible for the paddle
         *                                    to move, based on it's maximum speed.
         */
        void move(float deltaY, final float gameBoardHeight, final long millisecondsSinceLastUpdate);

        /**
         * Determine whether the specified ball has collided with the Paddle, and if so,
         * return a value indicating where on the paddle it collided.
         *
         * @param ball is the game ball to be compared against this Paddle.
         * @return between -1.0 and 1.0, with 1.0 representing a collision at the exact top of the
         * paddle, 0.0 indicating a collision with the exact center of the paddle, and -1.0
         * representing a collision with the exact bottom of the paddle, and proportional values
         * for positions in between. If no collision detected, will return
         * PongScene.NO_PADDLE_HIT (-2f).
         */
        float getRelativeCollisionLocation(@NonNull final Ball ball);

        /**
         * Retrieve the y coordinate of the center of the paddle.
         * @return the center y coordinate.
         */
        float getCenterY();
    }

    /**
     * One or more balls will move across the game board at a given time, bouncing off the top and
     * bottom walls as well as the paddles.
     */
    interface Ball extends GameEngine.CircleToRender, Parcelable {

        /**
         * Update the ball's position based on it's direction and the specified change in time.
         *
         * @param millisecondsSinceLastUpdate is the time delta for the ball's movement.
         * @param gameBoardHeight
         */
        void move(final long millisecondsSinceLastUpdate, final float gameBoardHeight);

        /**
         * Determine whether the ball has hit either the left or right side walls.
         *
         * @param gameBoardWidth is the width of the game board in pixels.
         * @param gameBoardHorizontalMargin is the width of the thumb margin in pixels.
         * @return PongScene.NO_WALL_HIT (0), PongScene.LEFT_WALL_HIT (1),
         * or PongScene.RIGHT_WALL_HIT (2).
         */
        int checkIfPointScored(final float gameBoardWidth, final float gameBoardHorizontalMargin);

        /**
         * Increase or decrease speed by the given percentage, depending on whether it is +/-.
         *
         * @param percentChangeInBallSpeed is the desired % change in speed, either + or -.
         */
        void changeSpeed(final float percentChangeInBallSpeed);

        /**
         * Update the Ball's direction to the specified degrees.
         *
         * @param directionInDegrees is the desired direction with 0° = up, 90° = right,
         *                           -90° = left, and 180°/-180° = down.
         */
        void setDirection(double directionInDegrees);

        /**
         * Update the Ball's color to the specified color.
         *
         * @param color is the int representation of the desired color.
         */
        void setColor(int color);

        /**
         * Retrieve the ball's current direction, with 0 = up, positive = rightward, etc.
         * @return the direction in degrees.
         */
        double getDirection();
    }

    /**
     * Describes direction in degrees as 0° = up, 90° = right, 180°/-180° = down, -90° = left.
     * This is counter to the usual definition of 0° = right, but will make it easier to tell if
     * a ball is moving right (positive degrees, 0°  to 180°  exclusive) or left (negative degrees,
     * 0° to -180°  exclusive). Also provide conversion to radians for trigonometric calculations.
     */
    interface Direction extends Parcelable {

        /**
         * Set the direction to the specified value. If abs(value) > 180° then normalize to the
         * equivalent value within the allowed range. Also, do not allow exactly 0°, since that
         * would make the game play impossible.
         *
         * @param degrees should be between -180° and 180°, with 0° = up, negative = left, and
         *                positive = right.
         */
        void setDirectionInDegrees(final double degrees);

        /**
         * @return the current direction in degrees.
         */
        double getDirectionInDegrees();

        /**
         * Get the standard radians representation of the angle for use in trigonometry. Convert
         * the degrees to the standard 0° = right orientation, then use Math.toRadians().
         *
         * @return the standard radians representation of the direction.
         */
        double getDirectionInRadians();
    }

    /**
     * Vertical lines will be used to mark the left and right edges of the game board, as well as
     * the center.
     */
    interface VerticalLine extends GameEngine.VerticalLineToRender, Parcelable {

        /**
         * Change the line's color.
         * @param color is the new color as an int.
         */
        void setColor(int color);
    }
}
