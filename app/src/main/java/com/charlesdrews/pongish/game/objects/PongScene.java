package com.charlesdrews.pongish.game.objects;

import android.graphics.Color;
import android.os.Parcel;

import com.charlesdrews.pongish.game.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Container for the other game objects.
 *
 * Created by charlie on 9/10/16.
 */
public class PongScene implements GameObjects.Scene {

    // =================================== Constants =============================================

    private static final int DEFAULT_BACKGROUND_COLOR = Color.BLACK;

    private static final double MIN_ABS_VAL_DEG_AFTER_PADDLE_COLLISION = 5d;
    private static final double MAX_ABS_VAL_DEG_AFTER_PADDLE_COLLISION = 180d -
            MIN_ABS_VAL_DEG_AFTER_PADDLE_COLLISION;
    private static final double ABS_VAL_RANGE_AFTER_PADDLE_COLLISION =
            MAX_ABS_VAL_DEG_AFTER_PADDLE_COLLISION - MIN_ABS_VAL_DEG_AFTER_PADDLE_COLLISION;
    private static final double HALF_ABS_VAL_RANGE_AFTER_PADDLE_COLLISION =
            ABS_VAL_RANGE_AFTER_PADDLE_COLLISION / 2d;


    // ================================= Member variables =======================================

    private int mGameBoardWidth, mGameBoardHeight, mBackgroundColor;
    private GameObjects.Paddle mLeftPaddle, mRightPaddle;
    private GameObjects.Ball mNormalBall;
    private List<GameObjects.Ball> mBonusBalls;


    // =================================== Constructor ==========================================

    public PongScene(final int gameBoardWidth, final int gameBoardHeight,
                     final int gameBoardColor) {
        mGameBoardWidth = gameBoardWidth;
        mGameBoardHeight = gameBoardHeight;
        mBackgroundColor = gameBoardColor;

        mLeftPaddle = new PongPaddle(GameObjects.Scene.LEFT_PADDLE, mGameBoardWidth,
                mGameBoardHeight);
        mRightPaddle = new PongPaddle(GameObjects.Scene.RIGHT_PADDLE, mGameBoardWidth,
                mGameBoardHeight);

        mNormalBall = new PongBall(mGameBoardWidth, mGameBoardHeight);

        mBonusBalls = new CopyOnWriteArrayList<>();
    }

    public PongScene(int gameBoardWidth, int gameBoardHeight) {
        this(gameBoardWidth, gameBoardHeight, DEFAULT_BACKGROUND_COLOR);
    }


    // ============================= GameObjects.PongScene methods ===============================

    @Override
    public void movePaddle(final int paddle, final float deltaY, final long millisSinceLastUpdate) {
        if (paddle == LEFT_PADDLE) {
            mLeftPaddle.move(deltaY, mGameBoardHeight, millisSinceLastUpdate);
        }
        else if (paddle == RIGHT_PADDLE) {
            mRightPaddle.move(deltaY, mGameBoardHeight, millisSinceLastUpdate);
        }
    }

    @Override
    public void updateGameObjectPositions(final long millisSinceLastUpdate) {

        // Move the normal ball, then check if it hit a paddle
        mNormalBall.move(millisSinceLastUpdate, mGameBoardHeight);
        checkForPaddleCollisionsAndUpdateBall(mNormalBall);

        // Do the same for each bonus ball
        for (GameObjects.Ball ball : mBonusBalls) {
            ball.move(millisSinceLastUpdate, mGameBoardHeight);
            checkForPaddleCollisionsAndUpdateBall(ball);
        }
    }

    @Override
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override
    public List<GameEngine.CircleToRender> getCirclesToRender() {

        List<GameEngine.CircleToRender> circles = new ArrayList<>(mBonusBalls.size() + 1);

        circles.add(mNormalBall);
        for (GameObjects.Ball ball : mBonusBalls) {
            circles.add(ball);
        }

        return circles;
    }

    @Override
    public List<GameEngine.RectToRender> getRectsToRender() {

        List<GameEngine.RectToRender> rects = new ArrayList<>(2);

        rects.add(mLeftPaddle);
        rects.add(mRightPaddle);

        return rects;
    }


    // =========================== Parcelable methods & constant ==================================

    protected PongScene(Parcel in) {
        //TODO
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //TODO
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PongScene> CREATOR = new Creator<PongScene>() {
        @Override
        public PongScene createFromParcel(Parcel in) {
            return new PongScene(in);
        }

        @Override
        public PongScene[] newArray(int size) {
            return new PongScene[size];
        }
    };


    // ================================ Helper methods ===========================================

    private double getDirectionAfterPaddleCollision(int paddlePosition,
                                                   float collisionLocation) {

        double absoluteValueNewDirection = 90d +
                (-collisionLocation) * HALF_ABS_VAL_RANGE_AFTER_PADDLE_COLLISION;

        if (paddlePosition == GameObjects.Scene.LEFT_PADDLE) {
            return absoluteValueNewDirection;
        }
        else if (paddlePosition == GameObjects.Scene.RIGHT_PADDLE) {
            return -absoluteValueNewDirection;
        }
        else {
            throw new IllegalStateException("Paddle's position is neither " +
                    "GameObjects.Scene.LEFT_PADDLE nor GameObjects.Scene.RIGHT_PADDLE");
        }
    }

    private void checkForPaddleCollisionsAndUpdateBall(GameObjects.Ball ball) {

        // Check for collision with left paddle
        float collision = mLeftPaddle.getRelativeCollisionLocation(ball);
        if (collision != NO_PADDLE_COLLISION) {
            ball.setDirection(getDirectionAfterPaddleCollision(LEFT_PADDLE, collision));
        }
        else {
            // If no collision with left paddle, check right paddle
            collision = mRightPaddle.getRelativeCollisionLocation(ball);
            if (collision != NO_PADDLE_COLLISION) {
                ball.setDirection(getDirectionAfterPaddleCollision(RIGHT_PADDLE, collision));
            }
        }
    }
}
