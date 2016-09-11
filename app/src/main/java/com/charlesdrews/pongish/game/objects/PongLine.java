package com.charlesdrews.pongish.game.objects;

import com.charlesdrews.pongish.game.GameEngine;

/**
 * Model the vertical lines that appear on the game board.
 *
 * Created by charlie on 9/11/16.
 */
public class PongLine implements GameEngine.VerticalLineToRender {
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
}
