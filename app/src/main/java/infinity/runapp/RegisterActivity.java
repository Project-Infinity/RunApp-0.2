package infinity.runapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
* Created by ADC on 3/2/2015.
*/
public class RegisterActivity extends ActionBarActivity implements OnClickListener
{
    private EditText mEmail, mPass1, mPass2, mLName, mFName;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    private static final String URL = "http://cgi.soic.indiana.edu/~team36/infinity/register.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = (EditText)findViewById(R.id.regEmail);
        mPass1 = (EditText)findViewById(R.id.regPass1);
        mPass2 = (EditText)findViewById(R.id.regPass2);
        mFName = (EditText)findViewById(R.id.regFName);
        mLName = (EditText)findViewById(R.id.regLName);

        Button register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(this);
    }

    public void onClick(View v) { new CreateUser().execute(); }

    class CreateUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Registering..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;
            String email = mEmail.getText().toString().toUpperCase();
            String pass1 = mPass1.getText().toString();
            String pass2 = mPass2.getText().toString();
            String fname = mFName.getText().toString();
            String lname = mLName.getText().toString();

            try {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("userEmail", email));
                    params.add(new BasicNameValuePair("fName", fname));
                    params.add(new BasicNameValuePair("lName", lname));
                    params.add(new BasicNameValuePair("pswd", pass1));
                    params.add(new BasicNameValuePair("pswd2", pass2));

                    Log.d("request!", "starting");

                    JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

                    Log.d("Login attempt", json.toString());

                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        addUserSQLite(email, fname, lname);
                        Log.d("User Created!", json.toString());
                        finish();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return json.getString(TAG_MESSAGE);
                    } else {
                        Log.d("Registration Failed!", json.getString(TAG_MESSAGE));
                        return json.getString(TAG_MESSAGE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            return null;
        }

        protected void onPostExecute(String file_url){
            pDialog.dismiss();
            if(file_url != null){
                Toast.makeText(RegisterActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void addUserSQLite(String newEmail, String newFname, String newLname){
        InfinityDBHandler infinityDbHandler = new InfinityDBHandler(this, null, null, 1);

        User newUser = new User(newEmail, newFname, newLname);
        infinityDbHandler.addUser(newUser);
    }

    public void goLogin(View view){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
