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


public class LoginActivity extends ActionBarActivity implements OnClickListener {

    private EditText mEmail, mPassword;

    private Button mLogin;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    //URL to get JSON Array
    private static final String URL = "http://cgi.soic.indiana.edu/~team36/infinity/login.php";
    //JSON Node Names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_FNAME = "fname";
    private static final String TAG_LNAME = "lname";

    public String eMail;
    public String firstName;
    public String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mLogin = (Button) findViewById(R.id.btnLogin);
        mLogin.setOnClickListener(this);
    }

    public void onClick(View v) { new UserLogin().execute();}

    class UserLogin extends AsyncTask <String, String, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging In...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args){

            int success;
            String email = mEmail.getText().toString().toUpperCase();
            String pass = mPassword.getText().toString();

            try{
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userEmail", email));
                params.add(new BasicNameValuePair("pswd", pass));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

                Log.d("Login attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);

                if(success == 1)
                {
                    Log.d("Successful Login!", json.toString());
                    eMail = json.getString(TAG_EMAIL);
                    firstName = json.getString(TAG_FNAME);
                    lastName = json.getString(TAG_LNAME);
                    addUserSQLite(eMail, firstName, lastName);
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    return json.getString(TAG_MESSAGE);
                }
                else
                {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url){
            pDialog.dismiss();

            if(file_url != null){
                Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }

    public void goReg(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void addUserSQLite(String newEmail, String newFname, String newLname){
        InfinityDBHandler infinityDbHandler = new InfinityDBHandler(this, null, null, 1);

        User newUser = new User(newEmail, newFname, newLname);
        infinityDbHandler.addUser(newUser);

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

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
