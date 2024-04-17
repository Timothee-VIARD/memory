package com.example.memory.game;

import static java.lang.Math.round;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.HomeActivity;
import com.example.memory.R;
import com.example.memory.game.cards.GameCard;
import com.example.memory.databinding.ActivityGameBinding;
import com.example.memory.game.service.ChronoService;
import com.example.memory.navigation.BottomNavFragment;
import com.example.memory.utilities.ReadWriteJSON;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.text.ParseException;

public class GameActivity extends AppCompatActivity implements BottomNavFragment.OnFragmentInteractionListener, ChronoService.OnSecondChangeListener, ChronoService.OnTimerFinishedListener {
    private ActivityGameBinding binding;
    private Game game;
    private ChronoService chronoService;
    private Intent intentService;
    private boolean isBound = false;
    private int difficulty;
    private String mode;
    private int widthCard = 383;
    private int heightCard = 536;
    private ReadWriteJSON readWriteJSON;
    private int seconds = 0;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChronoService.MyBinder myBinder = (ChronoService.MyBinder) service;
            chronoService = myBinder.getService();
            chronoService.setOnSecondChangeListener(GameActivity.this);
            chronoService.setOnTimerFinishedListener(GameActivity.this);
            isBound = true;

            onSecondChange(chronoService.getSeconds());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        this.difficulty = intent.getIntExtra("difficulty", 0);
        this.mode = intent.getStringExtra("mode");

        intentService = new Intent(this, ChronoService.class);
        intentService.putExtra("mode", mode);
        intentService.putExtra("difficulty", difficulty);
        startService(intentService);
        bindService(intentService, serviceConnection, BIND_AUTO_CREATE);

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

        Game game = new Game(this, difficulty, this);
        this.game = game;

        generateDimensions();
        setUI(flexboxLayout);
    }

    private void generateDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        double param1;
        double param2;
        double param3;

        if (height > 2000) {
            param1 = 1.75;
            param2 = 2.25;
            param3 = 2.75;
        } else {
            param1 = 2.0;
            param2 = 2.5;
            param3 = 3.25;
        }

        switch (difficulty) {
            case 1:
                this.widthCard = (int) round(this.widthCard / param1);
                this.heightCard = (int) round(this.heightCard / param1);
                break;
            case 2:
                this.widthCard = (int) round(this.widthCard / param2);
                this.heightCard = (int) round(this.heightCard / param2);
                break;
            case 3:
                this.widthCard = (int) round(this.widthCard / param3);
                this.heightCard = (int) round(this.heightCard / param3);
                break;
        }
    }

    public void updateUI() {
        FlexboxLayout flexboxLayout = findViewById(R.id.container);
        flexboxLayout.removeAllViews();
        setUI(flexboxLayout);
        updateScore();
        updateMultiplier();
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

    public void updateScore() {
        TextView score = findViewById(R.id.score);
        score.setText("Score : " + game.getScore());
    }

    public void updateMultiplier() {
        TextView multiplier = findViewById(R.id.multiplier);
        if (game.getScoreMultiplier() < 0) {
            multiplier.setText("x(" + Math.round(game.getScoreMultiplier() * 10) / 10.0 + ")");
        } else {
            multiplier.setText("x" + Math.round(game.getScoreMultiplier() * 10) / 10.0);
        }
    }

    public void endGame() {
        if (isBound) {
            chronoService.cancelTimer();
            unbindService(serviceConnection);
            isBound = false;
        }
        stopService(new Intent(this, ChronoService.class));

        if (mode.equals(getString(R.string.normal))) {
            game.updateScore(seconds);
        }
        updateScore();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                readWriteJSON.editJSONLeaderboard(mode, String.valueOf(difficulty), game.getScore(), chronoService.getSeconds());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialog = LayoutInflater.from(this).inflate(R.layout.pause_end_game_dialog, viewGroup, false);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();

        TextView title = dialog.findViewById(R.id.title);
        TextView score = dialog.findViewById(R.id.score);
        TextView time = dialog.findViewById(R.id.time);
        Button buttonLabel1 = dialog.findViewById(R.id.button1);
        Button buttonLabel2 = dialog.findViewById(R.id.button2);
        title.setText(R.string.fin_de_la_partie);
        score.setText("Score: " + game.getScore());
        time.setText(getString(R.string.temps) + chronoService.getSeconds() + "s");
        buttonLabel1.setText(R.string.menu_du_jeu);
        buttonLabel2.setText(R.string.rejouer);
        buttonLabel1.setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent intent = new Intent(GameActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        buttonLabel2.setOnClickListener(v -> {
            alertDialog.dismiss();
            game = new Game(this, difficulty, this);
            updateUI();
            if (isBound) {
                unbindService(serviceConnection);
                isBound = false;
            }
            intentService = new Intent(this, ChronoService.class);
            intentService.putExtra("mode", mode);
            intentService.putExtra("difficulty", difficulty);
            startService(intentService);
            bindService(intentService, serviceConnection, BIND_AUTO_CREATE);
        });
        alertDialog.show();
    }

    @Override
    public void onPauseGame() {
        if (isBound) {
            chronoService.pauseTimer();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialog = LayoutInflater.from(this).inflate(R.layout.pause_end_game_dialog, viewGroup, false);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();

        TextView title = dialog.findViewById(R.id.title);
        TextView score = dialog.findViewById(R.id.score);
        TextView time = dialog.findViewById(R.id.time);
        Button buttonLabel1 = dialog.findViewById(R.id.button1);
        Button buttonLabel2 = dialog.findViewById(R.id.button2);
        title.setText(getString(R.string.pause));
        score.setText("Score : " + game.getScore());
        time.setText(getString(R.string.temps) + chronoService.getSeconds() + "s");
        buttonLabel1.setText(getString(R.string.restart));
        buttonLabel2.setText(getString(R.string.continuer));
        buttonLabel1.setOnClickListener(v -> {
            alertDialog.dismiss();
            game = new Game(this, difficulty, this);
            updateUI();
            if (isBound) {
                chronoService.resetTimer();
                chronoService.resumeTimer();
                unbindService(serviceConnection);
                isBound = false;
            }

            intentService = new Intent(this, ChronoService.class);
            intentService.putExtra("mode", mode);
            intentService.putExtra("difficulty", difficulty);
            chronoService.cancelTimer();
            startService(intentService);
            bindService(intentService, serviceConnection, BIND_AUTO_CREATE);
        });
        buttonLabel2.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (isBound) {
                chronoService.resumeTimer();
            }
        });
        alertDialog.show();
    }

    private void setDifficultyText(TextView difficultyView) {
        switch (difficulty) {
            case 1:
                difficultyView.setText(R.string.facile);
                break;
            case 2:
                difficultyView.setText(R.string.moyen);
                break;
            case 3:
                difficultyView.setText(R.string.difficile);
                break;
        }
    }

    @Override
    public void onSecondChange(int seconds) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView time = findViewById(R.id.chrono);
                time.setText(getString(R.string.temps) + seconds + "s");
            }
        });
    }

    @Override
    public void onTimerFinished() {
        endGame();
    }

    public void onDestroy() {
        super.onDestroy();
        if (isBound) {
            chronoService.cancelTimer();
            unbindService(serviceConnection);
            isBound = false;
        }
        stopService(new Intent(this, ChronoService.class));
    }
}