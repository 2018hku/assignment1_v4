package comp7506.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Click to return to the main page
        Button play_again = (Button) findViewById(R.id.play_again);
        play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switch_p1 = new Intent(ResultActivity.this,Page1Activity.class);
                startActivityForResult(switch_p1,4);
                finish();
            }
        });

        updateResult();

    }

    //function to update the result based on the game play.
    private void updateResult()
    {
        //get the textview instances
        TextView scoreboard = (TextView) findViewById(R.id.score_result);
        TextView bmiboard = (TextView) findViewById(R.id.score_result2);

        //get the parameter passed from the game
        //update score
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String score = extras.getString("final_score");
            String bmi = extras.getString("bmi_class");

            scoreboard.setText(score);
            bmiboard.setText(bmi);
        }
    }

}
