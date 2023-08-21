package practica.univalle.basicretrofitadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import practica.univalle.basicretrofitadapter.models.Horse;

public class HorseRaceActivity extends AppCompatActivity {
    private Horse winnerHorse = null;
    private static final int NUM_CABALLOS = 10;
    private Horse[] horses = new Horse[NUM_CABALLOS];
    private volatile boolean raceFinished = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horse_race);

        LinearLayout layout = findViewById(R.id.horsesLayout);
        for (int i = 0; i < NUM_CABALLOS; i++) {
            View horseView = getLayoutInflater().inflate(R.layout.horse_layout, layout, false);
            horses[i] = new Horse(horseView, i);
            layout.addView(horseView);
        }

        Button startRaceButton = findViewById(R.id.startRaceButton);
        startRaceButton.setOnClickListener(v -> startRace());

        Button stopRaceButton = findViewById(R.id.stopRaceButton);
        stopRaceButton.setOnClickListener(v -> stopRace());

        Button stopOneHorseButton = findViewById(R.id.stoponehorse);
        stopOneHorseButton.setOnClickListener(v -> stopOneHorse());

    }

    private void startRace() {
        resetRace();
        for (Horse horse : horses) {
            new Thread(horse).start();
        }
    }


    private void stopRace() {
        for (Horse horse : horses) {
            horse.stop();
        }
    }

    private void stopOneHorse() {
        EditText inputHorseEditText = findViewById(R.id.inputhorse);
        String inputHorse = "Horse" + inputHorseEditText.getText().toString();

        for (Horse horse : horses) {
            if (horse.getHorseId().equals(inputHorse)) {
                horse.stop();
                break;
            }
        }


        boolean allHorsesStopped = true;
        for (Horse horse : horses) {
            if (!horse.isStopped()) {
                allHorsesStopped = false;
                break;
            }
        }

        if (allHorsesStopped) {
            determineWinner();
            updateWinnerTextView();
        }
    }

    private void determineWinner() {
        int maxDistance = -1;
        for (Horse horse : horses) {
            int distance = horse.getDistance();
            if (distance > maxDistance) {
                maxDistance = distance;
                winnerHorse = horse;
            }
        }
    }

    private void updateWinnerTextView() {
        TextView winnerTextView = findViewById(R.id.winnerTextView);
        if (winnerHorse != null) {
            winnerTextView.setText("Ganador: " + winnerHorse.getHorseName());
        } else {
            winnerTextView.setText("Sin ganador");
        }
    }

    public void notifyHorseFinished(Horse horse) {
        if (!raceFinished) {
            raceFinished = true;
            winnerHorse = horse;
            updateWinnerTextView();
        }
    }

    private void resetRace() {
        for (Horse horse : horses) {
            horse.resetDistance();
        }
        raceFinished = false;
        winnerHorse = null;
        updateWinnerTextView();
    }


}