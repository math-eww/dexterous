package info.mattsaunders.apps.dexterous;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Matt on 21/02/2015. Shows details of a move
 */
public class MoveDialogFragment extends DialogFragment {
    @Override
    public void onStart()
    {
        super.onStart();

        TextView textView = (TextView) this.getDialog().findViewById(android.R.id.title);
        if(textView != null)
        {
            textView.setGravity(Gravity.CENTER);
        }
/*
        DisplayMetrics metrics = MainActivity.c.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = (int) Math.round(metrics.heightPixels * 0.8);

        getDialog().getWindow().setLayout(width,height);
*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.move_dialog_fragment, container, false);
        Bundle details = MainActivity.db.getMoveDetailsFromId(getArguments().getInt("resource"));

        TextView damageType = (TextView) rootView.findViewById(R.id.dialog_damagetype);
        TextView type = (TextView) rootView.findViewById(R.id.dialog_type);
        TextView power = (TextView) rootView.findViewById(R.id.dialog_power);
        TextView accuracy = (TextView) rootView.findViewById(R.id.dialog_accuracy);
        TextView pp = (TextView) rootView.findViewById(R.id.dialog_pp);
        TextView priority = (TextView) rootView.findViewById(R.id.dialog_priority);
        TextView targets = (TextView) rootView.findViewById(R.id.dialog_targets);
        TextView effects = (TextView) rootView.findViewById(R.id.dialog_effect);
        TextView effectChance = (TextView) rootView.findViewById(R.id.dialog_effectchance);

        LinearLayout topContainer = (LinearLayout) rootView.findViewById(R.id.dialog_container);


        damageType.setText(details.getString("damageType"));
        type.setText(details.getString("type"));

        int powerInt = details.getInt("power");
        if (powerInt != 0) { power.setText(String.valueOf(powerInt)); }
        else { topContainer.removeView(rootView.findViewById(R.id.dialog_powerContainer)); }

        int accuracyInt = details.getInt("accuracy");
        if (accuracyInt != 0) { accuracy.setText(String.valueOf(accuracyInt)); }
        else { topContainer.removeView(rootView.findViewById(R.id.dialog_accuracyContainer)); }

        pp.setText(String.valueOf(details.getInt("pp")));

        int priorityInt = details.getInt("priority");
        if (priorityInt != 0) { priority.setText(String.valueOf(priorityInt)); }
        else { topContainer.removeView(rootView.findViewById(R.id.dialog_priorityContainer)); }

        targets.setText(details.getString("targets"));
        effects.setText(details.getString("effect"));

        int effectChanceInt = details.getInt("effectChance");
        if (effectChanceInt != 0) { effectChance.setText(String.valueOf(effectChanceInt) + "%"); }
        else { topContainer.removeView(rootView.findViewById(R.id.dialog_effectChanceContainer)); }


        String name = details.getString("name");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        getDialog().setTitle(name);

        return rootView;
    }
}
