package com.dng.minesweeper.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dng.minesweeper.model.Player;
import com.dng.minesweeper.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataService {

    private static final String TAG = "DataService";

    // [START instantiate Firestore references]
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference highscoresCollectionRef = db.collection(Constants.Highscore.HIGHSCORES);
    // [END instantiate Firestore references]

    // [START make DataService a singleton class]
    // static variable instance of type DataService
    public static DataService instance = DataService.getInstance();

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }

        return instance;
    }
    // [END make DataService a singleton class]

    // [START get highscores]
    public interface HighScoresInterface {
        void getHighScoresComplete(List<Player> playerList);
    }

    public void getHighscores(final HighScoresInterface complete) {
        final List<Player> playerList = new ArrayList<>();

        highscoresCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    String displayName = (String) snapshot.get(Constants.Highscore.NAME);
                    long score = (long) snapshot.get(Constants.Highscore.SCORE);
                    Player player = new Player(displayName, score);
                    playerList.add(player);
                }

                complete.getHighScoresComplete(playerList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
                complete.getHighScoresComplete(null);
            }
        });
    }
    // [END get highscores]

    // [START set highscore]
    public void setHighscore(String displayName, int score) {
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.Highscore.NAME, displayName);
        data.put(Constants.Highscore.SCORE, score);

        highscoresCollectionRef.document().set(data, SetOptions.merge());
    }
    // [END set highscore]

}
