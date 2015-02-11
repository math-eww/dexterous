package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Class to load database from assets file using SQLiteAssetHelper
 */
public class PokedexDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "pokedex.db";
    private static final int DATABASE_VERSION = 1;
    private static final int POKEMON_VERSION = 15;
    private static final String TAG = "PokedexDatabase";

    public PokedexDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor queryDatabase(
            String table, String[] columnNames, String whereClause, String[] selectionArgs,
            String groupBy, String having, String orderBy) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(table, columnNames, whereClause, selectionArgs, groupBy, having, orderBy);
        c.moveToFirst();
        return c;
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

        String [] sqlSelect = {"0 _id", "id", "identifier"};
        String sqlTables = "types";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public String getAbilityFromId(int id) {
        Cursor c = queryDatabase("abilities", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String ability = c.getString(0);
        c.close();
        return ability;
    }

    public String getEggGroupFromId(int id) {
        Cursor c = queryDatabase("egg_groups", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String eggGroup = c.getString(0);
        c.close();
        return eggGroup;
    }

    public String getMoveFromId(int id) {
        Cursor c = queryDatabase("moves", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String moveName = c.getString(0);
        c.close();
        return moveName;
    }

    public String getMoveLearnMethodFromId(int id) {
        Cursor c = queryDatabase("pokemon_move_methods", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String moveLearn = c.getString(0);
        c.close();
        return moveLearn;
    }

    public String getEvolutionTriggerFromId(int id) {
        Cursor c = queryDatabase("evolution_triggers", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String evoTrigger = c.getString(0);
        c.close();
        return evoTrigger;
    }

    public String getPokemonFromId(int id) {
        Cursor c = queryDatabase("pokemon", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String name = c.getString(0);
        String output = name.substring(0, 1).toUpperCase() + name.substring(1);
        c.close();
        return output;
    }

    public String getItemFromId(int id) {
        Cursor c = queryDatabase("items", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String itemName = c.getString(0);
        c.close();
        return itemName;
    }

    public ArrayList<Ability> getAbilities(int speciesId) {
        ArrayList<Ability> abilityArrayList = new ArrayList<Ability>();
        //Abilities query
        Cursor abilitiesQuery = queryDatabase("pokemon_abilities", new String[] {"ability_id","is_hidden"},"pokemon_id="+speciesId, null, null, null, null);
        for (int z = 0; z < abilitiesQuery.getCount(); z++) {
            //Build a new ability object and add to arraylist
            abilityArrayList.add(new Ability(
                    getAbilityFromId(abilitiesQuery.getInt(0)),
                    abilitiesQuery.getInt(0)
            ));
            abilitiesQuery.moveToNext();
        }
        abilitiesQuery.close();
        return abilityArrayList;
    }

    public ArrayList<EggGroup> getEggGroups(int speciesId){
        ArrayList<EggGroup> eggGroupsArrayList = new ArrayList<EggGroup>();
        //Egg groups
        Cursor eggGroupQuery = queryDatabase("pokemon_egg_groups", new String[] {"egg_group_id"},"species_id="+speciesId, null, null, null, null);
        for (int y = 0; y < eggGroupQuery.getCount(); y++) {
            //Build a new egg group object and add to arraylist
            eggGroupsArrayList.add(new EggGroup(
                    getEggGroupFromId(eggGroupQuery.getInt(0)),
                    eggGroupQuery.getInt(0)
            ));
            eggGroupQuery.moveToNext();
        }
        eggGroupQuery.close();
        return eggGroupsArrayList;
    }

    public ArrayList<Move> getMoveset(int speciesId) {
        ArrayList<Move> movesArrayList = new ArrayList<Move>();
        //Moveset
        Cursor movesetQuery = queryDatabase("pokemon_moves", new String[] {"move_id","pokemon_move_method_id","level"},
                "pokemon_id="+speciesId+" and version_group_id="+POKEMON_VERSION, null, null, null, null);
        for (int x = 0; x < movesetQuery.getCount(); x++) {
            //Build a new move object and add to arraylist
            Move tempMove = new Move(
                    getMoveFromId(movesetQuery.getInt(0)),
                    movesetQuery.getInt(0),
                    getMoveLearnMethodFromId(movesetQuery.getInt(1))
            );
            if (movesetQuery.getInt(1) == 1) {
                tempMove.setLevel(movesetQuery.getInt(2));
            }
            movesArrayList.add(tempMove);
            movesetQuery.moveToNext();
        }
        movesetQuery.close();
        return movesArrayList;
    }

    public ArrayList<Evolution> getEvolutions(int speciesId) {
        ArrayList<Evolution> evolutionsArrayList = new ArrayList<Evolution>();
        //Evolutions
        Cursor evolutionQueryInto = queryDatabase(
                "pokemon_species",
                new String[] { "id","identifier" },
                "evolves_from_species_id="+speciesId,
                null, null, null, null
        );
        for (int w = 0; w < evolutionQueryInto.getCount(); w++) {
            int evolvesIntoId = evolutionQueryInto.getInt(0);
            Cursor evolutionQuery = queryDatabase(
                    "pokemon_evolution",
                    new String[] {
                            "evolved_species_id","evolution_trigger_id","trigger_item_id","minimum_level",
                            //                "gender_id","location_id","held_item_id","time_of_day","known_move_id","known_move_type_id",
                            //                "minimum_happiness","minimum_beauty","minimum_affection","relative_physical_stats",
                            //                "party_species_id","party_type_id","trade_species_id",
                            //                "needs_overworld_rain","turn_upside_down"
                    },
                    "evolved_species_id="+evolvesIntoId,
                    null, null, null, null
            );

            String detail = "";
            int level = 0;
            switch (evolutionQuery.getInt(1)) {
                case 1:
                    level = evolutionQuery.getInt(3);
                    break;
                case 2:
                    break;
                case 3:
                    detail = getItemFromId(evolutionQuery.getInt(2));
                    break;
                case 4:
                    break;
            }
            evolutionsArrayList.add(new Evolution(
                    getEvolutionTriggerFromId(evolutionQuery.getInt(1)),
                    getPokemonFromId(evolutionQuery.getInt(0)),
                    String.valueOf(evolutionQuery.getInt(0)),
                    detail,
                    level

            ));
            evolutionQuery.close();
            evolutionQueryInto.moveToNext();
        }
        evolutionQueryInto.close();
        return evolutionsArrayList;
    }

    public String getEvolvesFrom(int speciesId) {
        Cursor evolvesFrom = queryDatabase("pokemon_species", new String[] {"evolves_from_species_id"},
                "id="+speciesId, null, null, null, null);
        if (evolvesFrom.getCount() > 0 && evolvesFrom.getInt(0) != 0) {
            return getPokemonFromId(evolvesFrom.getInt(0));
        }
        return "";
    }

    public void setEvolvesFrom(int speciesId) {
        Cursor evolvesFrom = queryDatabase("pokemon_species", new String[] {"evolves_from_species_id"},
                "id="+speciesId, null, null, null, null);
        if (evolvesFrom.getCount() > 0 && evolvesFrom.getInt(0) != 0) {
            Pokemon poke = MainActivity.pokemonList.get(speciesId-1);
            poke.setEvolvesFrom(getPokemonFromId(evolvesFrom.getInt(0)));
            poke.setEvolvesFromNum(evolvesFrom.getInt(0));
        }
    }

    public ArrayList<Pokemon> getPokemonList(Bundle pokeball1, Bundle pokeball2, Bundle pokeball3) {
        ArrayList<Pokemon> pokemonArrayList = new ArrayList<Pokemon>();

        Log.i(TAG,"Beginning Pokedex Load from DB");

        Cursor cursorTypes = getPokemonTypesTable();
        String[] typeNames = new String[cursorTypes.getCount()];
        for (int t = 0; t < cursorTypes.getCount(); t++) {
            typeNames[t] = cursorTypes.getString(2);
            cursorTypes.moveToNext();
        }
        cursorTypes.close();

        Log.i(TAG,"Stage 1 complete");

        Cursor cursorPokemon = getPokemonTable();
        Cursor cursorStats = getPokemonStatsTable();
        Log.i(TAG,"Entering main loop");
        //for (int i = 0; i < cursorPokemon.getCount(); i++) {
        for (int i = 0; i < MainActivity.getTotalPokes(); i++) {

            //Pokemon table
            String name = cursorPokemon.getString(1);
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            int speciesId = cursorPokemon.getInt(2);
            int height = cursorPokemon.getInt(3);
            int weight = cursorPokemon.getInt(4);

            //Stats table
            int[] stats = new int[6];
            for (int j = 0; j < 6; j++) {
                stats[j] = cursorStats.getInt(3);
                cursorStats.moveToNext();
            }

            //Types query
            Cursor types = queryDatabase("pokemon_types", new String[]{"pokemon_id", "type_id", "slot"}, "pokemon_id=" + speciesId, null, null, null, null);
            String type1 = typeNames[types.getInt(1) - 1];
            String type2 = "";
            if (types.getCount() > 1) {
                types.moveToNext();
                type2 = typeNames[types.getInt(1) - 1];
            }
            types.close();

            pokemonArrayList.add(new Pokemon(
                    i + 1,
                    name,
                    type1,
                    type2,
                    stats[0],
                    stats[1],
                    stats[2],
                    stats[3],
                    stats[4],
                    stats[5],
                    String.valueOf(height),
                    String.valueOf(weight),
                    null,
                    null,
                    null,
                    null
            ));
            cursorPokemon.moveToNext();
        }
        Log.i(TAG,"Exiting main loop");
        cursorPokemon.close();
        cursorStats.close();

        for (Pokemon poke : pokemonArrayList) {
            //System.out.println(poke.getSummary());
            //Set toggle states here
            if (pokeball1 != null) {
                if (pokeball1.getInt(poke.getStringNumber()) == 1) {
                    poke.setPokeballToggle1(true);
                    MainActivity.caughtDex++;
                }
            }
            if (pokeball2 != null) {
                if (pokeball2.getInt(poke.getStringNumber()) == 1) {
                    poke.setPokeballToggle2(true);
                    MainActivity.livingDex++;
                }
            }
            if (pokeball3 != null) {
                if (pokeball3.getInt(poke.getStringNumber()) == 1) {
                    poke.setPokeballToggle3(true);
                }
            }
        }

        return pokemonArrayList;
    }
}
