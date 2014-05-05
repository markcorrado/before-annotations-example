package com.example.testAppBeforeAnnotations;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BeforeAnnotationsActivity extends Activity {

    TextView beerCountView;
    int beerCount;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        beerCountView = (TextView) findViewById(R.id.countView);

        final View addBeerButton = findViewById(R.id.addBeerButton);
        addBeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBeerButtonClicked();
            }
        });

        loadBeerCount();
    }

    void addBeerButtonClicked() {
        beerCount++;
        saveBeerCount(beerCount);
        updateBeerViews();
    }

    void saveBeerCount(final int beerCount) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                    getPreferences(MODE_PRIVATE)
                            .edit()
                            .putInt("beerCount", beerCount)
                            .commit();
                    return null;
            }

        }.execute();
    }

    private void updateBeerViews() {
        if (beerCount == 0) {
            setTitle("Still Sober");
            beerCountView.setText("");
        } else {
            setTitle("Drinking");
            beerCountView.setText(Integer.toString(beerCount));
        }
    }

    void loadBeerCount() {
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                int beerCount = preferences.getInt("beerCount", 0);
                return beerCount;
            }

            @Override
            protected void onPostExecute(Integer beerCount) {
                beerCountLoaded(beerCount);
            }
        }.execute();
    }

    void beerCountLoaded(int beerCount) {
        this.beerCount = beerCount;
        updateBeerViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.beer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.emergency) {
            emergencySelected();
            return true;
        }
        return false;
    }

    void emergencySelected() {
        beerCount = 0;
        saveBeerCount(beerCount);
        updateBeerViews();
    }
}
