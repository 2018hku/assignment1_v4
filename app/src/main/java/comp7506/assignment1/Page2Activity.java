package comp7506.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Page2Activity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        Button male_p2 = (Button) findViewById(R.id.male_p2);
        Button female_p2 = (Button) findViewById(R.id.female_p2);
        male_p2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent switch_game = new Intent(Page2Activity.this,GameLauncher.class);
                switch_game.putExtra("gender","male");
                startActivityForResult(switch_game,1);
                finish();
            };
        });
        female_p2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent switch_game = new Intent(Page2Activity.this,GameLauncher.class);
                switch_game.putExtra("gender","female");
                startActivityForResult(switch_game,2);
                finish();
            };
        });
    };
}