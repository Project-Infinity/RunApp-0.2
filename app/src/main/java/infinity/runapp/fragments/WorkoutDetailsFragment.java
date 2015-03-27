package infinity.runapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import infinity.runapp.JSONParser;
import infinity.runapp.R;

/**
 * Created by adc on 3/23/15.
 */
public class WorkoutDetailsFragment extends Fragment {

    private String workoutName;
    private JSONParser mJSONParser = new JSONParser();
    private static final String URL = "http://cgi.soic.indiana.edu/~team36/infinity/get_workout_details.php";
    private static final String TAG_COUNT = "count";
    private ArrayList<String> workoutDetails = new ArrayList<>();
    private ListView mListView;
    private Integer phaseCount;

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.workout_details_layout, container, false);
        Bundle bundle = this.getArguments();

        workoutName = bundle.getString("workout");

        TextView gName = (TextView) v.findViewById(R.id.heading);

        gName.setText(workoutName);

        new ShowWorkoutDetails().execute();

        return v;
    }

    class ShowWorkoutDetails extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute(){ super.onPreExecute(); }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("workoutName", workoutName));
                JSONObject json = mJSONParser.makeHttpRequest(URL, "POST", params);

                phaseCount = json.getInt(TAG_COUNT);

                for (int i = 0; i < phaseCount; i++)
                {
                    int num = i + 1;
                    String thisPhase = "phase" + Integer.toString(num);
                    String phase = String.valueOf(json.getInt(thisPhase));

                    String thisRep = "reps" + Integer.toString(num);
                    String reps = String.valueOf(json.getInt(thisRep));

                    String thisDistance = "distance" + Integer.toString(num);
                    String distance = String.valueOf(json.getDouble(thisDistance));

                    workoutDetails.add("Phase: " + phase + "     Reps: " + reps + "     Distance: " + distance + " meters");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url){
            if(file_url != null){
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
            }

            setList();
        }


    }

    public void setList(){
        mListView = (ListView) v.findViewById(R.id.workoutList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_items, R.id.list_item, workoutDetails);



        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String itemValue = (String) mListView.getItemAtPosition(position);

//                Fragment groupDetails = new GroupDetailsFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("group", itemValue);
//                groupDetails.setArguments(bundle);
//
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.beginTransaction()
//                        .replace(R.id.container, groupDetails)
//                        .commit();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Position: " + position + ", Value: " + itemValue, Toast.LENGTH_LONG).show();
            }
        });
    }

}