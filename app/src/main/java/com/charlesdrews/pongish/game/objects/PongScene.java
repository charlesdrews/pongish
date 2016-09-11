package com.charlesdrews.pongish.game.objects;

import android.graphics.Color;
import android.os.Parcel;

import com.charlesdrews.pongish.game.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Container for the other game objects. Includes logic to update the positions of those objects.
 *
 * Created by charlie on 9/10/16.
 */
public class PongScene implements GameObjects.Scene {

    // =================================== Constants =============================================

    private static final int DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private static final int HORIZONTAL_THUMB_MARGIN_IN_PX = 200;

    private static final int PADDLE_COLOR = Color.WHITE;
    private static final float PADDLE_HEIGHT_AS_PERCENT_OF_GAME_BOARD_HEIGHT = 0.2f;
    private static final float PADDLE_WIDTH_IN_PX = 20f;

    private static final int NORMAL_BALL_COLOR = Color.WHITE;
    private static final float NORMAL_BALL_RADIUS_IN_PX = 30f;
    private static final float NORMAL_BALL_SPEED_IN_PX_PER_MS = 0.8f;

    private static final float BALL_SPEED_INCREASE_ON_PADDLE_HIT = 0.02f;

    private static final int BALL_COLOR_ON_POINT_SCORED = Color.RED;

    private static final double MIN_ABS_VAL_DEG_AFTER_PADDLE_COLLISION = 10d;
    private static final double HALF_ABS_VAL_RANGE_AFTER_PADDLE_COLLISION =
            (180d - 2d * MIN_ABS_VAL_DEG_AFTER_PADDLE_COLLISION) / 2d;


    // ================================= Member variables =======================================

    private int mGameBoardWidth, mGameBoardHeight, mGameBoardHorizontalMargin, mBackgroundColor;
    private GameObjects.Paddle mLeftPaddle, mRightPaddle;
    private GameObjects.Ball mNormalBall;
    private List<GameObjects.Ball> mBonusBalls;
    private int consecutivePaddleHits = 0;


    // =================================== Constructor ==========================================

    public PongScene(final int gameBoardWidth, final int gameBoardHeight,
                     final int gameBoardColor) {
        mGameBoardWidth = gameBoardWidth - 2 * HORIZONTAL_THUMB_MARGIN_IN_PX;
        mGameBoardHeight = gameBoardHeight;
        mGameBoardHorizontalMargin = HORIZONTAL_THUMB_MARGIN_IN_PX;
        mBackgroundColor = gameBoardColor;

        initializeGameObjects();
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
    public boolean updateGameObjectPositions(final long millisSinceLastUpdate) {

        // Move normal ball (update direction if paddle hit, otherwise check if side wall hit)
        boolean sideWallHit = moveBallAndCheckResult(mNormalBall, millisSinceLastUpdate);

        // Do the same for each bonus ball
        for (GameObjects.Ball ball : mBonusBalls) {
            sideWallHit = sideWallHit || moveBallAndCheckResult(ball, millisSinceLastUpdate);
        }

        return sideWallHit;
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

    @Override
    public void resetAfterPointScored() {
        initializeGameObjects();
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


    private void initializeGameObjects() {
        mLeftPaddle = new PongPaddle(LEFT_PADDLE, PADDLE_WIDTH_IN_PX,
                mGameBoardHeight * PADDLE_HEIGHT_AS_PERCENT_OF_GAME_BOARD_HEIGHT,
                mGameBoardWidth, mGameBoardHeight, mGameBoardHorizontalMargin, PADDLE_COLOR);

        mRightPaddle = new PongPaddle(RIGHT_PADDLE, PADDLE_WIDTH_IN_PX,
                mGameBoardHeight * PADDLE_HEIGHT_AS_PERCENT_OF_GAME_BOARD_HEIGHT,
                mGameBoardWidth, mGameBoardHeight, mGameBoardHorizontalMargin, PADDLE_COLOR);

        mNormalBall = new PongBall(mGameBoardWidth, mGameBoardHeight, mGameBoardHorizontalMargin,
                NORMAL_BALL_RADIUS_IN_PX, NORMAL_BALL_SPEED_IN_PX_PER_MS, NORMAL_BALL_COLOR);

        if (mBonusBalls == null) {
            mBonusBalls = new CopyOnWriteArrayList<>();
        }
        else {
            mBonusBalls.clear();
        }
    }

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

    /**
     * Check whether the given ball has hit either paddle. If so, update the ball's direction.
     *
     * @param ball whose position will be checked against the paddle positions.
     * @return true if ball hit a paddle, else false.
     */
    private boolean checkForPaddleCollisionsAndUpdateBall(GameObjects.Ball ball) {

        // Check for collision with left paddle
        float collision = mLeftPaddle.getRelativeCollisionLocation(ball);
        if (collision != NO_PADDLE_HIT) {
            consecutivePaddleHits += 1;
            ball.setDirection(getDirectionAfterPaddleCollision(LEFT_PADDLE, collision));
            ball.changeSpeed(BALL_SPEED_INCREASE_ON_PADDLE_HIT);
            return true;
        }
        else {
            // If no collision with left paddle, check right paddle
            collision = mRightPaddle.getRelativeCollisionLocation(ball);
            if (collision != NO_PADDLE_HIT) {
                consecutivePaddleHits += 1;
                ball.setDirection(getDirectionAfterPaddleCollision(RIGHT_PADDLE, collision));
                ball.changeSpeed(BALL_SPEED_INCREASE_ON_PADDLE_HIT);
                return true;
            }
        }
        return false;
    }

    private boolean moveBallAndCheckResult(GameObjects.Ball ball, long millisSinceLastUpdate) {

        // Start by updating the ball's position
        ball.move(millisSinceLastUpdate, mGameBoardHeight);

        // Then check if it hit a paddle, and update its direction if yes
        if (!checkForPaddleCollisionsAndUpdateBall(mNormalBall)) {

            // If the ball hasn't hit either paddle, check if it hit the left or right wall
            int hit = mNormalBall.checkIfPointScored(mGameBoardWidth, mGameBoardHorizontalMargin);

            // If a side wall was hit, return true so the game engine knows to pause the loop
            switch (hit) {
                case GameObjects.Scene.LEFT_WALL_HIT:
                    ball.setColor(BALL_COLOR_ON_POINT_SCORED);
                    consecutivePaddleHits = 0;
                    return true;

                case GameObjects.Scene.RIGHT_WALL_HIT:
                    ball.setColor(BALL_COLOR_ON_POINT_SCORED);
                    consecutivePaddleHits = 0;
                    return true;

                default:
                    return false;
            }
        }
        else {
            return false;
        }
    }
}
