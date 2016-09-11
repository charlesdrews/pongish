package com.charlesdrews.pongish.game.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.charlesdrews.pongish.game.GameEngine;

/**
 * Model the vertical lines that appear on the game board.
 *
 * Created by charlie on 9/11/16.
 */
public class PongLine implements GameObjects.VerticalLine, GameEngine.VerticalLineToRender {

    private float mX, mTopY, mBottomY;
    private int mColor;
    private boolean mDashed;

    public PongLine(float x, float topY, float bottomY, int color, boolean dashed) {
        mX = x;
        mTopY = topY;
        mBottomY = bottomY;
        mColor = color;
        mDashed = dashed;
    }


    // ============================ GameObjects.VerticalLine methods =============================

    @Override
    public void setColor(int color) {
        mColor = color;
    }


    // =========================== GameEngine.VerticalLineToRender methods ========================

    @Override
    public float getX() {
        return mX;
    }

    @Override
    public float getTopY() {
        return mTopY;
    }

    @Override
    public float getBottomY() {
        return mBottomY;
    }

    @Override
    public int getColor() {
        return mColor;
    }

    @Override
    public boolean isDashed() {
        return mDashed;
    }


    // =========================== Parcelable methods & constant ==================================

    protected PongLine(Parcel in) {
        mX = in.readFloat();
        mTopY = in.readFloat();
        mBottomY = in.readFloat();
        mColor = in.readInt();
        mDashed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mX);
        dest.writeFloat(mTopY);
        dest.writeFloat(mBottomY);
        dest.writeInt(mColor);
        dest.writeByte((byte) (mDashed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PongLine> CREATOR = new Creator<PongLine>() {
        @Override
        public PongLine createFromParcel(Parcel in) {
            return new PongLine(in);
        }

        @Override
        public PongLine[] newArray(int size) {
            return new PongLine[size];
        }
    };
}
