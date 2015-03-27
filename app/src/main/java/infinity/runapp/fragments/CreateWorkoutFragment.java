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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import infinity.runapp.InfinityDBHandler;
import infinity.runapp.JSONParser;
import infinity.runapp.R;
import infinity.runapp.User;

/**
 * Created by adc on 3/19/15.
 */
public class CreateWorkoutFragment extends Fragment
{
    JSONParser mJSONParser = new JSONParser();

    int success;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String URL = "http://cgi.soic.indiana.edu/~team36/infinity/create_workout.php";

    private EditText mWorkoutName;

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.create_workout_layout,container,false);

        mWorkoutName = (EditText)v.findViewById(R.id.workoutName);
        Button mCreateWorkoutBtn = (Button)v.findViewById(R.id.createWorkoutBtn);
        mCreateWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateWorkout().execute();
            }
        });

        return v;
    }

    class CreateWorkout extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(String... args){

            try{
                String email = getUserEmail();
                String name = mWorkoutName.getText().toString();

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userEmail", email));
                params.add(new BasicNameValuePair("workoutName", name));

                JSONObject json = mJSONParser.makeHttpRequest(URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);

                if (success == 1)
                {
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

            if(success == 1)
            {
                goCreateWorkout();
            }
            else
            {
                mWorkoutName.setText("");
            }
        }
    }

    public String getUserEmail(){
        InfinityDBHandler dbHandler = new InfinityDBHandler(getActivity(), null, null, 1);

        User myUser = dbHandler.setUser();

        return String.valueOf(myUser.getEmail());
    }

    public void goCreateWorkout(){
        String workoutName = mWorkoutName.getText().toString();

        Fragment createWorkout = new CreateWorkoutDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("workoutName", workoutName);
        createWorkout.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, createWorkout)
                .commit();
    }

}
