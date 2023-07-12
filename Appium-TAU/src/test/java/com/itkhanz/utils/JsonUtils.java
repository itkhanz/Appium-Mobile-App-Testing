package com.itkhanz.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class JsonUtils {
    public static Object[][] getJsonData(String filePath, String jsonData, int jsonAttributes) {
        try {
            JsonReader jsonReader = new JsonReader(new FileReader(filePath));
            JsonObject jsonObject = new Gson().fromJson(jsonReader, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray(jsonData);

            Object[][] arr = new String[jsonArray.size()][jsonAttributes];
            for (int i = 0; i < jsonArray.size(); i++) {
                arr[i][0] = String.valueOf(jsonArray.get(i).getAsJsonObject().get("TaskName"));
                arr[i][1] = String.valueOf(jsonArray.get(i).getAsJsonObject().get("TaskDesc"));
            }
            //System.out.println(Arrays.deepToString(arr));
            return arr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read file at location: " + filePath);
        }
    }
}
