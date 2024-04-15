package com.example.memory.game;

import static java.lang.Math.round;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.HomeActivity;
import com.example.memory.R;
import com.example.memory.cards.GameCard;
import com.example.memory.databinding.ActivityGameBinding;
import com.example.memory.navigation.BottomNavFragment;
import com.example.memory.navigation.HeaderFragment;
import com.example.memory.utilities.ReadWriteJSON;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements BottomNavFragment.OnFragmentInteractionListener {
    private ActivityGameBinding binding;
    private Game game;
    private int difficulty;
    private String mode;
    private int widthCard = 383;
    private int heightCard = 536;
    private ReadWriteJSON readWriteJSON;
    private Timer timer;
    private int seconds = 0;
    private int cardSet = R.drawable.basic_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startTimer(false);

        Intent intent = getIntent();
        this.difficulty = intent.getIntExtra("difficulty", 0);
        this.mode = intent.getStringExtra("mode");

        readWriteJSON = new ReadWriteJSON(getApplicationContext(), "leaderboard.json");
        TextView modeView = findViewById(R.id.mode);
        modeView.setText(mode);
        TextView difficultyView = findViewById(R.id.difficulty);
        setDifficultyText(difficultyView);

        getSupportFragmentManager().beginTransaction().replace(R.id.buttonsMenuGame, BottomNavFragment.newInstance(getString(R.string.pause), getString(R.string.stop))).commit();

        FlexboxLayout flexboxLayout = findViewById(R.id.container);
        flexboxLayout.setFlexDirection(FlexDirection.ROW);
        flexboxLayout.setFlexWrap(FlexWrap.WRAP);
        flexboxLayout.setAlignContent(AlignContent.FLEX_START);
        flexboxLayout.setAlignItems(AlignItems.FLEX_START);
        flexboxLayout.setJustifyContent(JustifyContent.CENTER);

        Game game = new Game(this, cardSet, difficulty, this);
        this.game = game;
        generateDimensions();
        setUI(flexboxLayout);
    }

    public void updateUI() {
        FlexboxLayout flexboxLayout = findViewById(R.id.container);
        flexboxLayout.removeAllViews();
        setUI(flexboxLayout);
    }

    private void setUI(FlexboxLayout flexboxLayout) {
        for (GameCard gameCard : game.getGameCards()) {
            ImageView imageView = gameCard.getImage();
            if (!gameCard.isFlipped) {
                imageView.setOnClickListener(v -> {
                    game.flipCard(gameCard);
                    updateUI();
                });
            }
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(widthCard, heightCard);
            layoutParams.setMargins(10, 10, 10, 10);
            imageView.setLayoutParams(layoutParams);
            flexboxLayout.addView(imageView);
        }
    }

    private void generateDimensions() {
        switch (difficulty) {
            case 1:
                this.widthCard = (int) round(this.widthCard / 1.75);
                this.heightCard = (int) round(this.heightCard / 1.75);
                break;
            case 2:
                this.widthCard = (int) round(this.widthCard / 2.5);
                this.heightCard = (int) round(this.heightCard / 2.5);
                break;
            case 3:
                this.widthCard = (int) round(this.widthCard / 3.25);
                this.heightCard = (int) round(this.heightCard / 3.25);
                break;
        }
    }

    public void updateScore() {
        TextView score = findViewById(R.id.score);
        score.setText("Score: " + game.getScore());
    }

    public void endGame() {
        stopTimer();
        Dialog dialog = new Dialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                readWriteJSON.editJSONLeaderboard(mode, String.valueOf(difficulty), game.getScore(), game.getAttempts());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        dialog.setContentView(R.layout.menu_dialog);
        TextView title = dialog.findViewById(R.id.title);
        TextView score = dialog.findViewById(R.id.score);
        TextView time = dialog.findViewById(R.id.time);
        Button buttonLabel1 = dialog.findViewById(R.id.button1);
        Button buttonLabel2 = dialog.findViewById(R.id.button2);
        title.setText("Fin de la partie");
        score.setText("Score: " + game.getScore());
        time.setText("Temps: " + (seconds - 1) + "s");
        buttonLabel1.setText("Menu du jeu");
        buttonLabel2.setText("Rejouer");
        buttonLabel1.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(GameActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        buttonLabel2.setOnClickListener(v -> {
            dialog.dismiss();
            game = new Game(this, cardSet, difficulty, this);
            updateUI();
            startTimer(false);
        });
        dialog.show();
    }

    @Override
    public void onPauseGame() {
        stopTimer();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.menu_dialog);
        TextView title = dialog.findViewById(R.id.title);
        TextView score = dialog.findViewById(R.id.score);
        TextView time = dialog.findViewById(R.id.time);
        Button buttonLabel1 = dialog.findViewById(R.id.button1);
        Button buttonLabel2 = dialog.findViewById(R.id.button2);
        title.setText("Pause");
        score.setText("Score: " + game.getScore());
        time.setText("Temps: " + (seconds - 1) + "s");
        buttonLabel1.setText("Restart");
        buttonLabel2.setText("Reprendre");
        buttonLabel1.setOnClickListener(v -> {
            dialog.dismiss();
            game = new Game(this, cardSet, difficulty, this);
            updateUI();
            startTimer(false);
        });
        buttonLabel2.setOnClickListener(v -> {
            dialog.dismiss();
            startTimer(true);
        });
        dialog.show();
    }

    private void startTimer(Boolean isContinued) {
        this.timer = new Timer();

        if (!isContinued) {
            this.seconds = 0;
        }
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView chrono = findViewById(R.id.chrono);
                        chrono.setText("Chrono : " + seconds + "s");
                        seconds++;
                    }
                });
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        this.timer.cancel();
    }

    private void setDifficultyText(TextView difficultyView) {
        switch (difficulty) {
            case 1:
                difficultyView.setText("Facile");
                break;
            case 2:
                difficultyView.setText("Moyen");
                break;
            case 3:
                difficultyView.setText("Difficile");
                break;
        }
    }
}