package josephbalawejder.ca.scavengerhunt;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Random;

public class FindActivity extends AppCompatActivity {

    public int randomNum;
    public String item;
    public ArrayList<String> items;
    public TextView findTextView;
    public int score;
    public long time_remaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        items = getIntent().getStringArrayListExtra("ITEMS");

        score = getIntent().getExtras().getInt("SCORE", 0);
        time_remaining = getIntent().getExtras().getLong("TIME");

//        int size = categories.size();

//        Random rand = new Random();
//        randomNum = rand.nextInt(size + 1);

        if (items.size() != 0){
            item = items.get(0);
        }

        findTextView = (TextView) findViewById(R.id.findTextView);
        findTextView.setText(item);

        final Button takePicButton = findViewById(R.id.TakePicButton);
        takePicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent takePicture = new Intent(FindActivity.this, TakePicActivity.class);
                takePicture.putExtra("ITEMS", items);
                takePicture.putExtra("TIME", time_remaining);
                takePicture.putExtra("SCORE", score);
                startActivity(takePicture);
//                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
            }
        });

        final Button skipButton = findViewById(R.id.SkipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                item = items.get(0);
                items.remove(0);
                items.add(items.size(), item);
                item = items.get(0);
                findTextView.setText(item);
            }
        });

        final Button stopButton = findViewById(R.id.StopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(new Intent(FindActivity.this, MainActivity.class));
            }
        });




        CountDownTimer newtimer = new CountDownTimer(time_remaining, 1000) {

            TextView time_text = findViewById(R.id.timeRemaining);

            public void onTick(long millisUntilFinished) {
                time_text.setText("Time Remaining: " + millisUntilFinished/1000);
                time_remaining = millisUntilFinished;
            }
            public void onFinish() {
                skipButton.setEnabled(false);
                takePicButton.setEnabled(false);

                time_text.setText("Time Up!");

            }
        };
        newtimer.start();
    }
}
