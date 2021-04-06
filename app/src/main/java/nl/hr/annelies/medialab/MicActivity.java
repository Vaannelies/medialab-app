package nl.hr.annelies.medialab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MicActivity extends AppCompatActivity {

        String fileName = "/sdcard/myaudio.3gp";

        MediaPlayer mediaPlayer = new MediaPlayer();
        FirebaseFirestore db;

        Button cancelButton;
        Button recordButton;
        ImageView micImage;
        boolean isRecording = false;


        Integer id;


        // mic
        MediaRecorder mediaRecorder;

        @Override
        public void onCreate(

                Bundle savedInstanceState
        ) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mic);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            id = sharedPreferences.getInt("id", 0);


            // initialize db
            db = FirebaseFirestore.getInstance();

            // nav buttons
            Button rulesButton = findViewById(R.id.button_1);
            Button homeButton = findViewById(R.id.button_2);
            Button settingsButton = findViewById(R.id.button_3);

            // Mic image
            micImage = findViewById(R.id.mic_image);

            rulesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MicActivity.this, RulesActivity.class));
                    finish();
                }
            });

            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MicActivity.this, SettingsActivity.class));
                    finish();
                }
            });

            cancelButton = findViewById(R.id.button_second);
            recordButton = findViewById(R.id.button_mic);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
//
            recordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isRecording) {
                        try {
                            recordAudio(view);
                            isRecording = true;
                            recordButton.setText("Verzenden");
                            micImage.setImageResource(R.drawable.mic_orange);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        stopAudio(view);
                        isRecording = false;
                        recordButton.setText("Opnemen");
                        micImage.setImageResource(R.drawable.mic_grey);

                        uploadAudio();

                        // let admins know that a message was recorded
                        Integer idInteger = new Integer(id);
                        db.collection("kids").document(idInteger.toString()).update("hasMessage", true);

                    }
                }

            });

        }


        public void recordAudio (View view) throws IOException
        {

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
        }

        public void stopAudio (View view)
        {
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
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }

        public void uploadAudio() {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference();
            StorageReference filepath = mStorage.child("Audio").child(id + ".3gp");
            Uri uri = Uri.fromFile(new File(fileName));

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Uploaded");
                }
            });
        }
}
