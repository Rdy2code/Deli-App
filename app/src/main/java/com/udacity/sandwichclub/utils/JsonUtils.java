package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    //Constant for logging messages for debugging purposes
    private static final String LOG_TAG = JsonUtils.class.getName();

    //Good coding practice: Define constant strings as separate variables
    private static final String JSON_NAME = "name";
    private static final String JSON_MAIN_NAME = "mainName";
    private static final String JSON_AKA = "alsoKnownAs";
    private static final String JSON_ORIGIN = "placeOfOrigin";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_INGREDIENTS = "ingredients";
    private static final String JSON_IMAGE = "image";
    private static final String JSON_FALLBACK = "fall_back_string";

    public static Sandwich parseSandwichJson(String json) {

        ArrayList<String> alsoKnownAs = new ArrayList<String>();
        ArrayList<String> ingredients = new ArrayList<String>();
        Sandwich sandwich = new Sandwich();

        // If the json string is empty, return early.
        if (json.isEmpty()) {
            return null;
        }

        // Use a try-catch block to catch the exceptions that can occur with JSON getter methods
        // when there is no value for the requested key
        try {

            /**Parse the Json object to extract the text values to be displayed in our UI.*/

            //Construct a JSONObject instance that contains the given json String
            JSONObject rootJsonObject = new JSONObject(json);

            //Get the JSON object for the "name" attribute of the sandwich
            JSONObject nameObject = rootJsonObject.optJSONObject(JSON_NAME);

            //Get the main name for the sandwich
            String mainName = nameObject.optString(JSON_MAIN_NAME, JSON_FALLBACK);
            sandwich.setMainName(mainName);

            //Construct JSONArray instance containing "also known as" names if they exist
            //Iterate the array to compile a list of strings for each aka
            JSONArray jsonArray_Aka = nameObject.optJSONArray(JSON_AKA);
            for (int i = 0; i < jsonArray_Aka.length(); i++) {
                alsoKnownAs.add(jsonArray_Aka.optString(i, JSON_FALLBACK));
            }
            sandwich.setAlsoKnownAs(alsoKnownAs);

            //Extract the text for the place where the sandwich originated
            String origin = rootJsonObject.optString(JSON_ORIGIN, JSON_FALLBACK);
            sandwich.setPlaceOfOrigin(origin);

            //Extract the description of the sandwich
            String description = rootJsonObject.optString(JSON_DESCRIPTION, JSON_FALLBACK);
            sandwich.setDescription(description);

            //Extract the ingredients for the sandwich and store in an ArrayList
            JSONArray jsonArray_Ingredients = rootJsonObject.optJSONArray(JSON_INGREDIENTS);
            for (int i = 0; i < jsonArray_Ingredients.length(); i++) {
                ingredients.add(jsonArray_Ingredients.getString(i));
            }
            sandwich.setIngredients(ingredients);

            //Extract as a String the Url reference to the image of the sandwich
            String image = rootJsonObject.optString(JSON_IMAGE);
            sandwich.setImage(image);

            //Instantiate a new Sandwich object instance with fields that correspond
            //to the text parsed from the JSON object
            sandwich = new Sandwich(mainName, alsoKnownAs, origin, description, image, ingredients);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Error", e);
        }

        return sandwich;
    }
}
