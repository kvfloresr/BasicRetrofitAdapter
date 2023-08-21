package practica.univalle.basicretrofitadapter.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import practica.univalle.basicretrofitadapter.HorseRaceActivity;
import practica.univalle.basicretrofitadapter.R;

public class Horse implements Runnable {
    private final ImageView horseImage;
    private LinearLayout horsesLayout;
    private static final int MAX_DISTANCE = 1000;
    private volatile boolean shouldStop = false;
    private String horseId;
    private int horseNumber;
    private String horseName;
    private int distance = 0;



    public Horse(View view, int id) {
        horseImage = view.findViewById(R.id.horseImage);
        horsesLayout = view.findViewById(R.id.racetrackLayout);
        horseId = "Horse" + id;
        horseNumber = id + 1;
        horseName = generateHorseName();
        loadHorseImage();
    }

    private void loadHorseImage() {
        String imageName = "horse" + horseNumber;
        int resourceId = horsesLayout.getResources().getIdentifier(imageName, "drawable", horsesLayout.getContext().getPackageName());
        if (resourceId != 0) {
            horseImage.setImageResource(resourceId);
        }

        TextView horseNameTextView = horsesLayout.findViewById(R.id.horseNameTextView);
        horseNameTextView.setText(horseName);
    }

    private String generateHorseName() {
        String[] horseNames = {
                "Relampago",
                "Tormenta",
                "Spirit",
                "Rayo",
                "Niebla",
                "Veloz",
                "Rambo",
                "Terminator",
                "Speed",
                "Max"
        };

        if (horseNumber <= horseNames.length) {
            return horseNames[horseNumber - 1];
        } else {
            return "Horse " + horseNumber;
        }
    }


    @Override
    public void run() {

        int finalDistance = 0;
        while (distance < MAX_DISTANCE && !shouldStop) {
            try {
                Thread.sleep((long) (Math.random() * 200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            distance += (int) (Math.random() * 10);
            finalDistance = Math.min(distance, MAX_DISTANCE);
            float progress = (float) finalDistance / MAX_DISTANCE;
            horseImage.post(() -> {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) horseImage.getLayoutParams();
                params.leftMargin = (int) (progress * (horsesLayout.getWidth() - horseImage.getWidth()));
                horseImage.setLayoutParams(params);

                TextView progressTextView = horsesLayout.findViewById(R.id.progressTextView);
                int percentage = (int) (progress * 100);
                progressTextView.setText(percentage + "%");

            });
        }
        if (finalDistance == MAX_DISTANCE) {
            notifyRaceFinished();
        }
    }


    private void notifyRaceFinished() {
        ((HorseRaceActivity) horsesLayout.getContext()).notifyHorseFinished(this);
    }
    public void resetDistance() {
        distance = 0;
    }
    public void stop() {
        shouldStop = true;
    }
    public String getHorseId() {
        return horseId;
    }
    public int getDistance() {
        return distance;
    }
    public boolean isStopped() {
        return shouldStop;
    }
    public String getHorseName() {
        return horseName;
    }

}
