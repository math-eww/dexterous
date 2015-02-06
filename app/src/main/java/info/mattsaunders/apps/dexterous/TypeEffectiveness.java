package info.mattsaunders.apps.dexterous;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Functions to calculate type effectiveness
 */
public class TypeEffectiveness {

     /*
     Matrix of the different types and their effectiveness to each other.
     0 represents "Not very effective",
     1 is "Neutral",
     2 is "Super effective",
     3 is "No Damage"
    */

    private static int[][] typeArray = {
            {1, 1, 1, 1, 1, 0, 1, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {2, 1, 0, 0, 1, 2, 0, 3, 2, 1, 1, 1, 1, 0, 2, 1, 2, 0},
            {1, 2, 1, 1, 1, 0, 2, 1, 0, 1, 1, 2, 0, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 0, 0, 1, 0, 3, 1, 1, 2, 1, 1, 1, 1, 1, 2},
            {1, 1, 3, 2, 1, 2, 0, 1, 2, 2, 1, 0, 2, 1, 1, 1, 1, 1},
            {1, 0, 2, 1, 0, 1, 2, 1, 0, 2, 1, 1, 1, 1, 2, 1, 1, 1},
            {1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 2, 1, 2, 1, 1, 2, 0},
            {3, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1},
            {1, 1, 1, 1, 1, 2, 1, 1, 0, 0, 0, 1, 0, 1, 2, 1, 1, 2},
            {1, 1, 1, 1, 1, 0, 2, 1, 2, 0, 0, 2, 1, 1, 2, 0, 1, 1},
            {1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 0, 0, 1, 1 ,1, 0, 1, 1},
            {1, 1, 0, 0, 2, 2, 0, 1, 0, 0, 2, 0, 1, 1, 1, 0, 1, 1},
            {1, 1, 2, 1, 3, 1, 1, 1, 1, 1, 2, 0, 0, 1, 1, 0, 1, 1},
            {1, 2, 1, 2, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 3, 1},
            {1, 1, 2, 1, 2, 1, 1, 1, 0, 0, 0, 2, 1, 1, 0, 2, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 2, 1, 3},
            {1, 0, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 0, 0},
            {1, 2, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 2, 2, 1}
    };

    public static ArrayList<String> typeNamesList = new ArrayList<>(Arrays.asList(
            "Normal", "Fighting", "Flying", "Poison", "Ground",
            "Rock", "Bug", "Ghost", "Steel", "Fire", "Water",
            "Grass", "Electric", "Psychic", "Ice", "Dragon",
            "Dark", "Fairy"
    ));

    public static Bundle getDefensiveTypeEffectiveness(String typeOne, String typeTwo) {
        Bundle bundle = new Bundle();

        int typeOneIndex = -1;
        int typeTwoIndex = -1;

        //Find position of types:
        typeOneIndex = typeNamesList.indexOf(typeOne);
        if (!typeTwo.equals("")) {
            typeTwoIndex = typeNamesList.indexOf(typeTwo);
        }

        int[] typeOneEffectiveness = new int[typeNamesList.size()];
        //Iterate through all the types and get effectiveness against typeOne
        for (int i = 0; i < typeNamesList.size(); i++) {
            typeOneEffectiveness[i] = typeArray[i][typeOneIndex];
        }
        if (typeTwoIndex != -1) {
            int[] typeTwoEffectiveness = new int[typeNamesList.size()];
            //Iterate through all the types and get effectiveness against typeTwo
            for (int i = 0; i < typeNamesList.size(); i++) {
                typeTwoEffectiveness[i] = typeArray[i][typeTwoIndex];
            }
            for (int i = 0; i < typeNamesList.size(); i++) {
                float multiplier = 1;
                float multiplierOne = 1;
                float multiplierTwo = 1;
                switch (typeOneEffectiveness[i]) {
                    case 0:
                        multiplierOne = 0.5f;
                        break;
                    case 2:
                        multiplierOne = 2;
                        break;
                    case 3:
                        multiplierOne = 0;
                        break;
                }
                switch (typeTwoEffectiveness[i]) {
                    case 0:
                        multiplierTwo = 0.5f;
                        break;
                    case 2:
                        multiplierTwo = 2;
                        break;
                    case 3:
                        multiplierTwo = 0;
                        break;
                }
                //Multiply weaknesses together to get actual type effectiveness and add to bundle
                multiplier = multiplierOne * multiplierTwo;
                bundle.putFloat(typeNamesList.get(i),multiplier);
            }
        } else {
            for (int i = 0; i < typeNamesList.size(); i++) {
                float multiplier = 1;
                switch (typeOneEffectiveness[i]) {
                    case 0:
                        multiplier = 0.5f;
                        break;
                    case 2:
                        multiplier = 2;
                        break;
                    case 3:
                        multiplier = 0;
                        break;
                }
                bundle.putFloat(typeNamesList.get(i), multiplier);
            }
        }


        return bundle;
    }

    public static Bundle getTypeEffectiveness(String typeOne, String typeTwo) {
        Bundle bundle = new Bundle();
        int typeOneIndex = -1;
        int typeTwoIndex = -1;

        //Find position of types:
        typeOneIndex = typeNamesList.indexOf(typeOne);
        if (!typeTwo.equals("")) {
            typeTwoIndex = typeNamesList.indexOf(typeTwo);
        }

        //Calculate effectiveness:
        if (typeTwoIndex != -1) {
            int[] typeOneEffectiveness = typeArray[typeOneIndex];
            int[] typeTwoEffectiveness = typeArray[typeTwoIndex];
            for (int i = 0; i < typeNamesList.size(); i++) {
                float multiplier = 1;
                float multiplierOne = 1;
                float multiplierTwo = 1;
                switch (typeOneEffectiveness[i]) {
                    case 0:
                        multiplierOne = 0.5f;
                        break;
                    case 2:
                        multiplierOne = 2;
                        break;
                    case 3:
                        multiplierOne = 0;
                        break;
                }
                switch (typeTwoEffectiveness[i]) {
                    case 0:
                        multiplierTwo = 0.5f;
                        break;
                    case 2:
                        multiplierTwo = 2;
                        break;
                    case 3:
                        multiplierTwo = 0;
                        break;
                }
                multiplier = multiplierOne * multiplierTwo;
                bundle.putFloat(typeNamesList.get(i),multiplier);
            }
        } else {
            int[] typeOneEffectiveness = typeArray[typeOneIndex];
            for (int i = 0; i < typeNamesList.size(); i++) {
                float multiplier = 1;
                switch (typeOneEffectiveness[i]) {
                    case 0:
                        multiplier = 0.5f;
                        break;
                    case 2:
                        multiplier = 2;
                        break;
                    case 3:
                        multiplier = 0;
                        break;
                }
                bundle.putFloat(typeNamesList.get(i),multiplier);
            }
        }
        return bundle;
    }
}
