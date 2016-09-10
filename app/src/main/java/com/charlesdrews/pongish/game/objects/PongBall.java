package com.charlesdrews.pongish.game.objects;

import android.os.Parcel;

/**
 * Models a ball that moves in a straight line without gravity or friction (i.e. speed does and
 * direction do not change on their own, only when explicitly changed).
 *
 * Created by charlie on 9/10/16.
 */
public class PongBall implements GameObjects.Ball {

    // ================================= Member variables =======================================
    //TODO


    // =================================== Constructor ==========================================
    //TODO


    // =============================== GameObjects.Ball methods ===================================

    @Override
    public void move(long millisecondsSinceLastUpdate) {
        //TODO

    }

    @Override
    public int checkIfHitSideWall(float rightWallXCoordinate) {
        //TODO
        return 0;
    }

    @Override
    public void changeSpeed(float deltaSpeedInPxPerSecond) {
        //TODO

    }

    @Override
    public void setDirection(double directionInDegrees) {
        //TODO

    }


    // ========================== GameEngine.CircleToRender methods ==============================

    @Override
    public float getCenterX() {
        //TODO
        return 0;
    }

    @Override
    public float getCenterY() {
        //TODO
        return 0;
    }

    @Override
    public float getRadius() {
        //TODO
        return 0;
    }

    @Override
    public int getColor() {
        //TODO
        return 0;
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
