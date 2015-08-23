package com.sachin.app.popularmovies.Movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sachin.app.popularmovies.Helper.CheckConnection;
import com.sachin.app.popularmovies.R;

public class ExceptionActivity extends AppCompatActivity {

    private Toolbar toolBar;
    TextView exceptionText, exceptionAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);

        toolBar = (Toolbar) findViewById(R.id.app_bar);
        toolBar.setTitle(R.string.app_name);
        setSupportActionBar(toolBar);

        exceptionText = (TextView) findViewById(R.id.exceptionTxt);
        exceptionAction = (TextView) findViewById(R.id.exceptionAction);

        exceptionAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnection.isConnection(ExceptionActivity.this)) {
                    Intent i = new Intent(ExceptionActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(ExceptionActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exception, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
