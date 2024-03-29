package gist.telecontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ControlFragment extends Fragment {

    public static ControlFragment newInstance(String name) {
        ControlFragment myFragment = new ControlFragment();

        Bundle args = new Bundle();
        args.putString("name", name);
        myFragment.setArguments(args);

        return myFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control, container, false);
        ((TextView)v.findViewById(R.id.device_name_title)).setText("Device name: " + getArguments().getString("name"));
        return v;
    }
}