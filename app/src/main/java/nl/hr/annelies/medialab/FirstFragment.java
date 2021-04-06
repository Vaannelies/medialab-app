package nl.hr.annelies.medialab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

public class FirstFragment extends Fragment {

    private Boolean kidLost;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        kidLost = sharedPreferences.getBoolean("kidLost", false);

        if(!kidLost) {
            buttonLost.setVisibility(View.VISIBLE);
            buttonFound.setVisibility(View.GONE);
        } else {
            buttonLost.setVisibility(View.GONE);
            buttonFound.setVisibility(View.VISIBLE);
        }


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
                                buttonLost.setVisibility(View.GONE);
                                buttonFound.setVisibility(View.VISIBLE);

                                startActivity(new Intent(getActivity(), MicActivity.class));
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
//                                NavHostFragment.findNavController(FirstFragment.this)
//                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("kidLost", false);
                                editor.apply();

                                buttonLost.setVisibility(View.VISIBLE);
                                buttonFound.setVisibility(View.GONE);
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