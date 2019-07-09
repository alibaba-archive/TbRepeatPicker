package com.teambition.recurrencerule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    RecurrenceRuleHelper recurrenceRuleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView textView = (TextView) findViewById(R.id.title);
        recurrenceRuleHelper = new RecurrenceRuleHelper(MainActivity.this,
                new OnRecurrenceSetListener() {
                    @Override
                    public void onRecurrenceSet(String[] recurrenceRule, String paresedStr) {
                        //do something
                        String ruleString = null;

                        if (recurrenceRule != null && recurrenceRule.length > 0) {
                            for (int i = 0; i < recurrenceRule.length; i++) {
                                ruleString += recurrenceRule[i];
                            }
                        }
                        textView.setText(paresedStr + ":" + ruleString + "\n");
                    }
                });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recurrenceRuleHelper.startSetRecurrence();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

}
