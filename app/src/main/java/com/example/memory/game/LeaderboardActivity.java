package com.example.memory.game;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.R;
import com.example.memory.databinding.ActivityLeaderboardBinding;
import com.example.memory.navigation.BottomNavFragment;
import com.example.memory.navigation.HeaderFragment;
import com.example.memory.utilities.ReadWriteJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity implements BottomNavFragment.OnFragmentInteractionListener {

    private ActivityLeaderboardBinding binding;
    private ReadWriteJSON readWriteJSON;
    private Object[] mode;
    private String[] seekBarDifficulty;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seekBarDifficulty = new String[1];
        mode = new String[1];
        readWriteJSON = new ReadWriteJSON(getApplicationContext(), "leaderboard.json");

        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().add(R.id.buttonsLeader, BottomNavFragment.newInstance(getString(R.string.returnString))).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.headerLeader, HeaderFragment.newInstance(R.drawable.logo_leaderboard_drawable, getString(R.string.leaderboard), getString(R.string.Welcome_Message))).commit();
        spinner = findViewById(R.id.spinnerLeader);
        List<String> choice = new ArrayList<>();
        choice.add(getString(R.string.normal));
        choice.add(getString(R.string.contre_la_montre));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLeader.setAdapter(adapter);
        mode[0] = binding.spinnerLeader.getSelectedItem().toString();
        binding.spinnerLeader.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode[0] = binding.spinnerLeader.getSelectedItem().toString();
                updateLeaderboard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //rien
            }
        });

        //Récupération de la difficulté choisie dans la seek bar
        seekBarDifficulty[0] = String.valueOf(binding.seekBarLeader.getProgress());
        binding.seekBarLeader.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarDifficulty[0] = String.valueOf(seekBar.getProgress());
                updateLeaderboard();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //rien
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //rien
            }
        });
    }

    private void updateLeaderboard() {
        try {
            JSONObject leaderboard = new JSONObject(readWriteJSON.readJSON("leaderboard.json"));
            if (mode[0].equals(getString(R.string.normal))) {
                JSONObject jsonArrayNormal = leaderboard.getJSONObject("Normal");
                if (seekBarDifficulty[0].equals("0")) {
                    JSONObject jsonArrayNormal1 = jsonArrayNormal.getJSONObject("1");
                    JSONArray jsonArray = jsonArrayNormal1.getJSONArray("leaderboardFacile");
                    displayLeaderboard(jsonArray);
                } else if (seekBarDifficulty[0].equals("1")) {
                    JSONObject jsonArrayNormal2 = jsonArrayNormal.getJSONObject("2");
                    JSONArray jsonArray = jsonArrayNormal2.getJSONArray("leaderboardMoyen");
                    displayLeaderboard(jsonArray);
                } else if (seekBarDifficulty[0].equals("2")) {
                    JSONObject jsonArrayNormal3 = jsonArrayNormal.getJSONObject("3");
                    JSONArray jsonArray = jsonArrayNormal3.getJSONArray("leaderboardDifficile");
                    displayLeaderboard(jsonArray);
                }
            } else if (mode[0].equals(getString(R.string.contre_la_montre))) {
                JSONObject jsonArrayChrono = leaderboard.getJSONObject("Contre la Montre");
                if (seekBarDifficulty[0].equals("0")) {
                    JSONObject jsonArrayNormal1 = jsonArrayChrono.getJSONObject("1");
                    JSONArray jsonArray = jsonArrayNormal1.getJSONArray("leaderboardFacile");
                    displayLeaderboard(jsonArray);
                } else if (seekBarDifficulty[0].equals("1")) {
                    JSONObject jsonArrayNormal2 = jsonArrayChrono.getJSONObject("2");
                    JSONArray jsonArray = jsonArrayNormal2.getJSONArray("leaderboardMoyen");
                    displayLeaderboard(jsonArray);
                } else if (seekBarDifficulty[0].equals("2")) {
                    JSONObject jsonArrayNormal3 = jsonArrayChrono.getJSONObject("3");
                    JSONArray jsonArray = jsonArrayNormal3.getJSONArray("leaderboardDifficile");
                    displayLeaderboard(jsonArray);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayLeaderboard(JSONArray jsonArray) throws JSONException {
        //récupérer les 3 plus grands scores, si le score est égal, on prend celui qui a la valeur attempts la plus basse
        // Convert JSONArray to List<JSONObject>
        List<JSONObject> jsonList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonList.add(jsonArray.getJSONObject(Integer.parseInt(String.valueOf(i))));
        }

        // Sort the list
        Collections.sort(jsonList, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                try {
                    int score1 = o1.getInt("score");
                    int score2 = o2.getInt("score");
                    if (score1 == score2) {
                        int attempts1 = o1.getInt("attempts");
                        int attempts2 = o2.getInt("attempts");
                        return Integer.compare(attempts2, attempts1);
                    } else {
                        return Integer.compare(score2, score1); // Note the order for descending sort
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Extract top 3 scores
        List<JSONObject> topScores = jsonList.subList(0, Math.min(3, jsonList.size()));

        // Stocker les 3 meilleurs scores
        JSONObject result1 = topScores.get(0) != null ? topScores.get(0) : null;
        JSONObject result2 = topScores.size() > 1 ? topScores.get(1) : null;
        JSONObject result3 = topScores.size() > 2 ? topScores.get(2) : null;

        if (result1 == null) {
            findViewById(R.id.date1).setVisibility(View.GONE);
            findViewById(R.id.score1).setVisibility(View.GONE);
            findViewById(R.id.attempts1).setVisibility(View.GONE);
        } else {
            findViewById(R.id.date1).setVisibility(View.VISIBLE);
            findViewById(R.id.score1).setVisibility(View.VISIBLE);
            findViewById(R.id.attempts1).setVisibility(View.VISIBLE);

            TextView date1 = findViewById(R.id.date1);
            TextView score1 = findViewById(R.id.score1);
            TextView attempts1 = findViewById(R.id.attempts1);

            date1.setText(result1.getString("date"));
            score1.setText(String.valueOf(result1.getInt("score")));
            attempts1.setText(String.valueOf(result1.getInt("attempts")));
        }

        if (result2 == null) {
            findViewById(R.id.date2).setVisibility(View.GONE);
            findViewById(R.id.score2).setVisibility(View.GONE);
            findViewById(R.id.attempts2).setVisibility(View.GONE);
        } else {
            findViewById(R.id.date2).setVisibility(View.VISIBLE);
            findViewById(R.id.score2).setVisibility(View.VISIBLE);
            findViewById(R.id.attempts2).setVisibility(View.VISIBLE);

            TextView date2 = findViewById(R.id.date2);
            TextView score2 = findViewById(R.id.score2);
            TextView attempts2 = findViewById(R.id.attempts2);

            date2.setText(result2.getString("date"));
            score2.setText(String.valueOf(result2.getInt("score")));
            attempts2.setText(String.valueOf(result2.getInt("attempts")));
        }

        if (result3 == null) {
            findViewById(R.id.date3).setVisibility(View.GONE);
            findViewById(R.id.score3).setVisibility(View.GONE);
            findViewById(R.id.attempts3).setVisibility(View.GONE);
        } else {
            findViewById(R.id.date3).setVisibility(View.VISIBLE);
            findViewById(R.id.score3).setVisibility(View.VISIBLE);
            findViewById(R.id.attempts3).setVisibility(View.VISIBLE);

            TextView date3 = findViewById(R.id.date3);
            TextView score3 = findViewById(R.id.score3);
            TextView attempts3 = findViewById(R.id.attempts3);

            date3.setText(result3.getString("date"));
            score3.setText(String.valueOf(result3.getInt("score")));
            attempts3.setText(String.valueOf(result3.getInt("attempts")));
        }
    }

    @Override
    public void onPauseGame() {
    }
}