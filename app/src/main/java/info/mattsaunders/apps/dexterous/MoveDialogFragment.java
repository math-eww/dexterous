package info.mattsaunders.apps.dexterous;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Matt on 21/02/2015.
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

        damageType.setText(details.getString("damageType"));
        type.setText(details.getString("type"));
        power.setText(String.valueOf(details.getInt("power")));
        accuracy.setText(String.valueOf(details.getInt("accuracy")));
        pp.setText(String.valueOf(details.getInt("pp")));
        priority.setText(String.valueOf(details.getInt("priority")));
        targets.setText(details.getString("targets"));
        effects.setText(details.getString("effect"));
        effectChance.setText(String.valueOf(details.getInt("effectChance")));

        String name = details.getString("name");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        getDialog().setTitle(name);

        return rootView;
    }
}
