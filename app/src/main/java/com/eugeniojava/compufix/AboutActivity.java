package com.eugeniojava.compufix;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(getString(R.string.about_activity_title));

        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewCourse = findViewById(R.id.textViewCourse);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        TextView textViewCollegeName = findViewById(R.id.textViewCollegeName);

        textViewName.setText(getString(R.string.about_activity_name));
        textViewCourse.setText(getString(R.string.about_activity_course));
        textViewEmail.setText(getString(R.string.about_activity_email));
        textViewDescription.setText(getString(R.string.about_activity_description));
        textViewCollegeName.setText(getString(R.string.about_activity_college_name));
    }
}
