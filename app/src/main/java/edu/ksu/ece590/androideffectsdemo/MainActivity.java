package edu.ksu.ece590.androideffectsdemo;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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

//import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    boolean recording = false;
    int sampleFreq = 44100;

    TextView MainContent;
    TextView TitleContent;
    ToggleButton ReverbButton;
    ToggleButton DoubleSampleButton;
    ToggleButton HalveSampleButton;

    ToggleButton LowPassButton;
    ToggleButton HighPassButton;
    ToggleButton ReverseButton;


    Button PlayButton;
    Button RecordButton;


    CustomDrawableView customDrawableView;

    AudioPlayTask audioTask;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC); //force media volume control instead of ringtone

        // find View-elements
        TitleContent = (TextView) findViewById(R.id.TitleContent);
        MainContent = (TextView) findViewById(R.id.text_main_content);

        RecordButton = (Button) findViewById(R.id.button_record);
        HalveSampleButton = (ToggleButton) findViewById(R.id.button_halve_samples);
        DoubleSampleButton = (ToggleButton) findViewById(R.id.button_double_samples);
        LowPassButton = (ToggleButton) findViewById(R.id.button_lowpass_filter);
        HighPassButton = (ToggleButton) findViewById(R.id.button_highpass_filter);
        ReverseButton = (ToggleButton) findViewById(R.id.button_reverse);
        ReverbButton = (ToggleButton) findViewById(R.id.button_reverb);
        PlayButton = (Button) findViewById(R.id.button_play);


        customDrawableView = (CustomDrawableView) findViewById(R.id.view);

        // create click listener
        final OnClickListener ReverbClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (text_main_content)
                TitleContent.setText(R.string.reverb_effect_name);
                MainContent.setText(R.string.reverb_effect_desc);
            }
        };

        OnClickListener DoubleSampleClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (text_main_content)
                TitleContent.setText(R.string.double_sample_title);
                MainContent.setText(R.string.double_sample_text);
            }
        };

        OnClickListener HalveSampleClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (text_main_content)
                TitleContent.setText(R.string.halve_sample_title);
                MainContent.setText(R.string.halve_sample_text);
            }
        };


        // create click listener
        OnClickListener LowPassClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (text_main_content)
                TitleContent.setText(R.string.lowpass_effect_name);
                MainContent.setText(R.string.lowpass_effect_desc);
            }
        };

        OnClickListener HighPassClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (text_main_content)
                TitleContent.setText(R.string.highpass_effect_name);
                MainContent.setText(R.string.highpass_effect_desc);
            }
        };

        OnClickListener ReverseClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (text_main_content)
                TitleContent.setText(R.string.reverse_effect_name);
                MainContent.setText(R.string.reverse_effect_desc);
            }
        };

        OnClickListener PlayClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordButton.setEnabled(false);
                RecordButton.setClickable(false);

                playRecord();

                RecordButton.setEnabled(true);
                RecordButton.setClickable(true);
            }
        };

        OnClickListener RecordClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the button was pressed and we were recording, we want to stop
                if (recording) {
                    RecordButton.setText("Rec");
                    RecordButton.setBackgroundColor(Color.LTGRAY);
                    PlayButton.setEnabled(true);
                    PlayButton.setClickable(true);

                    recording = false;
                } else {
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

                    //reset main text for new record
                    TitleContent.setText(R.string.start_title);
                    MainContent.setText(R.string.start_text);

                    Thread recordThread = new Thread(new Runnable() {

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
        RecordButton.setOnClickListener(RecordClick);
        HalveSampleButton.setOnClickListener(HalveSampleClick);
        DoubleSampleButton.setOnClickListener(DoubleSampleClick);
        LowPassButton.setOnClickListener(LowPassClick);
        HighPassButton.setOnClickListener(HighPassClick);
        ReverseButton.setOnClickListener(ReverseClick);
        ReverbButton.setOnClickListener(ReverbClick);
        PlayButton.setOnClickListener(PlayClick);

    }


    private void startRecord() {

        File file = new File(Environment.getExternalStorageDirectory(), "AndroidEffects.pcm");

        //Debugging popup thing
        /*
        final String promptStartRecord =
                "startRecord()\n"
                        + file.getAbsolutePath() + "\n";

        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                Toast.makeText(MainActivity.this,
                        promptStartRecord,
                        Toast.LENGTH_LONG).show();
            }});*/

        try {
			//perhaps we should query free space to prevent potential for IO exception
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(
                    sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioData = new short[minBufferSize];

            AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();

            boolean nonZero = false;
            while (recording) {
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

                for (int i = 0; i < numberOfShort; i++) {
                    if (audioData[i] <= 500 && audioData[i] >= -500 && !nonZero)
                        continue; //try to remove some white space
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


    private class AudioPlayTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
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

                if (HalveSampleButton.isChecked()) {
                    eController.AddEffect(new HalveSampleEffect());
                    //Halve the sample effect
                }

                //Leave like this to demonstrate signal loss/degradation.
                //I.e We lost part of the signal from halving. It cannot be regained by doubling.

                if (DoubleSampleButton.isChecked()) {
                    eController.AddEffect(new DoubleSampleEffect());
                    //Double the sample effect
                }
                if (LowPassButton.isChecked()) {
                    eController.AddEffect(new LowPassEffect(100.0f, sound.NumberOfSamples() / sound.SampleRate()));
                }
                if (HighPassButton.isChecked()) {
                    eController.AddEffect(new HighPassEffect(0.5f, sound.NumberOfSamples() / sound.SampleRate()));
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

                audioTrack.write(sound.GetBuffer(), 0, sound.GetBuffer().length);

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

    private void playRecord() {
        audioTask = new AudioPlayTask();
        audioTask.execute();
    }

    public void openTutorial(View view) {
        Intent intent = new Intent(this, DisplayTutorial.class);
        startActivity(intent);
    }
}
