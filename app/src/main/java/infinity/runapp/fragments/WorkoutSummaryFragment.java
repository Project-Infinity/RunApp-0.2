package infinity.runapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import infinity.runapp.R;

/**
 * Created by ADC on 2/9/2015.
 */
public class WorkoutSummaryFragment extends Fragment
{
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.workout_summary_layout,container,false);

        TextView workoutTime = (TextView) v.findViewById(R.id.workoutTime);
        TextView workoutDistance = (TextView) v.findViewById(R.id.workoutDistance);
        TextView workoutSpeed = (TextView) v.findViewById(R.id.workoutSpeed);

        Intent in = getActivity().getIntent();

        Bundle b = in.getExtras();

        long time = b.getLong("time");
        long seconds = time/1000;
        String timeString = Long.toString(seconds);

        float distance = b.getFloat("distance");
        String distString = Float.toString(distance);

        float metersPerSecond = distance/seconds;
        String mpsString = Float.toString(metersPerSecond);

        workoutTime.setText(timeString);
        workoutDistance.setText(distString);
        workoutSpeed.setText(mpsString);

        return v;
    }
}
