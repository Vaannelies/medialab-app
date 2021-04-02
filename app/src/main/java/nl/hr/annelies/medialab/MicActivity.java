package nl.hr.annelies.medialab;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MicrophoneDirection;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.ThreadLocalRandom;


public class MicActivity extends AppCompatActivity {

        String fileName = "/sdcard/myaudio.3gp";

        MediaPlayer mediaPlayer = new MediaPlayer();
        FirebaseFirestore db;

        EditText nameInput;
        Button cancelButton;
        Button submitButton;
        FloatingActionButton recordButton;
        FloatingActionButton playButton;



        int id = 3;


        // mic
        MediaRecorder mediaRecorder;

        @Override
        public void onCreate(

                Bundle savedInstanceState
        ) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mic);

            // initialize db
            db = FirebaseFirestore.getInstance();
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


            nameInput = findViewById(R.id.name_input);
            cancelButton = findViewById(R.id.button_second);
            submitButton = findViewById(R.id.button_third);
            recordButton = findViewById(R.id.button_mic);
            playButton = findViewById(R.id.button_play);

            playButton.setEnabled(false);


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
//                    NavHostFragment.findNavController(nl.hr.annelies.medialab.SecondFragment.this)
//                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }
            });

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadAudio();
                    String name = nameInput.getText().toString();


                    Map<String, Object> kid = new HashMap<>();
                    kid.put("name", name);
                    kid.put("id", id);
//                kid.put("mic", uploadAudio());
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

                    if (event.getAction() == 0) {

//                    mediaRecorder.start();
                        try {
                            recordAudio(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (event.getAction() == 1) {
                        stopAudio(view);
                    }


                    return false;
                }
            });


            playButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                playButton.setEnabled(false);
                    try {
                        playAudio(view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

            Long tsLong = System.currentTimeMillis()/1000;
            fileName = "/sdcard/myaudio_" + tsLong.toString() + ".3gp";
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
            playButton.setEnabled(false);
        }

        public void stopAudio (View view)
        {

//        cancelButton.setEnabled(false);
//        recordButton.setEnabled(true);

//            recordButton.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            playButton.setEnabled(true);
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

        public void uploadAudio() {

//        StorageReference filepath = db;
//        StorageReference filepath = db.getReference().child("Audio").child("new_audio.3gp");
//        Uri uri = Uri.fromFile(new File(fileName));
            StorageReference mStorage = FirebaseStorage.getInstance().getReference();
            StorageReference filepath = mStorage.child("Audio").child(id + ".3gp");
            Uri uri = Uri.fromFile(new File(fileName));

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Uploaded");
                }
            });

//        return uri;

        }




}
