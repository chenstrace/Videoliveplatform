package com.panda.videoliveplatform.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.panda.videoliveplatform.R;

public class WelcomeActivity extends Activity {
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        Intent localIntent = new Intent(WelcomeActivity.this, MainFragmentActivity.class);
                        WelcomeActivity.this.startActivity(localIntent);
                        WelcomeActivity.this.finish();

                    }

                }
                , 2000L);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.menu_welcome, paramMenu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }
}
