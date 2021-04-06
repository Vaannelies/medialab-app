package nl.hr.annelies.medialab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FirstFragment extends Fragment {

    private Boolean kidLost;
    private Integer id;
    FirebaseFirestore db;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonLost = view.findViewById(R.id.button_first);
        Button buttonFound = view.findViewById(R.id.button_found);
        Button buttonMic = view.findViewById(R.id.button_mic);

        db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        kidLost = sharedPreferences.getBoolean("kidLost", false);
        id = sharedPreferences.getInt("id", 0);

        if(!kidLost) {
            buttonLost.setVisibility(VISIBLE);
            buttonFound.setVisibility(GONE);
        } else {
            buttonLost.setVisibility(GONE);
            buttonFound.setVisibility(VISIBLE);
        }


        // listen for realtime updates
        DocumentReference docRef = db.collection("kids").document(id.toString());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    Log.w("ERROR", "Listen failed.", error);
                    return;
                }
                if(value != null && value.exists()) {
                    Log.d("DATA", "Current data: " + value.getData());
                    if(value.getData().get("found").equals(true)) {
                        buttonMic.setVisibility(VISIBLE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("kidLost", false);
                        editor.apply();
                        Log.d("MIC", "on");
                    } else {
                        buttonMic.setVisibility(INVISIBLE);
                        Log.d("MIC", "off");
                    }
                } else {
                    Log.d("DATA", "Current data: null");
                }
            }
        });

        // ID textview
        TextView idTextView = view.findViewById(R.id.textview_first);
        idTextView.setText("#" + id.toString());

        // mic button
        buttonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MicActivity.class));
            }
        });

        // lost button
        buttonLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Weet u het zeker?");
                builder.setMessage("U staat op het punt om een melding uit te sturen.");
                builder.setPositiveButton("Melding sturen",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                NavHostFragment.findNavController(FirstFragment.this)
//                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("kidLost", true);
                                editor.apply();
                                buttonLost.setVisibility(GONE);
                                buttonFound.setVisibility(VISIBLE);

                                Map<String, Object> kid = new HashMap<>();
                                kid.put("id", id);
                                kid.put("found", false);
                                // send ID to database
                                Integer idInteger = new Integer(id);
                                db.collection("kids").document(idInteger.toString()).set(kid);
                            }
                        });
                builder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // found button
        buttonFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Weet u het zeker?");
                builder.setMessage("U staat op het punt om de melding te verwijderen.");
                builder.setPositiveButton("Melding verwijderen",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("kidLost", false);
                                editor.apply();

                                buttonLost.setVisibility(VISIBLE);
                                buttonFound.setVisibility(GONE);
                                buttonMic.setVisibility(INVISIBLE);

                                db.collection("kids").document(id.toString()).delete();
//
                            }
                        });
                builder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
}