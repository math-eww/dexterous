package info.mattsaunders.apps.dexterous;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class PokemonDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        Intent intent = getIntent();
        int pokePosition = intent.getIntExtra("pokemon", 0);
        Pokemon poke = MainActivity.pokemonList.get(pokePosition);

        ImageView pokeSprite = (ImageView) findViewById(R.id.pokeDetailsImage);
        TextView pokeName = (TextView) findViewById(R.id.pokeDetailsName);
        TextView pokeNum = (TextView) findViewById(R.id.pokeDetailsNum);
        TextView pokeTypes = (TextView) findViewById(R.id.pokeDetailsTypes);
        TextView pokeHeightWeight = (TextView) findViewById(R.id.pokeDetailsHeighWeight);

        TextView pokeHp = (TextView) findViewById(R.id.pokeDetailsHp);
        TextView pokeAtk = (TextView) findViewById(R.id.pokeDetailsAtk);
        TextView pokeDef = (TextView) findViewById(R.id.pokeDetailsDef);
        TextView pokeSpAtk = (TextView) findViewById(R.id.pokeDetailsSpAtk);
        TextView pokeSpDef = (TextView) findViewById(R.id.pokeDetailsSpDef);
        TextView pokeSpd = (TextView) findViewById(R.id.pokeDetailsSpd);
        TextView pokeTtl = (TextView) findViewById(R.id.pokeDetailsTotals);

        Bitmap sprite = BitmapFactory.decodeFile(poke.getSpriteFile().getAbsolutePath());
        pokeSprite.setImageBitmap(sprite);

        String types = poke.getTypeOne();
        if (!poke.getTypeTwo().equals("")) { types = types + " | " + poke.getTypeTwo(); }

        pokeName.setText(poke.getName());
        pokeNum.setText(poke.getStringNumber());
        pokeTypes.setText(types);
        pokeHeightWeight.setText("Height: " + poke.getHeight() + " | " + "Weight: " + poke.getWeight());

        int[] stats = poke.getStats();
        pokeHp.setText("HP: " + Integer.toString(stats[0]));
        pokeAtk.setText("Attack: " + Integer.toString(stats[1]));
        pokeDef.setText("Defense: " + Integer.toString(stats[2]));
        pokeSpAtk.setText("Sp. Atk: " + Integer.toString(stats[3]));
        pokeSpDef.setText("Sp. Def: " + Integer.toString(stats[4]));
        pokeSpd.setText("Speed: " + Integer.toString(stats[5]));
        int total = stats[0] + stats[1] + stats[2] + stats[3] + stats[4] + stats[5];
        pokeTtl.setText("Total: " + Integer.toString(total));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pokemon_details, menu);
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
