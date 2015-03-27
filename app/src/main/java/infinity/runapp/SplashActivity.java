package infinity.runapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class SplashActivity extends ActionBarActivity {

    private Thread mSplash;
    private final int TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mSplash = new Thread() {

            @Override
            public void run() {
                synchronized (this) {
                    try
                    {
                        wait(TIME);
                    }
                    catch (InterruptedException e)
                    {
                    }
                    finally
                    {
                        checkForUserSQLite();
                        finish();
                    }
                }
            }
        };
        mSplash.start();
    }


    public void checkForUserSQLite()
    {
        InfinityDBHandler infinityDbHandler = new InfinityDBHandler(this, null, null, 1);

        if(infinityDbHandler.checkUser())
        {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        else
        {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
