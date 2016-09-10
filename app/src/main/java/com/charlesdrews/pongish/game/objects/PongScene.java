package com.charlesdrews.pongish.game.objects;

import android.os.Parcel;

import com.charlesdrews.pongish.game.GameEngine;

import java.util.List;

/**
 * Container for the other game objects.
 *
 * Created by charlie on 9/10/16.
 */
public class PongScene implements GameObjects.Scene {

    // ================================= Member variables =======================================

    int mGameBoardWidth, mGameBoardHeight;


    // =================================== Constructor ==========================================

    public PongScene(int gameBoardWidth, int gameBoardHeight) {
        mGameBoardWidth = gameBoardWidth;
        mGameBoardHeight = gameBoardHeight;

        //TODO?
    }


    // ============================= GameObjects.PongScene methods ===================================

    @Override
    public void movePaddle(int paddle, float deltaY) {
        //TODO

    }

    @Override
    public void updateGameObjectPositions(long millisecondsSinceLastUpdate) {
        //TODO

    }

    @Override
    public List<GameEngine.CircleToRender> getCirclesToRender() {
        //TODO
        return null;
    }

    @Override
    public List<GameEngine.RectToRender> getRectsToRender() {
        //TODO
        return null;
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
