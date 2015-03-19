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
    //private static final int POKEMON_VERSION = 15;
    private static final String TAG = "PokedexDatabase";
    private SQLiteDatabase db = getReadableDatabase();
    private String[] typeNames;

    public PokedexDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //db.setMaxSqlCacheSize(50);
    }

    public Cursor queryDatabase(
            String table, String[] columnNames, String whereClause, String[] selectionArgs,
            String groupBy, String having, String orderBy) {
        Cursor c = db.query(table, columnNames, whereClause, selectionArgs, groupBy, having, orderBy);
        c.moveToFirst();
        return c;
    }

    public Cursor getPokemonTable() {
        //SQLiteDatabase db = getReadableDatabase();
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
        //SQLiteDatabase db = getReadableDatabase();
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
        //SQLiteDatabase db = getReadableDatabase();
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

    public String getAbilityDetailsFromId(int id) {
        Cursor c = queryDatabase("ability_prose", new String[] {"short_effect"},"ability_id="+id, null, null, null, null);
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
        Cursor c = queryDatabase("move_names_english", new String[] {"name"},"move_id="+id, null, null, null, null);
        c.moveToFirst();
        String moveName = c.getString(0);
        c.close();
        return moveName;
    }

    public String getMoveTypeFromId(int id) {
        Cursor c = queryDatabase("moves", new String[] {"type_id"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String moveType = typeNames[c.getInt(0)-1]; //getTypeFromId(c.getInt(0));
        c.close();
        return moveType;
    }

    public Bundle getMoveDetailsFromId(int id) {
        String[] damageClasses = {"status","physical","special"};
        Cursor c = queryDatabase("moves", new String[] {"type_id","power","pp",
                "accuracy","priority","target_id","damage_class_id","effect_id","effect_chance","identifier"},
                "id="+id, null, null, null, null);
        c.moveToFirst();

        Bundle bundle = new Bundle();
        bundle.putString("type",getTypeFromId(c.getInt(0)));
        bundle.putInt("power",c.getInt(1));
        bundle.putInt("pp",c.getInt(2));
        bundle.putInt("accuracy",c.getInt(3));
        bundle.putInt("priority",c.getInt(4));
        bundle.putString("targets",getMoveTargetFromId(c.getInt(5)));
        bundle.putString("damageType",damageClasses[c.getInt(6)-1]);
        bundle.putString("effect",getMoveEffectFromId(c.getInt(7)));
        bundle.putInt("effectChance", c.getInt(8));
        bundle.putString("name",c.getString(9));

        c.close();
        return bundle;
    }

    public Bundle getBasicMoveDetailsFromId(int id) {
        String[] damageClasses = {"status","physical","special"};
        Cursor c = queryDatabase("moves", new String[] {"power","accuracy","damage_class_id"},
                "id="+id, null, null, null, null);
        c.moveToFirst();

        Bundle bundle = new Bundle();
        bundle.putInt("power",c.getInt(0));
        bundle.putInt("accuracy",c.getInt(1));
        bundle.putString("damageType",damageClasses[c.getInt(2)-1]);

        c.close();
        return bundle;
    }

    public String getMoveEffectFromId(int id) {
        Cursor c = queryDatabase("move_effect_prose", new String[] {"short_effect"},"move_effect_id="+id, null, null, null, null);
        c.moveToFirst();
        String effect = c.getString(0);
        c.close();
        return effect;
    }

    public String getMoveTargetFromId(int id) {
        Cursor c = queryDatabase("move_targets", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String target = c.getString(0);
        c.close();
        return target;
    }

    public String getTypeFromId(int id) {
        Cursor c = queryDatabase("types", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String typeName = c.getString(0);
        c.close();
        return typeName;
    }
/*
    public String getMoveLearnMethodFromId(int id) {
        Cursor c = queryDatabase("pokemon_move_methods", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String moveLearn = c.getString(0);
        c.close();
        return moveLearn;
    }
*/
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
/*
    public String getItemFromId(int id) {
        Cursor c = queryDatabase("items", new String[] {"identifier"},"id="+id, null, null, null, null);
        c.moveToFirst();
        String itemName = c.getString(0);
        c.close();
        return itemName;
    }
*/
    public String getItemNameFromId(int id) {
        Cursor c = queryDatabase("item_names_english", new String[] {"name"},"item_id="+id, null, null, null, null);
        c.moveToFirst();
        String itemName = c.getString(0);
        c.close();
        return itemName;
    }

    public String getLocationFromId(int id) {
        Cursor c = queryDatabase("location_names_english", new String[] {"name"},"location_id="+id+" and local_language_id=9", null, null, null, null);
        c.moveToFirst();
        String placeName = c.getString(0);
        c.close();
        return placeName;
    }

    public ArrayList<Ability> getAbilities(int speciesId) {
        ArrayList<Ability> abilityArrayList = new ArrayList<>();
        //Abilities query
        Cursor abilitiesQuery = queryDatabase("pokemon_abilities", new String[] {"ability_id","is_hidden"},"pokemon_id="+speciesId, null, null, null, null);
        int abilitiesQueryCount = abilitiesQuery.getCount();
        for (int z = 0; z < abilitiesQueryCount; z++) {
            //Build a new ability object and add to arraylist
            abilityArrayList.add(new Ability(
                    getAbilityFromId(abilitiesQuery.getInt(0)),
                    abilitiesQuery.getInt(0),
                    abilitiesQuery.getInt(1)
            ));
            abilitiesQuery.moveToNext();
        }
        abilitiesQuery.close();
        return abilityArrayList;
    }

    public ArrayList<EggGroup> getEggGroups(int speciesId){
        ArrayList<EggGroup> eggGroupsArrayList = new ArrayList<>();
        //Egg groups
        Cursor eggGroupQuery = queryDatabase("pokemon_egg_groups", new String[] {"egg_group_id"},"species_id="+speciesId, null, null, null, null);
        int eggGroupQueryCount = eggGroupQuery.getCount();
        for (int y = 0; y < eggGroupQueryCount; y++) {
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
        ArrayList<Move> movesArrayList = new ArrayList<>();
        Cursor movesetQuery = queryDatabase("pokemon_moves_current", new String[]{"move_id", "pokemon_move_method_id", "level"},
                "pokemon_id=" + speciesId, null, null, null, null);
        int moveseQueryCount = movesetQuery.getCount();
        int moveResource;
        int moveLearnType;
        for (int x = 0; x < moveseQueryCount; x++) {
            //Build a new move object and add to arraylist
            moveResource = movesetQuery.getInt(0);
            moveLearnType = movesetQuery.getInt(1);
            Move tempMove = new Move(
                    getMoveFromId(moveResource),
                    moveResource,
                    moveLearnType,
                    getMoveTypeFromId(moveResource)
            );
            if (moveLearnType == 1) {
                tempMove.level = movesetQuery.getInt(2);
            }
            movesArrayList.add(tempMove);
            movesetQuery.moveToNext();
        }
        movesetQuery.close();
        return movesArrayList;
    }

    public ArrayList<Evolution> getEvolutions(int speciesId) {
        ArrayList<Evolution> evolutionsArrayList = new ArrayList<>();
        //Evolutions
        Cursor evolutionQueryInto = queryDatabase(
                "pokemon_species",
                new String[] { "id","identifier" },
                "evolves_from_species_id="+speciesId,
                null, null, null, null
        );
        String[] columns = new String[] {
                "evolved_species_id","evolution_trigger_id","minimum_level","trigger_item_id",
                "gender_id","location_id","held_item_id","time_of_day","known_move_id","known_move_type_id",
                "minimum_happiness","minimum_beauty","minimum_affection","relative_physical_stats",
                "party_species_id","party_type_id","trade_species_id",
                "needs_overworld_rain","turn_upside_down"
        };
        int evolutionQueryIntoCount = evolutionQueryInto.getCount();
        for (int w = 0; w < evolutionQueryIntoCount; w++) {
            int evolvesIntoId = evolutionQueryInto.getInt(0);
            Cursor evolutionQuery = queryDatabase(
                    "pokemon_evolution",
                    columns,
                    "evolved_species_id="+evolvesIntoId,
                    null, null, null, null
            );

            String detail = "";
            int level = 0;
            switch (evolutionQuery.getInt(1)) {
                case 1: //level up
                    level = evolutionQuery.getInt(2);
                    if (level == 0) {
                        detail = "levelUp=Other,";
                        //Level up but no specific level - get specific condition
                    }
                    break;
                case 2: //trade
                    break;
                case 3: //use item
                    //detail = getItemFromId(evolutionQuery.getInt(2));
                    break;
                case 4: //shed
                    break;
            }
            for (int j = 0; j < evolutionQuery.getCount(); j++) {
                for (int i = 3; i < columns.length; i++) {
                    if (!evolutionQuery.getString(i).equals("") && !evolutionQuery.getString(i).equals("0")) {
                        detail = detail + columns[i] + "=" + evolutionQuery.getString(i) + ","; // Can be separated with split "," and then value extracted with split "="
                    }
                }
                evolutionQuery.moveToNext();
            }
            evolutionQuery.moveToFirst();
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

    public ArrayList<Evolution> getForms(int speciesId) {
        ArrayList<Evolution> pokemonForms = new ArrayList<>();
        //Add megas & forms
        Cursor pokemonFormsQuery = queryDatabase("pokemon",new String[] {"id","identifier","species_id"},"species_id="+speciesId+" and id>"+10000,null,null,null,null);
        for (int i = 0; i < pokemonFormsQuery.getCount()-1; i++) {
            Cursor pokemonFormsIdQuery = queryDatabase("pokemon_forms", new String[] {"form_identifier","is_mega"},"pokemon_id="+pokemonFormsQuery.getInt(0),null,null,null,null);
            String detail = "";
            if (pokemonFormsIdQuery.getInt(1) == 1) { detail = "mega"; }
            pokemonForms.add(new Evolution(
                    pokemonFormsIdQuery.getString(0),
                    pokemonFormsQuery.getString(1),
                    pokemonFormsQuery.getString(2),
                    detail,
                    pokemonFormsQuery.getInt(0)
            ));
            pokemonFormsQuery.moveToNext();
            pokemonFormsIdQuery.close();
        }
        pokemonFormsQuery.close();
        return pokemonForms;
    }

    public int[] getStats(int speciesId) {
        Cursor stats = queryDatabase("pokemon_stats", new String[] {"base_stat"}, "pokemon_id="+speciesId,null,null,null,null);
        int statsCount = stats.getCount();
        int[] pokemonStats = new int[statsCount];
        for (int i = 0; i < statsCount; i++) {
            pokemonStats[i] = stats.getInt(0);
            stats.moveToNext();
        }
        stats.close();
        return pokemonStats;
    }

    public String getPokemonTypesStringFromId(int speciesId) {
        Cursor typesQuery = queryDatabase("pokemon_types", new String[] {"type_id"},"pokemon_id="+speciesId,null,null,null,null);
        String types = typeNames[typesQuery.getInt(0)-1];
        if (typesQuery.getCount() > 1) {
            typesQuery.moveToNext();
            types = types + " | " + typeNames[typesQuery.getInt(0)-1];
        }
        typesQuery.close();
        return types;
    }

    public String[] getPokemonHeightWeight(int speciesId) {
        Cursor pokemonHWQuery = queryDatabase("pokemon", new String[] {"height","weight"},"id="+speciesId,null,null,null,null);
        String[] pokemonHW = new String[2];
        pokemonHW[0] = pokemonHWQuery.getString(0);
        pokemonHW[1] = pokemonHWQuery.getString(1);
        pokemonHWQuery.close();
        return pokemonHW;
    }
/*
    public String getEvolvesFrom(int speciesId) {
        Cursor evolvesFrom = queryDatabase("pokemon_species", new String[] {"evolves_from_species_id"},
                "id="+speciesId, null, null, null, null);
        if (evolvesFrom.getCount() > 0 && evolvesFrom.getInt(0) != 0) {
            return getPokemonFromId(evolvesFrom.getInt(0));
        }
        return "";
    }
*/
    public void setEvolvesFrom(int speciesId) {
        Cursor evolvesFrom = queryDatabase("pokemon_species", new String[] {"evolves_from_species_id"},
                "id="+speciesId, null, null, null, null);
        if (evolvesFrom.getCount() > 0 && evolvesFrom.getInt(0) != 0) {
            Pokemon poke = Global.pokemonList.get(speciesId-1);
            poke.setEvolvesFrom(getPokemonFromId(evolvesFrom.getInt(0)));
            poke.setEvolvesFromNum(evolvesFrom.getInt(0));
        }
    }

    public ArrayList<Pokemon> getPokemonList(Bundle pokeball1, Bundle pokeball2, Bundle pokeball3) {
        ArrayList<Pokemon> pokemonArrayList = new ArrayList<>();

        Log.i(TAG,"Beginning Stage 1: Pokedex Load from DB");

        Cursor cursorTypes = getPokemonTypesTable();
        typeNames = new String[cursorTypes.getCount()];
        int cursorTypesCount = cursorTypes.getCount();
        for (int t = 0; t < cursorTypesCount; t++) {
            typeNames[t] = cursorTypes.getString(2);
            cursorTypes.moveToNext();
        }
        cursorTypes.close();

        Cursor cursorPokemon = getPokemonTable();
        Cursor cursorStats = getPokemonStatsTable();
        Log.i(TAG,"Stage 1 Complete: Beginning Stage 2: Entering main loop");
        //for (int i = 0; i < cursorPokemon.getCount(); i++) {
        int totalPokes = Global.TOTAL_POKES;
        for (int i = 0; i < totalPokes; i++) {

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
        Log.i(TAG,"Stage 2 Complete: Exiting main loop: Beginning Stage 3: Setting Toggles");
        cursorPokemon.close();
        cursorStats.close();

        MainActivity.caughtDex = 0;
        MainActivity.livingDex = 0;
        for (Pokemon poke : pokemonArrayList) {
            //Set toggle states here
            String pokeNum = poke.getStringNumber();
            if (pokeball1 != null) {
                if (pokeball1.getInt(pokeNum) == 1) {
                    poke.setPokeballToggle1(true);
                    MainActivity.caughtDex++;
                }
            }
            if (pokeball2 != null) {
                if (pokeball2.getInt(pokeNum) == 1) {
                    poke.setPokeballToggle2(true);
                    MainActivity.livingDex++;
                }
            }
            if (pokeball3 != null) {
                if (pokeball3.getInt(pokeNum) == 1) {
                    poke.setPokeballToggle3(true);
                }
            }
            Global.curPokeList.add(poke.getNumber() - 1);
        }
        Log.i(TAG, "Stage 3 Complete: Load Completed: Returning Array");

        return pokemonArrayList;
    }
}
