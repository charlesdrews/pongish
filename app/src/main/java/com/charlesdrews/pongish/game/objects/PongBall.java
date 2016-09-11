package com.charlesdrews.pongish.game.objects;

import android.graphics.Color;
import android.os.Parcel;

/**
 * Models a ball that moves in a straight line without gravity or friction (i.e. speed does and
 * direction do not change on their own, only when explicitly changed).
 *
 * Created by charlie on 9/10/16.
 */
public class PongBall implements GameObjects.Ball {

    // ================================= Member variables =======================================

    private GameObjects.Direction mDirection;
    private float mCenterX, mCenterY, mRadiusInPx, mSpeedInPxPerMs;
    private int mColor;


    // =================================== Constructor ==========================================

    public PongBall(final int gameBoardWidth, final int gameBoardHeight,
                    final int gameBoardHorizontalMargin, final float radiusInPx,
                    final float speedInPxPerMs, final int color) {
        mCenterX = gameBoardHorizontalMargin + gameBoardWidth / 2f;
        mCenterY = gameBoardHeight / 2f;
        mRadiusInPx = radiusInPx;
        mSpeedInPxPerMs = speedInPxPerMs;
        mColor = color;

        mDirection = new BallDirection();
    }


    // =============================== GameObjects.Ball methods ===================================

    @Override
    public void move(final long millisecondsSinceLastUpdate, final int gameBoardHeight) {
        float distanceInPx = mSpeedInPxPerMs * millisecondsSinceLastUpdate;

        // Trigonometry
        double angleInRadians = mDirection.getDirectionInRadians();
        mCenterX += Math.cos(angleInRadians) * distanceInPx;
        mCenterY += Math.sin(angleInRadians) * distanceInPx;

        // Check if ball hit top or bottom wall
        if (mCenterY - mRadiusInPx < 0) {
            mDirection.setDirectionInDegrees(180d - mDirection.getDirectionInDegrees());
            mCenterY = mRadiusInPx;
        }
        else if (mCenterY + mRadiusInPx > gameBoardHeight) {
            mDirection.setDirectionInDegrees(180d - mDirection.getDirectionInDegrees());
            mCenterY = gameBoardHeight - mRadiusInPx;
        }
    }

    @Override
    public int checkIfPointScored(int gameBoardWidth, int gameBoardHorizontalMargin) {

        // Check left wall
        if (mCenterX - mRadiusInPx <= gameBoardHorizontalMargin) {
            return GameObjects.Scene.LEFT_WALL_HIT;
        }
        // Check right wall
        else if (mCenterX + mRadiusInPx >= gameBoardHorizontalMargin + gameBoardWidth) {
            return GameObjects.Scene.RIGHT_WALL_HIT;
        }
        else {
            return GameObjects.Scene.NO_WALL_HIT;
        }
    }

    @Override
    public void changeSpeed(float deltaSpeedInPxPerMs) {
        if (mSpeedInPxPerMs != 0) {
            mSpeedInPxPerMs += deltaSpeedInPxPerMs;
        }
    }

    @Override
    public void setDirection(double directionInDegrees) {
        mDirection.setDirectionInDegrees(directionInDegrees);
    }

    @Override
    public void setColor(int color) {
        mColor = color;
    }


    // ========================== GameEngine.CircleToRender methods ==============================

    @Override
    public float getCenterX() {
        return mCenterX;
    }

    @Override
    public float getCenterY() {
        return mCenterY;
    }

    @Override
    public float getRadius() {
        return mRadiusInPx;
    }

    @Override
    public int getColor() {
        return mColor;
    }


    // =========================== Parcelable methods & constant ==================================

    protected PongBall(Parcel in) {
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

    public static final Creator<PongBall> CREATOR = new Creator<PongBall>() {
        @Override
        public PongBall createFromParcel(Parcel in) {
            return new PongBall(in);
        }

        @Override
        public PongBall[] newArray(int size) {
            return new PongBall[size];
        }
    };
}
