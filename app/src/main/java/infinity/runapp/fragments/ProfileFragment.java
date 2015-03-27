package infinity.runapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import infinity.runapp.InfinityDBHandler;
import infinity.runapp.R;
import infinity.runapp.User;

/**
 * Created by ADC on 2/9/2015.
 */
public class ProfileFragment extends Fragment
{
    TextView mName;
    TextView mEmail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.profile_layout,container,false);
        InfinityDBHandler dbHandler = new InfinityDBHandler(getActivity(), null, null, 1);

        User myUser = dbHandler.setUser();

        String fname = String.valueOf(myUser.getFname());
        String lname = String.valueOf(myUser.getLname());
        String email = String.valueOf(myUser.getEmail()).toLowerCase();

        mName = (TextView)v.findViewById(R.id.name);
        mEmail = (TextView)v.findViewById(R.id.email);

        mName.setText(fname + " " + lname );
        mEmail.setText(email);

        return v;
    }

}
