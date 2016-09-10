package com.charlesdrews.pongish.game.objects;

import android.graphics.Color;
import android.os.Parcel;

import com.charlesdrews.pongish.game.GameEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for the other game objects.
 *
 * Created by charlie on 9/10/16.
 */
public class PongScene implements GameObjects.Scene {

    // =================================== Constants =============================================

    private static final int DEFAULT_BACKGROUND_COLOR = Color.BLACK;


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

        //TODO - initialize balls
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
        /*
        mNormalBall.move(millisSinceLastUpdate);

        for (GameObjects.Ball ball : mBonusBalls) {
            ball.move(millisSinceLastUpdate);
        }
        */
    }

    @Override
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override
    public List<GameEngine.CircleToRender> getCirclesToRender() {

        /*
        List<GameEngine.CircleToRender> circles = new ArrayList<>(mBonusBalls.size() + 1);

        circles.add(mNormalBall);
        for (GameObjects.Ball ball : mBonusBalls) {
            circles.add(ball);
        }

        return circles;
        */
        return new ArrayList<GameEngine.CircleToRender>();
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
}
