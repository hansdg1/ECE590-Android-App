package edu.ksu.ece590.androideffectsdemo;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.os.Build;
import android.widget.CheckBox;
import android.widget.Toast;

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

import edu.ksu.ece590.androideffectsdemo.effects.EffectsController;
import edu.ksu.ece590.androideffectsdemo.effects.HighPassEffect;
import edu.ksu.ece590.androideffectsdemo.effects.LowPassEffect;
import edu.ksu.ece590.androideffectsdemo.effects.ReverbEffect;
import edu.ksu.ece590.androideffectsdemo.effects.ReverseEffect;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;


public class MainActivity extends ActionBarActivity {


    boolean recording = false;
    int sampleFreq = 44100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }



    public void recordBtnOnClick(View v) {

        Button recBtn = (Button)findViewById(R.id.recordBtn);
        Button playBtn = (Button)findViewById(R.id.playBtn);

        //if the button was pressed and we were recording, we want to stop
        if(recording){
            recBtn.setText("Record");
            playBtn.setEnabled(true);


            recording = false;



        }else {
        //we were not recording, and we want to start
            recBtn.setText("Stop");
            playBtn.setEnabled(false);

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

    public void playBtnOnClick(View v) {

        Button recBtn = (Button)findViewById(R.id.recordBtn);
        Button playBtn = (Button)findViewById(R.id.playBtn);

        recBtn.setEnabled(false);

        playRecord();

        recBtn.setEnabled(true);
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

            while(recording){
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);
                for(int i = 0; i < numberOfShort; i++){
                    dataOutputStream.writeShort(audioData[i]);
                }
            }

            audioRecord.stop();
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void playRecord(){


        CheckBox reverbCheckbox = (CheckBox)findViewById(R.id.reverbCheckbox);
        CheckBox reverseCheckbox = (CheckBox)findViewById(R.id.reverseCheckBox);
        CheckBox lowpassCheckbox = (CheckBox)findViewById(R.id.lowPassCheckbox);
        CheckBox highpassCheckbox = (CheckBox)findViewById(R.id.highPassCheckbox);





        File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");

        int shortSizeInBytes = Short.SIZE/Byte.SIZE;
        int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);

        short[] audioData = new short[bufferSizeInBytes];

        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
            while(dataInputStream.available() > 0) {
                audioData[i] += dataInputStream.readShort();

                i++;
            }

            dataInputStream.close();



            //Just something to test with. This should all be refactored
            SoundPCM sound = new SoundPCM(audioData,44100);

            EffectsController eController = new EffectsController();



            if(reverbCheckbox.isChecked())
            {
                //add the effects
                eController.AddEffect(new ReverbEffect(0.25f,250,44100));
            }



            if(lowpassCheckbox.isChecked())
            {
                eController.AddEffect((new LowPassEffect(100.0f, sound.NumberOfSamples()/sound.SampleRate())));
            }
            if(highpassCheckbox.isChecked())
            {
                eController.AddEffect((new HighPassEffect(100.0f, sound.NumberOfSamples()/sound.SampleRate())));
            }
            if(reverseCheckbox.isChecked())
            {

                eController.AddEffect(new ReverseEffect());
            }

            sound = eController.CalculateEffects(sound);




            final String promptPlayRecord =
                    "PlayRecord()\n"
                            + file.getAbsolutePath() + "\n";

            Toast.makeText(MainActivity.this,
                    promptPlayRecord,
                    Toast.LENGTH_LONG).show();

            AudioTrack audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    sound.GetBufferSizeInBytes(),
                    AudioTrack.MODE_STREAM);


            audioTrack.play();
            audioTrack.write(sound.GetBuffer(), 0, sound.NumberOfSamples());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
