package com.charlesdrews.pongish.game;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Provide the View and Renderer functionality.
 *
 * Created by charlie on 9/10/16.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, GameContract.View,
        GameEngine.Renderer {

    // ===================================== Member variables =====================================

    private GameContract.Presenter mPresenter;
    private GameContract.ViewActivity mViewActivity;

    private boolean mSurfaceReady = false;
    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private boolean mDragInProgress = false;
    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;


    // ======================================= Constructor =======================================

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mViewActivity = (GameContract.ViewActivity) context;

        mHolder = getHolder();
        mHolder.addCallback(this);
    }


    // =============================== SurfaceView override methods ===============================

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //TODO
        return true;
    }


    // ================================ SurfaceHolder.Callback methods ===========================

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //TODO

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //TODO

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //TODO

    }


    // ================================ GameContract.View methods ===============================

    @Override
    public void bindPresenter(@NonNull GameContract.Presenter presenter) {
        //TODO

    }

    @Override
    public void unbindPresenter() {
        //TODO

    }

    @Override
    public boolean isGameViewReady() {
        //TODO
        return false;
    }


    // ================================ GameEngine.Renderer methods ==============================

    @Override
    public boolean beginDrawing() {
        //TODO
        return false;
    }

    @Override
    public void commitDrawing() {
        //TODO

    }

    @Override
    public void drawBackground(int color) {
        //TODO

    }

    @Override
    public void drawCircle(float centerX, float centerY, float radius, int color) {
        //TODO

    }

    @Override
    public void drawRect(float leftX, float topY, float rightX, float bottomY, int color) {
        //TODO

    }

    @Override
    public void drawFramesPerSecond(@NonNull String fpsText, float x, float y, float textSize, int color) {
        //TODO

    }
}
