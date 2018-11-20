package comp7506.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Page1Activity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        Button start_game = (Button) findViewById(R.id.start_game);
        TextView game_p1 = (TextView) findViewById(R.id.game_p1);
        start_game.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent switch_p2 = new Intent(view.getContext(),Page2Activity.class);
                startActivityForResult(switch_p2,1);
                finish();
            };
        });
    };
}
