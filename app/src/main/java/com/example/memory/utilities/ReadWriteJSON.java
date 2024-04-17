package com.example.memory.utilities;

import android.content.Context;

import com.example.memory.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReadWriteJSON {
    private Context context;

    public ReadWriteJSON(Context context, String fileName) {
        this.context = context;
        //test si fichier existe sinon le créer et le remplir avec le fichier json de assets
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setJSON(fileName);
        }
    }

    public String readJSON(String fileName) {
        String json = null;
        InputStream jsonAssetsFile;
        InputStream jsonFile;
        try {
            jsonFile = context.openFileInput(fileName);
            if (jsonFile == null) {
                File file = new File(context.getFilesDir(), fileName);
                file.createNewFile();
                setJSON(fileName);
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

    public void editJSONCard(String cardName, boolean newIsBought) {
        FileWriter fileWriter = null;
        BufferedWriter writer = null;

        File file = new File(context.getFilesDir(), "cards.json");
        try {
            JSONObject jsonObject = new JSONObject(readJSON("cards.json"));
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

    public void editJSONCard(String cardName, boolean newIsBought, boolean selected) {
        FileWriter fileWriter = null;
        BufferedWriter writer = null;

        File file = new File(context.getFilesDir(), "cards.json");
        try {
            JSONObject jsonObject = new JSONObject(readJSON("cards.json"));
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

    public void editJSONLeaderboard(String mode, String difficulty, int gameScore, int time) throws ParseException {
        FileWriter fileWriter = null;
        BufferedWriter writer = null;

        // Créer un objet SimpleDateFormat avec le format actuel de la date
        SimpleDateFormat currentFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);

        Date date = new Date();

        // Analyser la chaîne de date en un objet Date
        Date dateFormated = currentFormat.parse(currentFormat.format(date));

        // Créer un objet SimpleDateFormat avec le format souhaité
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Utiliser la méthode format pour formater l'objet Date au format souhaité
        String formattedDate = desiredFormat.format(dateFormated);
        File file = new File(context.getFilesDir(), "leaderboard.json");
        try {
            JSONObject jsonObject = new JSONObject(readJSON("leaderboard.json"));

            if(mode.equals(context.getString(R.string.normal))) {
                JSONObject jsonArrayNormal = jsonObject.getJSONObject("Normal");
                if(difficulty.equals("1")) {
                    JSONObject jsonArrayDifficulty = jsonArrayNormal.getJSONObject("1");
                    JSONArray jsonArray = jsonArrayDifficulty.getJSONArray("leaderboardFacile");
                    JSONObject newScore = new JSONObject();
                    newScore.put("date", formattedDate.toString());
                    newScore.put("score", gameScore);
                    newScore.put("attempts", time);
                    jsonArray.put(newScore);
                } else if(difficulty.equals("2")) {
                    JSONObject jsonArrayDifficulty = jsonArrayNormal.getJSONObject("2");
                    JSONArray jsonArray = jsonArrayDifficulty.getJSONArray("leaderboardMoyen");
                    JSONObject newScore = new JSONObject();
                    newScore.put("date", formattedDate.toString());
                    newScore.put("score", gameScore);
                    newScore.put("attempts", time);
                    jsonArray.put(newScore);
                } else if(difficulty.equals("3")) {
                    JSONObject jsonArrayDifficulty = jsonArrayNormal.getJSONObject("3");
                    JSONArray jsonArray = jsonArrayDifficulty.getJSONArray("leaderboardDifficile");
                    JSONObject newScore = new JSONObject();
                    newScore.put("date", formattedDate.toString());
                    newScore.put("score", gameScore);
                    newScore.put("attempts", time);
                    jsonArray.put(newScore);
                }
            } else if (mode.equals(context.getString(R.string.contre_la_montre))) {
                JSONObject jsonArrayChrono = jsonObject.getJSONObject("Contre la Montre");
                if(difficulty.equals("1")) {
                    JSONObject jsonArrayDifficulty = jsonArrayChrono.getJSONObject("1");
                    JSONArray jsonArray = jsonArrayDifficulty.getJSONArray("leaderboardFacile");
                    JSONObject newScore = new JSONObject();
                    newScore.put("date", formattedDate.toString());
                    newScore.put("score", gameScore);
                    newScore.put("attempts", time);
                    jsonArray.put(newScore);
                } else if(difficulty.equals("2")) {
                    JSONObject jsonArrayDifficulty = jsonArrayChrono.getJSONObject("2");
                    JSONArray jsonArray = jsonArrayDifficulty.getJSONArray("leaderboardMoyen");
                    JSONObject newScore = new JSONObject();
                    newScore.put("date", formattedDate.toString());
                    newScore.put("score", gameScore);
                    newScore.put("attempts", time);
                    jsonArray.put(newScore);
                } else if(difficulty.equals("3")) {
                    JSONObject jsonArrayDifficulty = jsonArrayChrono.getJSONObject("3");
                    JSONArray jsonArray = jsonArrayDifficulty.getJSONArray("leaderboardDifficile");
                    JSONObject newScore = new JSONObject();
                    newScore.put("date", formattedDate.toString());
                    newScore.put("score", gameScore);
                    newScore.put("attempts", time);
                    jsonArray.put(newScore);
                }
            }

            //Remplacer le tableau de scores
            fileWriter = new FileWriter(file.getAbsoluteFile());
            writer = new BufferedWriter(fileWriter);
            writer.write(jsonObject.toString());
            writer.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setJSON(String fileName) {
        InputStream jsonAssetsFile;
        FileWriter fileWriter = null;
        BufferedWriter writer = null;

        File file = new File(context.getFilesDir(), fileName);
        try {
            jsonAssetsFile = context.getAssets().open(fileName);
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
