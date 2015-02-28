package info.mattsaunders.apps.dexterous;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Matt on 21/02/2015. Dialog fragment to show ability details
 */
public class AbilityDialogFragment extends DialogFragment {
    @Override
    public void onStart()
    {
        super.onStart();

        TextView textView = (TextView) this.getDialog().findViewById(android.R.id.title);
        if(textView != null)
        {
            textView.setGravity(Gravity.CENTER);
        }

        DisplayMetrics metrics = MainActivity.c.getResources().getDisplayMetrics();
        int width = (int) Math.round(metrics.widthPixels * 0.8);
        int height = (int) Math.round(metrics.heightPixels * 0.7);

        getDialog().getWindow().setLayout(width,height);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ability_dialog_fragment, container, false);

        TextView abilityOneName = (TextView) rootView.findViewById(R.id.dialog_AbilityOneName);
        TextView abilityOneText = (TextView) rootView.findViewById(R.id.dialog_AbilityOneText);
        TextView abilityTwoName = (TextView) rootView.findViewById(R.id.dialog_AbilityTwoName);
        TextView abilityTwoText = (TextView) rootView.findViewById(R.id.dialog_AbilityTwoText);
        TextView abilityThreeName = (TextView) rootView.findViewById(R.id.dialog_AbilityThreeName);
        TextView abilityThreeText = (TextView) rootView.findViewById(R.id.dialog_AbilityThreeText);

        PokedexDatabase db = MainActivity.db;
        int totalAbilities = getArguments().getInt("total");
        int resource;
        for (int x = 0; x < totalAbilities; x++) {
            resource = getArguments().getInt("ability" + x);
            String abilityName = db.getAbilityFromId(resource);
            String abilityText = db.getAbilityDetailsFromId(resource);
            if (abilityText.contains("[")) {
                //Log.i("Ability Dialog","Cutting out extra text: Original: " + abilityText);
                abilityText = abilityText
                        .replaceAll("\\[|\\]","")
                        .replaceAll("\\{mechanic:(.*?)\\}","")
                        .replaceAll("\\{type:|\\}","")
                        .replaceAll("\\{move:","")
                        .replaceAll("\\{ability:", "");
            }
            if (x == 0) { abilityOneName.setText(abilityName); abilityOneText.setText(abilityText); }
            else if (x == 1) { abilityTwoName.setText(abilityName); abilityTwoText.setText(abilityText); }
            else if (x == 2) { abilityThreeName.setText(abilityName); abilityThreeText.setText(abilityText); }
        }

        getDialog().setTitle("Abilities");

        return rootView;
    }
}

