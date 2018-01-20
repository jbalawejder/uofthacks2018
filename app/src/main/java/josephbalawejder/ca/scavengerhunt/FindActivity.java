package josephbalawejder.ca.scavengerhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class FindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        ArrayList<String> categories = getIntent().getStringArrayListExtra("CATEGORY_LIST");
        int size = categories.size();

        Random rand = new Random();
        int randomNum = rand.nextInt(size + 1);
        String category = categories.get(randomNum);
        TextView findTextView = (TextView) findViewById(R.id.findTextView);
        findTextView.setText("Find " + category);

        final Button takePicButton = findViewById(R.id.TakePicButton);
        takePicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(new Intent(FindActivity.this, TakePicActivity.class));
            }
        });

        final Button skipButton = findViewById(R.id.SkipButton);

        final Button stopButton = findViewById(R.id.StopButton);
//        stopButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Code here executes on main thread after user presses button
//                startActivity(new Intent(FindActivity.this, stopActivity.class));
//            }
//        });
    }
}
