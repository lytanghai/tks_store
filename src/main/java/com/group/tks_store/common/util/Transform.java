package com.group.tks_store.common.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class Transform {

    // Utility method to convert camelCase to snake_case
    private static String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    // Method to recursively convert JSONObject keys to snake_case
    public static JSONObject convertKeysToSnakeCase(JSONObject jsonObject) {
        JSONObject newJsonObject = new JSONObject();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            // Convert key to snake_case
            String snakeKey = camelToSnake(key);

            // If value is a JSONObject, recursively convert it
            if (value instanceof JSONObject) {
                value = convertKeysToSnakeCase((JSONObject) value);
            }
            // If value is a JSONArray, iterate over and convert each element
            else if (value instanceof JSONArray) {
                value = convertArrayKeysToSnakeCase((JSONArray) value);
            }

            // Put the transformed key and value in the new JSONObject
            newJsonObject.put(snakeKey, value);
        }

        return newJsonObject;
    }

    // Method to recursively convert JSONArray elements' keys to snake_case
    private static JSONArray convertArrayKeysToSnakeCase(JSONArray jsonArray) {
        JSONArray newArray = new JSONArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object element = jsonArray.get(i);

            if (element instanceof JSONObject) {
                element = convertKeysToSnakeCase((JSONObject) element);
            } else if (element instanceof JSONArray) {
                element = convertArrayKeysToSnakeCase((JSONArray) element);
            }

            newArray.put(element);
        }

        return newArray;
    }


}
