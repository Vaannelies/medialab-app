package nl.hr.annelies.medialab;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MicrophoneDirection;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.ThreadLocalRandom;


public class SecondFragment extends Fragment {

    String fileName;

    MediaPlayer mediaPlayer = new MediaPlayer();

    EditText nameInput;
    Button cancelButton;
    Button submitButton;
    FloatingActionButton recordButton;


    // mic
    MediaRecorder mediaRecorder;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        // initialize db
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        try {
//            mediaRecorder.setAudioSource(MicrophoneDirection.MIC_DIRECTION_TOWARDS_USER);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//            mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()
//                    + "/myaudio.3gp");
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//
//        try {
//            mediaRecorder.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        nameInput = view.findViewById(R.id.name_input);
        cancelButton =  view.findViewById(R.id.button_second);
        submitButton =  view.findViewById(R.id.button_third);
        recordButton =  view.findViewById(R.id.button_mic);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                int id = 3;

                Map<String, Object> kid = new HashMap<>();
                kid.put("name", name);
                kid.put("id", id);
                // send ID to database

                db.collection("kids")
                        .add(kid);
            }
        });
//
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println(event);
                System.out.println(event.getAction());
                // up = 1, down is 0

                if(event.getAction() == 0) {

//                    mediaRecorder.start();
                    try {
                        recordAudio(view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(event.getAction() == 1) {
                    stopAudio(view);
                }



                return false;
            }
        });



//        recordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                try {
//                    recordAudio(view);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });



    }

    public void recordAudio (View view) throws IOException
    {
//        cancelButton.setEnabled(true);
//        recordButton.setEnabled(false);
//        recordButton.setEnabled(false);

        System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/myaudio.3gp");
        fileName = "/sdcard/myaudio.3gp";

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            mediaRecorder.setOutputFile(fileName);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stopAudio (View view)
    {

//        cancelButton.setEnabled(false);
//        recordButton.setEnabled(true);

//            recordButton.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        try {
            playAudio(view);
            System.out.println("Hoi");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playAudio (View view) throws IOException
    {
//        playButton.setEnabled(false);
//        recordButton.setEnabled(false);
//        stopButton.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(fileName);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }


}

