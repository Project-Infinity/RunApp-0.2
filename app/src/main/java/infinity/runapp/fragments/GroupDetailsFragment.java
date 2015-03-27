package infinity.runapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by ADC on 2/9/2015.
 */
public class GroupDetailsFragment extends Fragment{

    private String groupName;
    private String motto;
    private String admin;
    private int totalMembers;
    private JSONParser mJSONParser = new JSONParser();
    private static final String URL = "http://cgi.soic.indiana.edu/~team36/infinity/get_group_details.php";

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.group_details_layout,container,false);
        Bundle bundle = this.getArguments();

        groupName = bundle.getString("group");

        TextView gName = (TextView)v.findViewById(R.id.heading);

        gName.setText(groupName);

        new ShowGroupDetails().execute();

        return v;
    }

    class ShowGroupDetails extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args){
            try{
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("groupName", groupName));
                JSONObject json = mJSONParser.makeHttpRequest(URL, "POST", params);

                motto = json.getString("tagline");
                admin = json.getString("adminName");
                totalMembers = json.getInt("totalMembers");

            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (file_url != null) {
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
            }
            setDetails();
        }
    }

    public void setDetails(){
        TextView mMotto = (TextView) v.findViewById(R.id.motto);
        TextView mAdmin = (TextView) v.findViewById(R.id.admin);
        TextView mMembers = (TextView) v.findViewById(R.id.members);
        mMotto.setText(motto);
        mAdmin.setText(admin);
        mMembers.setText(String.valueOf(totalMembers));
    }
}