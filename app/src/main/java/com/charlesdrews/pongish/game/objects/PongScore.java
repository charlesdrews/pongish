package com.charlesdrews.pongish.game.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model a player's score, including the position on the game board where it will be drawn.
 *
 * Created by charlie on 9/11/16.
 */
public class PongScore implements GameObjects.Score {

    private int mScore, mColor;
    private float mX, mTopY, mTextSize;
    private boolean mRightAligned;

    public PongScore(int color, float x, float topY, float textSize, boolean rightAligned) {
        mScore = 0;
        mColor = color;
        mX = x;
        mTopY = topY;
        mTextSize = textSize;
        mRightAligned = rightAligned;
    }

    // ================================ GameObjects.Score methods =================================

    @Override
    public void incrementScoreByOne() {
        mScore += 1;
    }

    @Override
    public void setScore(int score) {
        if (score >= 0) {
            mScore = score;
        }
    }


    // ============================== GameEngine.ScoreToRender methods ============================

    @Override
    public String getScoreText() {
        return "" + mScore;
    }

    @Override
    public float getX() {
        return mX;
    }

    @Override
    public float getTopY() {
        return mTopY;
    }

    @Override
    public float getTextSize() {
        return mTextSize;
    }

    @Override
    public int getColor() {
        return mColor;
    }

    @Override
    public boolean isRightAligned() {
        return mRightAligned;
    }


    // =========================== Parcelable methods & constant ==================================

    protected PongScore(Parcel in) {
        mScore = in.readInt();
        mColor = in.readInt();
        mX = in.readFloat();
        mTopY = in.readFloat();
        mTextSize = in.readFloat();
        mRightAligned = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mScore);
        dest.writeInt(mColor);
        dest.writeFloat(mX);
        dest.writeFloat(mTopY);
        dest.writeFloat(mTextSize);
        dest.writeByte((byte) (mRightAligned ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PongScore> CREATOR = new Creator<PongScore>() {
        @Override
        public PongScore createFromParcel(Parcel in) {
            return new PongScore(in);
        }

        @Override
        public PongScore[] newArray(int size) {
            return new PongScore[size];
        }
    };
}
