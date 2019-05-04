package com.example.testapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                if(isNetworkAvailable())
                {
                    // perform long lived async task ... read something from URL/API etc
                    BackgroundTask bt = new BackgroundTask(MainActivity.this);
                    bt.execute();
                }
                else {

                    // just start the activity and hope for the best...
                    Intent secInt = new Intent(getBaseContext(),SecondActivity.class);
                    Bundle bund = new Bundle();

                    startActivity(secInt,bund);

                }

            }
        });



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    private class Test
    {
        int counter = 0;

    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        MainActivity parent = null;

        public BackgroundTask(MainActivity activity) {
            parent = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            //dialog.setMessage("Doing something, please wait.");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Intent secInt = new Intent(parent,SecondActivity.class);
            Bundle bund = new Bundle();

            startActivity(secInt,bund);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // use http://www.mocky.io to allow random delays to be simulated
                //
                // /v2/5ccdac3e2e00005c15182ad5?mocky-delay=3000ms

                URL url = new URL("http://www.mocky.io/v2/5ccdac3e2e00005c15182ad5?mocky-delay=3000ms");
                URLConnection urlConnection = url.openConnection();
                InputStream in = urlConnection.getInputStream();
                IOUtils.copy(in, System.out);
                //Thread.sleep(5000);  // or hard wired 5 second delay
            }
            catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

    }
}
