package edu.ksu.ece590.androideffectsdemo;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.ksu.ece590.androideffectsdemo.effects.DoubleSampleEffect;
import edu.ksu.ece590.androideffectsdemo.effects.EffectsController;
import edu.ksu.ece590.androideffectsdemo.effects.HalveSampleEffect;
import edu.ksu.ece590.androideffectsdemo.effects.HighPassEffect;
import edu.ksu.ece590.androideffectsdemo.effects.LowPassEffect;
import edu.ksu.ece590.androideffectsdemo.effects.ReverbEffect;
import edu.ksu.ece590.androideffectsdemo.effects.ReverseEffect;
import edu.ksu.ece590.androideffectsdemo.renders.CustomDrawableView;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

public class MainActivity extends ActionBarActivity {


    boolean recording = false;
    int sampleFreq = 44100;

    TextView MainContent;
    TextView TitleContent;
    ToggleButton ReverbButton;
<<<<<<< HEAD
    ToggleButton DoubleSampleButton;
    ToggleButton HalveSampleButton;

=======
>>>>>>> PaulUI
    ToggleButton LowPassButton;
    ToggleButton HighPassButton;
    ToggleButton ReverseButton;


    Button PlayButton;
    Button RecordButton;


    CustomDrawableView customDrawableView;

    AudioPlayTask audioTask;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find View-elements
        TitleContent = (TextView) findViewById(R.id.TitleContent);
        MainContent = (TextView) findViewById(R.id.MainContent);
        ReverbButton = (ToggleButton) findViewById(R.id.ReverbButton);
<<<<<<< HEAD
        DoubleSampleButton = (ToggleButton) findViewById(R.id.DoubleSampleButton);
        HalveSampleButton = (ToggleButton) findViewById(R.id.HalveSampleButton);
=======
>>>>>>> PaulUI

        LowPassButton = (ToggleButton) findViewById(R.id.lowpassFilterButton);
        HighPassButton = (ToggleButton) findViewById(R.id.highpassButton);
        ReverseButton = (ToggleButton) findViewById(R.id.reverseButton);

        PlayButton = (Button) findViewById(R.id.PlayButton);
        RecordButton = (Button) findViewById(R.id.RecordButton);


        customDrawableView = (CustomDrawableView) findViewById(R.id.view);

        // create click listener
        final View.OnClickListener ReverbClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
<<<<<<< HEAD
                MainContent.setText(R.string.reverb_text);
                TitleContent.setText(R.string.reverb_title);
            }
        };

        View.OnClickListener DoubleSampleClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.double_sample_text);
                TitleContent.setText(R.string.double_sample_title);
            }
        };

        View.OnClickListener HalveSampleClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.halve_sample_text);
                TitleContent.setText(R.string.halve_sample_title);
=======
                MainContent.setText(R.string.reverb_effect_desc);
                TitleContent.setText(R.string.reverb_effect_name);
>>>>>>> PaulUI
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

        View.OnClickListener PlayClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RecordButton.setEnabled(false);
                RecordButton.setClickable(false);


                        playRecord();


                RecordButton.setEnabled(true);
                RecordButton.setClickable(true);
            }
        };

        View.OnClickListener RecordClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the button was pressed and we were recording, we want to stop
                if(recording){
                    RecordButton.setText("Rec");
                    RecordButton.setBackgroundColor(Color.LTGRAY);
                    PlayButton.setEnabled(true);
                    PlayButton.setClickable(true);

                    recording = false;



                }else {
                    //we were not recording, and we want to start
                    RecordButton.setText("Stop");
                    RecordButton.setBackgroundColor(Color.RED);
                    PlayButton.setEnabled(false);
                    PlayButton.setClickable(false);


                    //reset the toggle buttons
                    HalveSampleButton.setChecked(false);
                    DoubleSampleButton.setChecked(false);
                    LowPassButton.setChecked(false);
                    HighPassButton.setChecked(false);
                    ReverseButton.setChecked(false);
                    ReverbButton.setChecked(false);
                    customDrawableView.clearImageBuffer();


                    Thread recordThread = new Thread(new Runnable(){

                        @Override
                        public void run() {
                            //this should be locked.
                            recording = true;
                            startRecord();
                        }



                    });

                    recordThread.start();

                }
            }
        };


        // assign click listener to the OK button (btnOK)
        ReverbButton.setOnClickListener(ReverbClick);
<<<<<<< HEAD
        HalveSampleButton.setOnClickListener(HalveSampleClick);
        DoubleSampleButton.setOnClickListener(DoubleSampleClick);
=======
>>>>>>> PaulUI
        PlayButton.setOnClickListener(PlayClick);
        RecordButton.setOnClickListener(RecordClick);

        LowPassButton.setOnClickListener(LowPassClick);
        HighPassButton.setOnClickListener(HighPassClick);
        ReverseButton.setOnClickListener(ReverseClick);



    }


    private void startRecord(){

        File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");

        final String promptStartRecord =
                "startRecord()\n"
                        + file.getAbsolutePath() + "\n";

        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                Toast.makeText(MainActivity.this,
                        promptStartRecord,
                        Toast.LENGTH_LONG).show();
            }});

        try {
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioData = new short[minBufferSize];

            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();

            boolean nonZero = false;
            while(recording){
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

                for(int i = 0; i < numberOfShort; i++){
                    if(audioData[i] <= 500 && audioData[i] >= -500 && !nonZero) continue; //try to remove some white space
                    else {
                        nonZero = true;
                        dataOutputStream.writeShort(audioData[i]);
                    }



                }
            }

            audioRecord.stop();
            dataOutputStream.close();


            //load the data again so we can instantly draw it

            int shortSizeInBytes = Short.SIZE / Byte.SIZE;
            int bufferSizeInBytes = (int) (file.length() / shortSizeInBytes);

            audioData = new short[bufferSizeInBytes];

            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
            while (dataInputStream.available() > 0) {
                audioData[i] += dataInputStream.readShort();

                i++;
            }

            dataInputStream.close();

            //Just something to test with. This should all be refactored
            SoundPCM sound = new SoundPCM(audioData, sampleFreq);


            customDrawableView.DrawImageBuffer(sound);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private class AudioPlayTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try {

                File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");

                int shortSizeInBytes = Short.SIZE / Byte.SIZE;
                int bufferSizeInBytes = (int) (file.length() / shortSizeInBytes);

                short[] audioData = new short[bufferSizeInBytes];

                InputStream inputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

                int i = 0;
                while (dataInputStream.available() > 0) {
                    audioData[i] += dataInputStream.readShort();

                    i++;
                }

                dataInputStream.close();

                //Just something to test with. This should all be refactored
                SoundPCM sound = new SoundPCM(audioData, sampleFreq);

                EffectsController eController = new EffectsController();

                if (HalveSampleButton.isChecked())
                {
                    eController.AddEffect(new HalveSampleEffect());
                    //Halve the sample effect
                }

                //Leave like this to demonstrate signal loss/degradation.
                //I.e We lost part of the signal from halving. It cannot be regained by doubling.

                if (DoubleSampleButton.isChecked())
                {
                    eController.AddEffect(new DoubleSampleEffect());
                    //Double the sample effect

                }


                if (LowPassButton.isChecked()) {
                    eController.AddEffect((new LowPassEffect(100.0f, sound.NumberOfSamples() / sound.SampleRate())));
                }
                if (HighPassButton.isChecked()) {
                    eController.AddEffect((new HighPassEffect(100.0f, sound.NumberOfSamples() / sound.SampleRate())));
                }
                if (ReverseButton.isChecked()) {

                    eController.AddEffect(new ReverseEffect());
                }
                if (ReverbButton.isChecked()) {
                    //add the effects
                    eController.AddEffect(new ReverbEffect(0.25f, 250, 44100));
                }

                sound = eController.CalculateEffects(sound);




                int minSize = AudioTrack.getMinBufferSize(sampleFreq, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

                AudioTrack audioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        sampleFreq,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,

                        minSize,
                        AudioTrack.MODE_STREAM);

                customDrawableView.update(audioTrack, sound);

                audioTrack.play();


                audioTrack.write(sound.GetBuffer(), 0,sound.GetBuffer().length);

                audioTrack.stop();
                audioTrack.release();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private void playRecord(){


        audioTask = new AudioPlayTask();
        audioTask.execute();



    }

    public void openTutorial(View view) {
        Intent intent = new Intent(this, DisplayTutorial.class);
        startActivity(intent);
    }

}
