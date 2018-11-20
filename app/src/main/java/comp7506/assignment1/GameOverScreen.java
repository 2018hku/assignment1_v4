package comp7506.assignment1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GameOverScreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);//setContentView to the game surfaceview
        //Custom XML files can also be used, and then retrieve the game instance using findViewById.
    }
}
