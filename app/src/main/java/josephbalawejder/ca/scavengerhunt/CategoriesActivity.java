package josephbalawejder.ca.scavengerhunt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.*;


public class CategoriesActivity extends AppCompatActivity {

    public ArrayList<String> colourItems = new ArrayList<String>();
    public ArrayList<String> schoolItems = new ArrayList<String>();
    public ArrayList<String> homeItems = new ArrayList<String>();
    public ArrayList<String> hackathonItems = new ArrayList<String>();
    public ArrayList<String> categoryList = new ArrayList<String>();
    public HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        homeItems.add("Bottle");
        schoolItems.add("stuff");
        colourItems.add("stuff");

        String[] hackathonValues = new String[] {"banana", "can", "mouse",
                "bottle", "keyboard", "cable", "food", "bag of chips", "wallet",
                "shoes", "watch", "keys"};

        String[] homeValues = new String[] {"shoe", "umbrella", "coat", "keys", "apple", "bottle",
                "bed", "tv", "dish", "fork", "sweater", "lamp", "bag"};
        
        for (int i = 0; i < hackathonValues.length; ++i) {
            hackathonItems.add(hackathonValues[i]);
        }


        //Create mapping
        map.put("Home", homeItems);
        map.put("Colours", colourItems);
        map.put("School", schoolItems);
        map.put("Hackathon",hackathonItems);

        final ListView listview = (ListView) findViewById(R.id.listview);
        String[] values = new String[] { "Home", "Colours", "School", "Hackathon"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final CategoriesActivity.StableArrayAdapter adapter = new CategoriesActivity.StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String category = (String) listview.getItemAtPosition(position);
                System.out.println(category);
                categoryList = map.get(category);
                Intent newActivity = new Intent(CategoriesActivity.this, FindActivity.class);
                newActivity.putExtra("ITEMS", categoryList);
                startActivity(newActivity);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
            }
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

//        @Override
//        public static shuffle_array(ArrayList<String> s){
//            Random rand = new Random();
//            randomNum = rand.nextInt();
//
//
//        }

    }
}
