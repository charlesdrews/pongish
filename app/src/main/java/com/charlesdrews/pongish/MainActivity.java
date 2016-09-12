package com.charlesdrews.pongish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.charlesdrews.pongish.game.PongActivity;
import com.charlesdrews.pongish.game.objects.GameObjects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.two_player_game_button).setOnClickListener(this);
        findViewById(R.id.one_player_right_game).setOnClickListener(this);
        findViewById(R.id.one_player_left_game).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(MainActivity.this, PongActivity.class);

        switch (view.getId()) {

            case R.id.two_player_game_button:
                intent.putExtra(PongActivity.COMPUTER_CONTROLLED_PADDLE_KEY,
                        GameObjects.Scene.NEITHER_PADDLE);
                break;

            case R.id.one_player_right_game:
                intent.putExtra(PongActivity.COMPUTER_CONTROLLED_PADDLE_KEY,
                        GameObjects.Scene.LEFT_PADDLE);
                break;

            case R.id.one_player_left_game:
                intent.putExtra(PongActivity.COMPUTER_CONTROLLED_PADDLE_KEY,
                        GameObjects.Scene.RIGHT_PADDLE);
                break;
        }

        startActivity(intent);
    }
}
