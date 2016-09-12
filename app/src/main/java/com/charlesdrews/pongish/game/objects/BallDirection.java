package com.charlesdrews.pongish.game.objects;

import android.os.Parcel;

import java.util.Random;

/**
 * Models a ball's direction in degrees, with 0° = up, 180°/-180° = down, anything positive up to
 * 180° = rightward, and anything negative down to -180° = leftward.
 *
 * Created by charlie on 9/10/16.
 */
public class BallDirection implements GameObjects.Direction {

    private static final double MAX_RANDOM_DEGREES = 150d;
    private static final double MIN_RANDOM_DEGREES = 30d;

    private static Random sRandom = new Random();

    private double mDegrees;

    /**
     * Start off with a random direction ranging from 30° to 150°, or from -30° to -150°, suitable
     * for the initial direction of the ball at the beginning of a round.
     */
    public BallDirection() {

        // First get absolute value in allowed range
        mDegrees = MIN_RANDOM_DEGREES +
                sRandom.nextDouble() * (MAX_RANDOM_DEGREES - MIN_RANDOM_DEGREES);

        // Then choose leftward (-) or rightward (+) at random
        if (sRandom.nextInt(2) == 0) {
            mDegrees = -mDegrees;
        }
    }


    // ============================= GameObjects.Direction methods ===============================

    @Override
    public void setDirectionInDegrees(final double degrees) {
        mDegrees = degrees;

        // Normalize the value to be between -180° and 180°
        while (mDegrees >= 180d) {
            mDegrees -= 360d;
        }
        while (mDegrees <= -180d) {
            mDegrees += 360d;
        }

        // Don't let it be exactly 0°, 180°, or -180°, since that would make game play impossible.
        if (mDegrees % 90d == 0d) {
            mDegrees += 1d;
        }
    }

    @Override
    public double getDirectionInDegrees() {
        return mDegrees;
    }

    @Override
    public double getDirectionInRadians() {
        return Math.toRadians(mDegrees - 90d);
    }


    // =========================== Parcelable methods & constant ==================================

    protected BallDirection(Parcel in) {
        mDegrees = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mDegrees);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BallDirection> CREATOR = new Creator<BallDirection>() {
        @Override
        public BallDirection createFromParcel(Parcel in) {
            return new BallDirection(in);
        }

        @Override
        public BallDirection[] newArray(int size) {
            return new BallDirection[size];
        }
    };
}
