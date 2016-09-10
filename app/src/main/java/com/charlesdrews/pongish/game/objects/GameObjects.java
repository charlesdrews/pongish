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
     * The Scene is a container for the other game objects (paddles, balls) and will include the
     * logic to detect and handle collisions between game objects.
     */
    interface Scene extends Parcelable {

        int NO_COLLISION = 0;
        int LEFT_WALL_COLLISION = 1;
        int RIGHT_WALL_COLLISION = 2;

        /**
         * Move all game objects the distance they should travel in the specified amount of time.
         *
         * @param millisecondsSinceLastUpdate is the time delta for the movement.
         */
        void updateGameObjectPositions(final long millisecondsSinceLastUpdate);

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
        List<GameEngine.RectToRender> getRectsToRender();

    }

    interface Paddle extends GameEngine.RectToRender, Parcelable {

        /**
         * Move up (negative deltaY) or down (positive deltaY) and ensure paddle stays on screen.
         * The Paddle's top y cannot be < 0 and the bottom y cannot be > maxY. Also, ensure the
         * movement does not exceed the maximum speed of the paddle.
         *
         * @param deltaY is the number of pixels to move up (negative) or down (positive).
         * @param maxY is the maximum allowable y value for the game/scene.
         * @param millisecondsSinceLastUpdate will determine how far it is possible for the paddle
         *                                    to move, based on it's maximum speed.
         */
        void move(final float deltaY, final float maxY, final long millisecondsSinceLastUpdate);

        /**
         * Determine whether the specified ball has collided with the Paddle, and if so,
         * return a value indicating where on the paddle it collided.
         *
         * @param ball is the game ball to be compared against this Paddle.
         * @return between -1.0 and 1.0, with 1.0 representing a collision at the exact top of the
         * paddle, 0.0 indicating a collision with the exact center of the paddle, and -1.0
         * representing a collision with the exact bottom of the paddle, and proportional values
         * for positions in between. If no collision detected, will return Scene.NO_COLLISION (-2f).
         */
        float getRelativeCollisionLocation(@NonNull final Ball ball);
    }

    interface Ball extends GameEngine.CircleToRender, Parcelable {

        /**
         * Update the ball's position based on it's direction and the specified change in time.
         *
         * @param millisecondsSinceLastUpdate is the time delta for the ball's movement.
         */
        void move(final long millisecondsSinceLastUpdate);

        /**
         * Determine whether the ball has hit either the left or right side walls.
         *
         * @param rightWallXCoordinate is equivalent to the game/scene's width.
         * @return Scene.NO_COLLISION (0), Scene.LEFT_WALL_COLLISION (1),
         * or Scene.RIGHT_WALL_COLLISION (2).
         */
        int checkIfHitSideWall(final float rightWallXCoordinate);

        /**
         * Increase or decrease speed by the given amount, depending on whether it is +/-.
         *
         * @param deltaSpeedInPxPerSecond is the desired change in speed, either + or -.
         */
        void changeSpeed(final float deltaSpeedInPxPerSecond);

        /**
         * Update the Ball's direction to the specified degrees.
         *
         * @param directionInDegrees is the desired direction with 0° = up, 90° = right,
         *                           -90° = left, and 180°/-180° = down.
         */
        void setDirection(double directionInDegrees);
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
        void setDirectionInDegrees(double degrees);

        /**
         * Get the standard radians representation of the angle for use in trigonometry. Convert
         * the degrees to the standard 0° = right orientation, then use Math.toRadians().
         *
         * @return the standard radians representation of the direction.
         */
        double getDirectionInRadians();
    }
}
