package com.group.tks_store.common.util;

import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.Iterator;

@NoArgsConstructor
public class Transform {

    public static JSONObject convertKeysToSnakeCase(JSONObject jsonObject) {
        JSONObject newJsonObject = new JSONObject();

        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                newJsonObject.put(convertToSnakeCase(key), convertKeysToSnakeCase((JSONObject) value));
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                JSONArray newJsonArray = new JSONArray();

                for (int i = 0; i < jsonArray.length(); i++) {
                    Object arrayElement = jsonArray.get(i);
                    if (arrayElement instanceof JSONObject) {
                        newJsonArray.put(convertKeysToSnakeCase((JSONObject) arrayElement));
                    } else {
                        newJsonArray.put(arrayElement);
                    }
                }

                newJsonObject.put(convertToSnakeCase(key), newJsonArray);
            } else {
                newJsonObject.put(convertToSnakeCase(key), value);
            }
        }

        return newJsonObject;
    }

    private static String convertToSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
