package com.charlesdrews.pongish.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Provide the View and Renderer functionality.
 *
 * Created by charlie on 9/10/16.
 */
public class PongView extends SurfaceView implements SurfaceHolder.Callback, GameContract.View,
        GameEngine.Renderer {

    // ===================================== Member variables =====================================

    private GameContract.Presenter mPresenter;
    private GameContract.ViewActivity mViewActivity;

    private boolean mSurfaceReady = false;
    private SurfaceHolder mHolder;
    private int mSurfaceWidth = 0;
    private Canvas mCanvas;
    private Paint mPaint = new Paint();

    private int mLeftSideActivePointerId = MotionEvent.INVALID_POINTER_ID;
    private boolean mLeftSideMoveInProgress = false;
    private float mLeftSideLastYCoordinate = -1f;

    private int mRightSideActivePointerId = MotionEvent.INVALID_POINTER_ID;
    private boolean mRightSideMoveInProgress = false;
    private float mRightSideLastYCoordinate = -1f;



    // ======================================= Constructor =======================================

    public PongView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mViewActivity = (GameContract.ViewActivity) context;

        mHolder = getHolder();
        mHolder.addCallback(this);
    }


    // =============================== SurfaceView override methods ===============================

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int index = MotionEventCompat.getActionIndex(event);
        int id = event.getPointerId(index);
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {

                // Check if pointer is on left or right side, and whether there is already an
                // active pointer on that side.
                if (event.getX(index) < mSurfaceWidth / 2f) {

                    // Pointer is on left side. If there isn't already an active pointer on the
                    // left side, consider this one. Otherwise, ignore it (only care about ONE
                    // pointer on each side at a time).
                    if (!mLeftSideMoveInProgress) {
                        mLeftSideMoveInProgress = true;
                        mLeftSideActivePointerId = id;
                        mLeftSideLastYCoordinate = event.getY(index);
                    }
                }
                else {

                    // Pointer is on right side. Perform same check as on left side.
                    if (!mRightSideMoveInProgress) {
                        mRightSideMoveInProgress = true;
                        mRightSideActivePointerId = id;
                        mRightSideLastYCoordinate = event.getY(index);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Report left side deltaY to presenter if a move is in progress
                if (mLeftSideMoveInProgress){
                    float newY = event.getY(event.findPointerIndex(mLeftSideActivePointerId));
                    mPresenter.onLeftSidePointerMove(newY - mLeftSideLastYCoordinate);
                    mLeftSideLastYCoordinate = newY;
                }

                // Report right side deltaY to presenter if a move is in progress
                if (mRightSideMoveInProgress){
                    float newY = event.getY(event.findPointerIndex(mRightSideActivePointerId));
                    mPresenter.onRightSidePointerMove(newY - mRightSideLastYCoordinate);
                    mRightSideLastYCoordinate = newY;
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (id == mLeftSideActivePointerId) {
                    mLeftSideMoveInProgress = false;
                    mLeftSideActivePointerId = MotionEvent.INVALID_POINTER_ID;
                    mLeftSideLastYCoordinate = -1f;
                }
                else if (id == mRightSideActivePointerId) {
                    mRightSideMoveInProgress = false;
                    mRightSideActivePointerId = MotionEvent.INVALID_POINTER_ID;
                    mRightSideLastYCoordinate = -1f;
                }
                break;
            }
        }

        // Return true so that the event is consumed and subsequent events are not ignored.
        return true;
    }


    // ================================ SurfaceHolder.Callback methods ===========================

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {}

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mPresenter.setGameBoardDimensions(width, height);

        // If this is the first time surfaceChanged was called, then start the game!
        if (!mSurfaceReady) {
            mSurfaceReady = true;
            mSurfaceWidth = width;
            mPresenter.onGameViewReady(mViewActivity.getSavedGameState());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceReady = false;
    }


    // ================================ GameContract.View methods ===============================

    @Override
    public void bindPresenter(@NonNull GameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void unbindPresenter() {
        mPresenter = null;
    }

    @Override
    public boolean isGameViewReady() {
        return mSurfaceReady;
    }


    // ================================ GameEngine.Renderer methods ==============================

    @Override
    public boolean beginDrawing() {
        if (mHolder.getSurface().isValid()) {
            mCanvas = mHolder.lockCanvas();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void commitDrawing() {
        mHolder.unlockCanvasAndPost(mCanvas);
        mCanvas = null;
    }

    @Override
    public void drawBackground(int color) {
        mCanvas.drawColor(color);
    }

    @Override
    public void drawCircle(float centerX, float centerY, float radius, int color) {
        mPaint.setColor(color);
        mCanvas.drawCircle(centerX, centerY, radius, mPaint);
    }

    @Override
    public void drawRect(float leftX, float topY, float rightX, float bottomY, int color) {
        mPaint.setColor(color);
        mCanvas.drawRect(leftX, topY, rightX, bottomY, mPaint);
    }

    @Override
    public void drawFramesPerSecond(@NonNull String fpsText, float x, float y, float textSize, int color) {
        mPaint.setColor(color);
        mPaint.setTextSize(textSize);
        mCanvas.drawText(fpsText, x, y, mPaint);
    }
}
