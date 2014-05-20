package com.example.testAppBeforeAnnotations;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import org.androidannotations.annotations.EActivity;

@EActivity
public class BeforeAnnotationsActivity extends Activity {

    TextView countView;
    int count;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        countView = (TextView) findViewById(R.id.countView);

        final View addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton();
            }
        });

        loadCount();
    }

    void addButton() {
        count++;
        saveBeerCount(count);
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
        if (count == 0) {
            setTitle("None");
            countView.setText("");
        } else {
            setTitle("Some");
            countView.setText(Integer.toString(count));
        }
    }

    void loadCount() {
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                int count = preferences.getInt("count", 0);
                return count;
            }

            @Override
            protected void onPostExecute(Integer beerCount) {
                countLoaded(beerCount);
            }
        }.execute();
    }

    void countLoaded(int beerCount) {
        this.count = beerCount;
        updateBeerViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
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
        count = 0;
        saveBeerCount(count);
        updateBeerViews();
    }
}
