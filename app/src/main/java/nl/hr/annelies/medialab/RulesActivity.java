package nl.hr.annelies.medialab;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RulesActivity extends AppCompatActivity {
    ListView listView;
//    String[] rules = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button homeButton = findViewById(R.id.button_2);
        Button settingsButton = findViewById(R.id.button_3);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RulesActivity.this, SettingsActivity.class));
                finish();
            }
        });
//        rules[0] = "Bladiebla :)";
//        rules[1] = "Blieblabloe :D";
//
//        listView = findViewById(R.id.list_view);
//        listView.setAdapter(new ArrayAdapter<String>(RulesActivity.this, R.layout.rule, rules));


    }
}
