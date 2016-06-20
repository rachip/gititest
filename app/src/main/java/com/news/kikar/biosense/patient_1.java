package com.news.kikar.biosense;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class patient_1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_1);
        Button switchActivityBtn = (Button) findViewById(R.id.bSwitchActivity);
        switchActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedStartActivity();
            }
        });

        Button saveme=(Button)findViewById(R.id.save);


        saveme.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v)
            {
                //ALERT MESSAGE
                Toast.makeText(getBaseContext(), "Please wait, connecting to server.", Toast.LENGTH_LONG).show();

                try{

                    // URLEncode user defined data

                    String loginValue    = URLEncoder.encode(login.getText().toString(), "UTF-8");
                    String fnameValue  = URLEncoder.encode(fname.getText().toString(), "UTF-8");
                    String emailValue   = URLEncoder.encode(email.getText().toString(), "UTF-8");
                    String passValue    = URLEncoder.encode(pass.getText().toString(), "UTF-8");

                    // Create http cliient object to send request to server

                    HttpClient Client = new DefaultHttpClient();

                    // Create URL string

                    String URL = "http://androidexample.com/media/webservice/httpget.php?user="+loginValue+"&name="+fnameValue+"&email="+emailValue+"&pass="+passValue;

                    //Log.i("httpget", URL);

                    try
                    {
                        String SetServerString = "";

                        // Create Request to server and get response

                        HttpGet httpget = new HttpGet(URL);
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        SetServerString = Client.execute(httpget, responseHandler);

                        // Show response on activity

                        content.setText(SetServerString);
                    }
                    catch(Exception ex)
                    {
                        content.setText("Fail!");
                    }
                }
                catch(UnsupportedEncodingException ex)
                {
                    content.setText("Fail");
                }
            }
        });
    }

    }

    private void animatedStartActivity() {
        // we only animateOut this activity here.
        // The new activity will animateIn from its onResume() - be sure to implement it.
        final Intent intent = new Intent(getApplicationContext(), patient2.class);
        // disable default animation for new intent
        startActivity(intent);



    }
}

