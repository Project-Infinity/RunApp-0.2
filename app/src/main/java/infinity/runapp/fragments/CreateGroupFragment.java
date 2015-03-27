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
 * Created by adc on 3/9/15.
 */
public class CreateGroupFragment extends Fragment
{
    JSONParser mJSONParser = new JSONParser();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String URL = "http://cgi.soic.indiana.edu/~team36/infinity/create_group.php";

    private EditText mGroupName, mGroupMotto;

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.create_group_layout,container,false);

        mGroupName = (EditText)v.findViewById(R.id.groupName);
        mGroupMotto = (EditText)v.findViewById(R.id.groupMotto);
        Button mCreateGroupBtn = (Button)v.findViewById(R.id.createGroupBtn);
        mCreateGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateGroup().execute();
            }
        });

        getActivity().setTitle("Create Group");

        return v;
    }

    class CreateGroup extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(String... args){

            try{
                int success;
                String name = mGroupName.getText().toString();
                String motto = mGroupMotto.getText().toString();
                String email = getUserEmail();

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userEmail", email));
                params.add(new BasicNameValuePair("groupName", name));
                params.add(new BasicNameValuePair("groupMotto", motto));

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

            goGroups();
        }
    }

    public void goGroups(){
        Fragment groups = new GroupsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, groups)
                .commit();
    }

    public String getUserEmail(){
        InfinityDBHandler dbHandler = new InfinityDBHandler(getActivity(), null, null, 1);

        User myUser = dbHandler.setUser();

        return String.valueOf(myUser.getEmail());
    }

}
