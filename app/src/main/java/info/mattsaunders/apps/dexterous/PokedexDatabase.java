package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Class to load database from assets file using SQLiteAssetHelper
 */
public class PokedexDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "pokedex.db";
    private static final int DATABASE_VERSION = 1;

    public PokedexDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getPokemonTable() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"0 _id", "identifier", "species_id", "height", "weight"};
        String sqlTables = "pokemon";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getPokemonStatsTable() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"0 _id", "pokemon_id", "stat_id", "base_stat"};
        String sqlTables = "pokemon_stats";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getPokemonTypesTable() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"0 _id", "pokemon_id", "type_id", "slot"};
        String sqlTables = "pokemon_types";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public ArrayList<Pokemon> getPokemonDBList() {
        ArrayList<Pokemon> pokemonArrayList = new ArrayList<Pokemon>();

        Cursor cursorPokemon = getPokemonTable();
        Cursor cursorStats = getPokemonStatsTable();
        Cursor pokemonTypes = getPokemonTypesTable();
        for (int i = 0; i < cursorPokemon.getCount(); i++) {
            //Pokemon table
            String name = cursorPokemon.getString(1);
            int speciesId = cursorPokemon.getInt(2);
            int height = cursorPokemon.getInt(3);
            int weight = cursorPokemon.getInt(4);
            //Stats table
            int[] stats = new int[6];
            for (int j = 0; j < 6; j++) {
                stats[j] = cursorStats.getInt(3);
                cursorStats.moveToNext();
            }
            //Types table
            //Maybe select * from types table where pokemon_id = pokemon number

            System.out.println(
                    "Pokemon from DB:      " + name + ": " + speciesId + " - Height: " + height + " - Weight: " + weight + " \n" +
                    "Stats: " + stats[0] + ", " + stats[1] + ", " + stats[2] + ", " + stats[3] + ", " + stats[4] + ", " + stats[5]
            );
            cursorPokemon.moveToNext();
        }

        return pokemonArrayList;
    }
}
