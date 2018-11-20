package comp7506.assignment1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GameLauncher extends AppCompatActivity {
    private GameView  game;
    private String gender;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //command to retrieve the selected gender from the previous page
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gender = extras.getString("gender");
        }

        //Initialise the game, with gender input.
        game = new GameView (GameLauncher.this, gender);
        setContentView(game);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.background);
        mp.start();
    }
}
