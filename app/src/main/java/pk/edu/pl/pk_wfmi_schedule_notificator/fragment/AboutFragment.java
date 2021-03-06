package pk.edu.pl.pk_wfmi_schedule_notificator.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private View view;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_about, container, false);

        prepareGithubButton();
        updateVersion();

        return view;
    }

    private void prepareGithubButton() {
        Button githubButton = view.findViewById(R.id.github_button);
        githubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/kbednarz/pk-wfmi-schedule-notifier"));
                startActivity(browserIntent);
            }
        });
    }

    private void updateVersion() {
        try {
            TextView versionTextView = view.findViewById(R.id.version_view);
            PackageManager pm = getActivity().getPackageManager();
            PackageInfo info = pm.getPackageInfo(getActivity().getPackageName(), 0);

            versionTextView.append(" " + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getActionBar().setTitle(R.string.about_section);
    }

}
