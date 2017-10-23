package com.vasilievpavel.mobhub.commons;

import android.content.Context;

import com.vasilievpavel.mobhub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ColorManager {
    private JSONObject languages;

    public ColorManager(Context context) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.colors);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            languages = new JSONObject(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getColor(String language) {
        try {
            return languages.getJSONObject(language).getString("color");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
