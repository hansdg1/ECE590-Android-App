package edu.ksu.ece590.androideffectsdemo;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    //.add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }*/

    TextView MainContent;
    TextView TitleContent;
    Button ReverbButton;
    Button AutotuneButton;
    Button PitchButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find View-elements
        TitleContent = (TextView) findViewById(R.id.TitleContent);
        MainContent = (TextView) findViewById(R.id.MainContent);
        ReverbButton = (Button) findViewById(R.id.ReverbButton);
        AutotuneButton = (Button) findViewById(R.id.AutotuneButton);
        PitchButton = (Button) findViewById(R.id.PitchButton);

        // create click listener
        View.OnClickListener ReverbClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.reverb_text);
                TitleContent.setText(R.string.reverb_title);
            }
        };

        View.OnClickListener AutotuneClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.autotune_text);
                TitleContent.setText(R.string.autotune_title);
            }
        };

        View.OnClickListener PitchClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.pitch_text);
                TitleContent.setText(R.string.pitch_title);
            }
        };

        // assign click listener to the OK button (btnOK)
        ReverbButton.setOnClickListener(ReverbClick);
        PitchButton.setOnClickListener(PitchClick);
        AutotuneButton.setOnClickListener(AutotuneClick);
    }
}
    /*@Override
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
    }*/

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/

