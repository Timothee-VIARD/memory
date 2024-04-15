package com.example.memory.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ReadWriteJSON {
    private Context context;

    public ReadWriteJSON(Context context) {
        this.context = context;
        //test si fichier existe sinon le créer et le remplir avec le fichier json de assets
        File file = new File(context.getFilesDir(), "cards.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setJSON();
        }
    }

    public String readJSON() {
        String json = null;
        InputStream jsonAssetsFile;
        InputStream jsonFile;
        try {
            jsonFile = context.openFileInput("cards.json");
            if (jsonFile == null) {
                File file = new File(context.getFilesDir(), "cards.json");
                file.createNewFile();
                setJSON();
            }
            int size = jsonFile.available();
            byte[] buffer = new byte[size];
            jsonFile.read(buffer);
            jsonFile.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void editJSON(String cardName, boolean newIsBought) {
        String jsonString = readJSON();
        FileWriter fileWriter = null;
        BufferedWriter writer = null;

        File file = new File(context.getFilesDir(), "cards.json");
        try {
            JSONObject jsonObject = new JSONObject(readJSON());
            JSONArray jsonArray = jsonObject.getJSONArray("cards");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cardObject = jsonArray.getJSONObject(i);

                if (cardObject.getString("name").equals(cardName)) {
                    // Modifier les propriétés de la carte
                    cardObject.put("estAchetee", newIsBought);
                    jsonArray.put(i, cardObject);
                    break;
                }
            }

            // Convertir le jsonArray en jsonobject
            jsonObject.put("cards", jsonArray);

            //Editer la carte qui correspond au cardName
            fileWriter = new FileWriter(file.getAbsoluteFile());
            writer = new BufferedWriter(fileWriter);
            writer.write(jsonObject.toString());
            writer.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void editJSON(String cardName, boolean newIsBought, boolean selected) {
        String jsonString = readJSON();
        FileWriter fileWriter = null;
        BufferedWriter writer = null;

        File file = new File(context.getFilesDir(), "cards.json");
        try {
            JSONObject jsonObject = new JSONObject(readJSON());
            JSONArray jsonArray = jsonObject.getJSONArray("cards");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cardObject = jsonArray.getJSONObject(i);

                if (cardObject.getString("name").equals(cardName)) {
                    // Modifier les propriétés de la carte
                    cardObject.put("estAchetee", newIsBought);
                    cardObject.put("selected", selected);
                    jsonArray.put(i, cardObject);
                    break;
                }
            }

            // Convertir le jsonArray en jsonobject
            jsonObject.put("cards", jsonArray);

            //Editer la carte qui correspond au cardName
            fileWriter = new FileWriter(file.getAbsoluteFile());
            writer = new BufferedWriter(fileWriter);
            writer.write(jsonObject.toString());
            writer.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setJSON() {
        InputStream jsonAssetsFile;
        FileWriter fileWriter = null;
        BufferedWriter writer = null;

        File file = new File(context.getFilesDir(), "cards.json");
        try {
            jsonAssetsFile = context.getAssets().open("cards.json");
            int size = jsonAssetsFile.available();
            byte[] buffer = new byte[size];
            jsonAssetsFile.read(buffer);
            jsonAssetsFile.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            fileWriter = new FileWriter(file.getAbsoluteFile());
            writer = new BufferedWriter(fileWriter);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
