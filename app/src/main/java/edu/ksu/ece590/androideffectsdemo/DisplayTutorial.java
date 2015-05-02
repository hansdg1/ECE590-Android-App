package edu.ksu.ece590.androideffectsdemo;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.ksu.ece590.androideffectsdemo.renders.CustomDrawableView;


public class DisplayTutorial extends ActionBarActivity {

    TextView MainContent;
    TextView TitleContent;
    Button ReverbButton;
    Button LowPassButton;
    Button HighPassButton;
    Button ReverseButton;


    Button AppButton;
    Button RecordButton;


    CustomDrawableView customDrawableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tutorial);

        // find View-elements
        //TitleContent = (TextView) findViewById(R.id.TitleContent);
        //MainContent = (TextView) findViewById(R.id.MainContent);
        //ReverbButton = (ToggleButton) findViewById(R.id.ReverbButton);

        //LowPassButton = (ToggleButton) findViewById(R.id.lowpassFilterButton);
       // HighPassButton = (ToggleButton) findViewById(R.id.highpassButton);
       // ReverseButton = (ToggleButton) findViewById(R.id.reverseButton);

        AppButton = (Button) findViewById(R.id.AppButton);
        //RecordButton = (Button) findViewById(R.id.RecordButton);


        customDrawableView = (CustomDrawableView) findViewById(R.id.view);

        // create click listener
       /* View.OnClickListener ReverbClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.reverb_text);
                TitleContent.setText(R.string.reverb_title);
            }
        };


        // create click listener
        View.OnClickListener LowPassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.lowpass_effect_desc);
                TitleContent.setText(R.string.lowpass_effect_name);
            }
        };

        View.OnClickListener HighPassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.highpass_effect_desc);
                TitleContent.setText(R.string.highpass_effect_name);
            }
        };

        View.OnClickListener ReverseClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.reverse_effect_desc);
                TitleContent.setText(R.string.reverse_effect_name);
            }
        };
        */
        View.OnClickListener AppClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            onBackPressed();
            }
        };

        // assign click listener to the OK button (btnOK)
        //ReverbButton.setOnClickListener(ReverbClick);
        AppButton.setOnClickListener(AppClick);
        /*RecordButton.setOnClickListener(RecordClick);

        LowPassButton.setOnClickListener(LowPassClick);
        HighPassButton.setOnClickListener(HighPassClick);
        ReverseButton.setOnClickListener(ReverseClick); */
    }

}
