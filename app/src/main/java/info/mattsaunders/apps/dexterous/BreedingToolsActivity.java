package info.mattsaunders.apps.dexterous;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;


public class BreedingToolsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeding_tools);

        final ToggleButton circle = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonCircle);
        final ToggleButton circle2 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonCircle2);
        final ToggleButton circle3 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonCircle3);
        final ToggleButton circle4 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonCircle4);
        final ToggleButton circle5 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonCircle5);
        final ToggleButton triangle = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonTriangle);
        final ToggleButton triangle2 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonTriangle2);
        final ToggleButton triangle3 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonTriangle3);
        final ToggleButton triangle4 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonTriangle4);
        final ToggleButton triangle5 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonTriangle5);
        final ToggleButton square = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonSquare);
        final ToggleButton square2 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonSquare2);
        final ToggleButton square3 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonSquare3);
        final ToggleButton square4 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonSquare4);
        final ToggleButton square5 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonSquare5);
        final ToggleButton heart = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonHeart);
        final ToggleButton heart2 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonHeart2);
        final ToggleButton heart3 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonHeart3);
        final ToggleButton heart4 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonHeart4);
        final ToggleButton heart5 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonHeart5);
        final ToggleButton star = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonStar);
        final ToggleButton star2 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonStar2);
        final ToggleButton star3 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonStar3);
        final ToggleButton star4 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonStar4);
        final ToggleButton star5 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonStar5);
        final ToggleButton diamond = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonDiamond);
        final ToggleButton diamond2 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonDiamond2);
        final ToggleButton diamond3 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonDiamond3);
        final ToggleButton diamond4 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonDiamond4);
        final ToggleButton diamond5 = (ToggleButton) findViewById(R.id.breedingtools_toggleButtonDiamond5);

        Button resetButton = (Button) findViewById(R.id.breedingtools_resetbutton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setChecked(false);
                circle2.setChecked(false);
                circle3.setChecked(false);
                circle4.setChecked(false);
                circle5.setChecked(false);
                triangle.setChecked(false);
                triangle2.setChecked(false);
                triangle3.setChecked(false);
                triangle4.setChecked(false);
                triangle5.setChecked(false);
                square.setChecked(false);
                square2.setChecked(false);
                square3.setChecked(false);
                square4.setChecked(false);
                square5.setChecked(false);
                heart.setChecked(false);
                heart2.setChecked(false);
                heart3.setChecked(false);
                heart4.setChecked(false);
                heart5.setChecked(false);
                star.setChecked(false);
                star2.setChecked(false);
                star3.setChecked(false);
                star4.setChecked(false);
                star5.setChecked(false);
                diamond.setChecked(false);
                diamond2.setChecked(false);
                diamond3.setChecked(false);
                diamond4.setChecked(false);
                diamond5.setChecked(false);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_breeding_tools, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
