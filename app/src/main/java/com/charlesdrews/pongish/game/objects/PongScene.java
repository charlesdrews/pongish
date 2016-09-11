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

    private static final int CENTER_LINE_COLOR = Color.WHITE;
    private static final int END_LINE_COLOR = Color.WHITE;

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

    private List<GameEngine.CircleToRender> mCirclesToRender;
    private List<GameEngine.RectangleToRender> mRectanglesToRender;
    private List<GameEngine.VerticalLineToRender> mVerticalLinesToRender;


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

        //TODO - if consecutivePaddleHits exceeds a threshold, add bonus balls?

        return sideWallHit;
    }

    @Override
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override
    public List<GameEngine.CircleToRender> getCirclesToRender() {

        // Clear list of circles to render, in case it contains any expired bonus balls.
        mCirclesToRender.clear();

        // Add normal ball and bonus balls (if any exist).
        mCirclesToRender.add(mNormalBall);
        for (GameObjects.Ball ball : mBonusBalls) {
            mCirclesToRender.add(ball);
        }

        return mCirclesToRender;
    }

    @Override
    public List<GameEngine.RectangleToRender> getRectanglesToRender() {
        return mRectanglesToRender;
    }

    @Override
    public List<GameEngine.VerticalLineToRender> getVerticalLinesToRender() {
        return mVerticalLinesToRender;
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


    /**
     * Add a left paddle, a right paddle, and the normal ball to the scene. If any bonus balls
     * exist, remove them. Also prepare the lists of circles and rectangles to return to the
     * renderer.
     */
    private void initializeGameObjects() {

        // Add left & right paddles and the normal ball.
        mLeftPaddle = new PongPaddle(LEFT_PADDLE, PADDLE_WIDTH_IN_PX,
                mGameBoardHeight * PADDLE_HEIGHT_AS_PERCENT_OF_GAME_BOARD_HEIGHT,
                mGameBoardWidth, mGameBoardHeight, mGameBoardHorizontalMargin, PADDLE_COLOR);

        mRightPaddle = new PongPaddle(RIGHT_PADDLE, PADDLE_WIDTH_IN_PX,
                mGameBoardHeight * PADDLE_HEIGHT_AS_PERCENT_OF_GAME_BOARD_HEIGHT,
                mGameBoardWidth, mGameBoardHeight, mGameBoardHorizontalMargin, PADDLE_COLOR);

        mNormalBall = new PongBall(mGameBoardWidth, mGameBoardHeight, mGameBoardHorizontalMargin,
                NORMAL_BALL_RADIUS_IN_PX, NORMAL_BALL_SPEED_IN_PX_PER_MS, NORMAL_BALL_COLOR);

        // Instantiate an empty list for bonus balls, or if one exists, empty it.
        if (mBonusBalls == null) {
            mBonusBalls = new CopyOnWriteArrayList<>();
        }
        else {
            mBonusBalls.clear();
        }

        // Instantiate empty list to hold balls as circles to return to the renderer.
        mCirclesToRender = new ArrayList<>();

        // Instantiate and initialize list of paddles as rectangles to return to the renderer.
        mRectanglesToRender = new ArrayList<>(2);
        mRectanglesToRender.add(mLeftPaddle);
        mRectanglesToRender.add(mRightPaddle);

        // Instantiate and initialize list of vertical lines to return to the renderer.
        mVerticalLinesToRender = new ArrayList<>(3);

        // Left end line
        mVerticalLinesToRender.add(new PongLine(HORIZONTAL_THUMB_MARGIN_IN_PX, 0,
                mGameBoardHeight, END_LINE_COLOR, false));

        // Right end line
        mVerticalLinesToRender.add(new PongLine(HORIZONTAL_THUMB_MARGIN_IN_PX + mGameBoardWidth,
                0, mGameBoardHeight, END_LINE_COLOR, false));

        // Center line
        mVerticalLinesToRender.add(new PongLine(HORIZONTAL_THUMB_MARGIN_IN_PX + mGameBoardWidth / 2f,
                0, mGameBoardHeight, CENTER_LINE_COLOR, true));

    }

    /**
     * Calculate the new direction for a ball that has struck the specified paddle in the given
     * location.
     *
     * @param paddlePosition is either GameObjects.Scene.LEFT_PADDLE or
     *                       GameObjects.Scene.RIGHT_PADDLE.
     * @param collisionLocation ranges from -1.0, bottom of paddle, to 1.0, top of paddle, with
     *                          0.0 being the exact center of the paddle.
     * @return a new ball direction in degrees.
     */
    private double getDirectionAfterPaddleCollision(final int paddlePosition,
                                                    final float collisionLocation) {

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

    /**
     * Take the following steps:
     *   1) Move the given Ball. Its move() method handles collisions with top/bottom walls.
     *   2) Check if it hit a paddle and if so, change it's direction accordingly.
     *   3) If no paddle hit, check if a point was scored; return true if yes.
     * @param ball is the Ball whose position will be updated.
     * @param millisSinceLastUpdate is the time in milliseconds since the ball was last moved.
     * @return true if a point was scored, else false.
     */
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
