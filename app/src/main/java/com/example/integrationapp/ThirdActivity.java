package com.example.integrationapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThirdActivity extends AppCompatActivity {
    TextView email, name;
    Button log_out;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        email = findViewById(R.id.textView4);
        name = findViewById(R.id.textView3);
        log_out = findViewById(R.id.logOut);
        circleImageView = findViewById(R.id.profile_image);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        try {
                          String full_name =   object.getString("name");
                          String email_id = object.getString("email");
                          String url = object.getJSONObject("picture").getJSONObject("data").getString("url");

                          name.setText(full_name);
                          email.setText(email_id);
                          Picasso.get().load(url).noFade().into(circleImageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ThirdActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you Sure!!")
                        .setMessage("Do you want to Log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginManager.getInstance().logOut();
                                startActivity(new Intent(ThirdActivity.this, MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }
}