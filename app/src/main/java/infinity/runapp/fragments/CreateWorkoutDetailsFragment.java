package infinity.runapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by adc on 3/19/15.
 */
public class CreateWorkoutDetailsFragment extends Fragment
{
    JSONParser mJSONParser = new JSONParser();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String URL = "http://cgi.soic.indiana.edu/~team36/infinity/add_workout_phase.php";

    private String workoutName;
    private TextView mWorkoutName;
    private TextView mWorkoutPhase;
    private EditText mWorkoutReps;
    private EditText mWorkoutDistance;

    private Integer phaseCount;

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.create_workout_details_layout,container,false);
        Bundle bundle = this.getArguments();

        phaseCount = 1;
        workoutName = bundle.getString("workoutName");

        Button mAddPhaseBtn = (Button)v.findViewById(R.id.addPhaseBtn);
        Button mDoneBtn = (Button)v.findViewById(R.id.doneBtn);
        mWorkoutName = (TextView)v.findViewById(R.id.workoutName);
        mWorkoutPhase = (TextView)v.findViewById(R.id.workoutPhase);

        mWorkoutName.setText(workoutName);
        mWorkoutPhase.setText(phaseCount.toString());

        mWorkoutReps = (EditText)v.findViewById(R.id.workoutReps);
        mWorkoutDistance = (EditText)v.findViewById(R.id.workoutDistance);

        mAddPhaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddPhase().execute();
            }
        });
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWorkoutsDetails();
            }
        });

        return v;
    }

    class AddPhase extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(String... args){

            try{
                int success;
                String name = workoutName;
                String phase = phaseCount.toString();
                String reps = mWorkoutReps.getText().toString();
                String distance = mWorkoutDistance.getText().toString();

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("workoutName", name));
                params.add(new BasicNameValuePair("workoutPhase", phase));
                params.add(new BasicNameValuePair("workoutReps", reps));
                params.add(new BasicNameValuePair("workoutDistance", distance));

                JSONObject json = mJSONParser.makeHttpRequest(URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);

                if (success == 1)
                {
                    phaseCount++;
                    return json.getString(TAG_MESSAGE);

                } else
                {
                    return json.getString(TAG_MESSAGE);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url){
            if(file_url != null)
            {
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
            }
            mWorkoutPhase.setText(phaseCount.toString());
            mWorkoutReps.setText("");
            mWorkoutDistance.setText("");
        }
    }


    public void goWorkoutsDetails(){
        Fragment fm = new WorkoutDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("workoutName", workoutName);
        fm.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fm)
                .commit();
    }

}
