package infinity.runapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
 * Created by ADC on 2/9/2015.
 */
public class GroupsFragment extends Fragment
{
    private JSONParser mJSONParser = new JSONParser();
    private static final String TAG_COUNT = "count";
    private ArrayList<String> groupList = new ArrayList<>();
    private ListView mListView;
    private static final String URL = "http://cgi.soic.indiana.edu/~team36/infinity/get_groups.php";

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.groups_layout,container,false);

        new ShowGroups().execute();

        return v;
    }

    class ShowGroups extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args){

            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userEmail", getUserEmail()));
                JSONObject json = mJSONParser.makeHttpRequest(URL, "POST", params);

                int count = json.getInt(TAG_COUNT);

                for(int i = 0; i < count; i++)
                {
                    int num = i + 1;
                    String thisGroup = "group" + Integer.toString(num);
                    String group = json.getString(thisGroup);
                    groupList.add(group);
                }

            }catch (JSONException e){
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

    public String getUserEmail(){
        InfinityDBHandler dbHandler = new InfinityDBHandler(getActivity(), null, null, 1);

        User myUser = dbHandler.setUser();

        return String.valueOf(myUser.getEmail());
    }

    public void setList(){
        mListView = (ListView) v.findViewById(R.id.groupList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_items, R.id.list_item, groupList);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String itemValue = (String) mListView.getItemAtPosition(position);

                Fragment groupDetails = new GroupDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("group", itemValue);
                groupDetails.setArguments(bundle);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, groupDetails)
                        .commit();
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Position: " + position + ", Value: " + itemValue, Toast.LENGTH_LONG).show();
            }
        });
    }
}
