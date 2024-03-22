package com.example.memory;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ReadWriteJSON {

    public void copyJsonFileToInternalStorage(Context context) {
        try {
            // Open the file from the res/raw directory
            InputStream is = context.getResources().openRawResource(R.raw.cards);

            // Create an OutputStream to the internal files directory
            OutputStream os = context.openFileOutput("cards.json", MODE_PRIVATE);

            // Create a buffer to hold the data
            byte[] buffer = new byte[1024];
            int length;

            // Read from the InputStream and write to the OutputStream
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            // Close the streams
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readJSON(Context context) {
        String json = null;
        try {
            InputStream is = context.openFileInput("cards.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void editJSON(Context context, String cardName, String newImage, String newPrice, String newDescription, boolean newIsBought, String newRarity) {
        String jsonString = readJSON(context);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("cards");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cardObject = jsonArray.getJSONObject(i);

                if (cardObject.getString("name").equals(cardName)) {
                    // Modifier les propriétés de la carte
                    cardObject.put("image", newImage);
                    cardObject.put("prix", newPrice);
                    cardObject.put("description", newDescription);
                    cardObject.put("estAchetee", newIsBought);
                    cardObject.put("rarete", newRarity);
                    break;
                }
            }

            // Convertir le JSONObject en chaîne
            String modifiedJsonString = jsonObject.toString();

            // Écrire la chaîne dans le fichier JSON
            OutputStream os = context.openFileOutput("cards.json", MODE_PRIVATE);
            os.write(modifiedJsonString.getBytes());
            os.close();

            Log.d("Shop", String.valueOf(readJSON(context)));

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
