package josephbalawejder.ca.scavengerhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TakePicActivity extends AppCompatActivity {

    private VisualRecognition vrClient;
    private CameraHelper helper;

    private String item;
    private String correct = "Correct!";
    private String incorrect = "Incorrect :(";
    private ArrayList<String> items;
    private boolean itemCorrect;
    public long time_remaining;
    public int score;

    public CountDownTimer newtimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);

        items = getIntent().getStringArrayListExtra("ITEMS");
        item = items.get(0).toLowerCase();

        time_remaining = getIntent().getExtras().getLong("TIME");
        score = getIntent().getExtras().getInt("SCORE");

        //start the timer
        newtimer = new CountDownTimer(time_remaining, 1000){
            public void onTick(long millisUntilFinished) {
                time_remaining = millisUntilFinished;
            }
            public void onFinish() {}
        };
        newtimer.start();


        // Initialize Visual Recognition client
        vrClient = new VisualRecognition(
                VisualRecognition.VERSION_DATE_2016_05_20,
                getString(R.string.api_key)
        );

        itemCorrect = false;

        // Initialize camera helper
        helper = new CameraHelper(this);
        helper.dispatchTakePictureIntent();
    }

    public void takePicture(View view) {
        TextView itemFound = findViewById(R.id.header);

        if(itemFound.getText() == correct){
            items.remove(0);
            score++;
        }

        //call findActivity with the items
        Intent findActivity = new Intent(TakePicActivity.this, FindActivity.class);
        findActivity.putExtra("ITEMS", items);
        findActivity.putExtra("TIME", time_remaining);
        findActivity.putExtra("SCORE", score);
        startActivity(findActivity);
        finish();
//        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {

            newtimer.cancel();

            //text box on the top of the screen
            TextView itemFound = findViewById(R.id.header);
            itemFound.setText("Processing Image ...");
            itemFound.setTextColor(0xFF000000);

            TextView detectedObjects = findViewById(R.id.detected_objects);
            String msg = "Looking for " + item;
            detectedObjects.setText(msg);

            Button nextScreen = findViewById(R.id.next_screen);
            nextScreen.setVisibility(View.INVISIBLE);


            final Bitmap photo = helper.getBitmap(resultCode);
            final File photoFile = helper.getFile(resultCode);
            ImageView preview = findViewById(R.id.preview);
            preview.setImageBitmap(photo);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    VisualClassification response =
                            vrClient.classify(
                                    new ClassifyImagesOptions.Builder()
                                            .images(photoFile)
                                            .build()
                            ).execute();

                    ImageClassification classification =
                            response.getImages().get(0);

                    VisualClassifier classifier =
                            classification.getClassifiers().get(0);

                    //get the list of objects from watson
                    final StringBuffer output = new StringBuffer();
                    for(VisualClassifier.VisualClass object: classifier.getClasses()) {
                        if(object.getScore() > 0.3f)
                            output.append(object.getName())
                                    .append(", ");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView detectedObjects = findViewById(R.id.detected_objects);
                            String foundItems = "Watson found: " + output.toString();
                            detectedObjects.setText(foundItems);

                            if (output.lastIndexOf(item) == -1){
                                itemCorrect = false;
                            }else{
                                itemCorrect = true;
                            }

                            //detectedObjects.setText(Integer.toString(a));

                            //text box on the top of the screen
                            TextView itemFound = findViewById(R.id.header);
                            Button nextScreen = findViewById(R.id.next_screen);
                            nextScreen.setVisibility(View.VISIBLE);

                            if(itemCorrect){
                                itemFound.setText(correct);
                                itemFound.setTextColor(0xFF00FF00);

                                nextScreen.setText("Find next item");
                            }
                            else{
                                itemFound.setText(incorrect);
                                itemFound.setTextColor(0xFFFF0000);

                                nextScreen.setText("Try again");
                            }

                        }
                    });
                }
            });
        }



    }
}
