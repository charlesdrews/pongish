package com.charlesdrews.pongish.game.objects;

import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.Random;

/**
 * Models a paddle that can move up and down vertically, but cannot move horizontally. Can be
 * located at the left or the right side of the screen, and can reflect balls that collide with it.
 *
 * Created by charlie on 9/10/16.
 */
public class PongPaddle implements GameObjects.Paddle {

    // ===================================== Constants ==========================================

    private static final float DEFAULT_OUTSIDE_MARGIN = 20f;
    private static final float MAXIMUM_HUMAN_PADDLE_SPEED_IN_PX_PER_MS = 1_000f;
    private static final float MAXIMUM_COMPUTER_PADDLE_SPEED_IN_PX_PER_MS = 0.75f;

    private static final float COMPUTER_PADDLE_EXTRA_ABS_VALUE = 0.3f;

    private static Random sRandom = new Random();


    // ================================= Member variables =======================================

    private boolean mComputerControlled;
    private int mPaddlePosition, mColor;
    private float mLeftX, mTopY, mRightX, mBottomY;
    private float mMaxSpeedInPxPerMs;


    // =================================== Constructor ==========================================

    /**
     * Instantiate a new Paddle and set its position at the left or right of the game board.
     *
     * @param paddlePosition must be GameObjects.Scene.LEFT_PADDLE or
     *                       GameObjects.Scene.RIGHT_PADDLE.
     * @param paddleHeight is the height in pixels of the paddle's desired height.
     * @param gameBoardWidth is the width in pixels of the game board.
     * @param gameBoardHeight is the height in pixels of the game board.
     * @param gameBoardHorizontalMargin is the width in pixels of the space on either side of the
     *                                  game board for the user's thumbs.
     * @param paddleColor is an int representation of the paddle's desired color.
     */
    public PongPaddle(final boolean computerControlled, final int paddlePosition,
                      final float paddleWidth, final float paddleHeight,
                      final float gameBoardWidth, final float gameBoardHeight,
                      final float gameBoardHorizontalMargin, final int paddleColor) {

        // Set left and right coordinates based on paddle type, or throw exception if invalid type
        if (paddlePosition == GameObjects.Scene.LEFT_PADDLE) {
            mPaddlePosition = paddlePosition;
            mLeftX = gameBoardHorizontalMargin + DEFAULT_OUTSIDE_MARGIN;
            mRightX = mLeftX + paddleWidth;
        }
        else if (paddlePosition == GameObjects.Scene.RIGHT_PADDLE) {
            mPaddlePosition = paddlePosition;
            mRightX = gameBoardHorizontalMargin + gameBoardWidth - DEFAULT_OUTSIDE_MARGIN;
            mLeftX = mRightX - paddleWidth;
        }
        else {
            throw new IllegalArgumentException("paddlePosition must be either" +
                    "GameObjects.Scene.LEFT_PADDLE or GameObjects.Scene.RIGHT_PADDLE");
        }

        mTopY =  (gameBoardHeight - paddleHeight) / 2f;
        mBottomY = mTopY + paddleHeight;

        mColor = paddleColor;

        // Set max speed
        mComputerControlled = computerControlled;
        if (mComputerControlled) {
            mMaxSpeedInPxPerMs = MAXIMUM_COMPUTER_PADDLE_SPEED_IN_PX_PER_MS;
        }
        else {
            mMaxSpeedInPxPerMs = MAXIMUM_HUMAN_PADDLE_SPEED_IN_PX_PER_MS;
        }
    }


    // ============================ GameObjects.Paddle methods ===================================

    @Override
    public boolean isComputerControlled() {
        return mComputerControlled;
    }

    @Override
    public void move(float deltaY, final float gameBoardHeight,
                     final long millisecondsSinceLastUpdate) {

        // Reduce deltaY if moving that far would exceed maximum paddle speed
        if (deltaY > 0) {
            deltaY = Math.min(deltaY, mMaxSpeedInPxPerMs * millisecondsSinceLastUpdate);
        }
        else {
            deltaY = Math.max(deltaY, (- mMaxSpeedInPxPerMs) * millisecondsSinceLastUpdate);
        }

        // Update top and bottom coordinates
        mTopY += deltaY;
        mBottomY += deltaY;

        // Make sure paddle doesn't go off the game board
        if (mTopY < 0) {
            float overage = -mTopY;
            mTopY += overage;
            mBottomY += overage;
        }
        else if (mBottomY > gameBoardHeight) {
            float overage = mBottomY - gameBoardHeight;
            mTopY -= overage;
            mBottomY -= overage;
        }
    }

    @Override
    public float getRelativeCollisionLocation(@NonNull GameObjects.Ball ball) {

        boolean possibleCollision;

        // If left paddle, consider leftmost point on the ball
        if (mPaddlePosition == GameObjects.Scene.LEFT_PADDLE) {
            possibleCollision = (ball.getCenterX() - ball.getRadius() <= mRightX);
        }
        // If right paddle, consider rightmost point on the ball
        else if (mPaddlePosition == GameObjects.Scene.RIGHT_PADDLE) {
            possibleCollision = (ball.getCenterX() + ball.getRadius() >= mLeftX);
        }
        else {
            throw new IllegalStateException("Paddle's position is neither " +
                    "GameObjects.Scene.LEFT_PADDLE nor GameObjects.Scene.RIGHT_PADDLE");
        }

        // If x coordinate indicates a possible collision, check y coordinate
        if (possibleCollision && ball.getCenterY() >= mTopY && ball.getCenterY() <= mBottomY) {

            // Determine how far above or below the paddle's center the ball hit
            float paddleHalfHeight = (mBottomY - mTopY) / 2f;
            float paddleCenterY = mTopY + paddleHalfHeight;

            // Return between 0.0 and 1.0 if ball struck top half, else between 0.0 and -1.0 if
            // ball struck bottom half.

            // Add a little extra so the computer isn't too perfect...
            float extra = sRandom.nextFloat() * COMPUTER_PADDLE_EXTRA_ABS_VALUE * 2 -
                    COMPUTER_PADDLE_EXTRA_ABS_VALUE;

            return (-((ball.getCenterY() - paddleCenterY) / paddleHalfHeight)) + extra;
        }
        else {
            return GameObjects.Scene.NO_PADDLE_HIT;
        }
    }

    @Override
    public float getCenterY() {
        return mTopY + ((mBottomY - mTopY) / 2f);
    }


    // ========================= GameEngine.RectangleToRender methods ================================

    @Override
    public float getLeftX() {
        return mLeftX;
    }

    @Override
    public float getTopY() {
        return mTopY;
    }

    @Override
    public float getRightX() {
        return mRightX;
    }

    @Override
    public float getBottomY() {
        return mBottomY;
    }

    @Override
    public int getColor() {
        return mColor;
    }


    // =========================== Parcelable methods & constant ==================================

    protected PongPaddle(Parcel in) {
        mPaddlePosition = in.readInt();
        mColor = in.readInt();
        mLeftX = in.readFloat();
        mTopY = in.readFloat();
        mRightX = in.readFloat();
        mBottomY = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPaddlePosition);
        dest.writeInt(mColor);
        dest.writeFloat(mLeftX);
        dest.writeFloat(mTopY);
        dest.writeFloat(mRightX);
        dest.writeFloat(mBottomY);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PongPaddle> CREATOR = new Creator<PongPaddle>() {
        @Override
        public PongPaddle createFromParcel(Parcel in) {
            return new PongPaddle(in);
        }

        @Override
        public PongPaddle[] newArray(int size) {
            return new PongPaddle[size];
        }
    };
}