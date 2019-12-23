package fathi.shakhes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fathi.shakhes.FoodMessageTab.TabMessage;
import fathi.shakhes.FoodTableTab.TabTable;
import shakhes.R;

public class FoodActivity extends AppCompatActivity {
    TextView[] texts;
    String url_food_picture = "https://dining.sharif.ir/api/picture?access_token=";
    AppSessionManager s;
    String token;
    Button[] foodmenu ;
    TextView toman ;
    ImageView image ;
    Bitmap decodedBytePicture ;
    Intent tabtable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        s = new AppSessionManager(getApplicationContext());
        token = s.getDiningData().get("access_token");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.drawable.shariflogo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View v = LayoutInflater
                .from(getSupportActionBar().getThemedContext())
                .inflate(R.layout.action_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(v, params);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        TextView myTextView2a = (TextView) findViewById(R.id.text2);
        myTextView2a.setText("سامانه تغذیه");
        myTextView2a.setTypeface(typeFace);
         image = (ImageView) findViewById(R.id.image);

        texts = new TextView[3];
        texts[0] = (TextView) findViewById(R.id.foodprofile_name);
        texts[1] = (TextView) findViewById(R.id.foodprofile_charge);
        texts[2] = (TextView) findViewById(R.id.foodprofile_id);



        foodmenu = new Button[4];

        foodmenu[0] = findViewById(R.id.daysale_submit);
        foodmenu[1] = findViewById(R.id.daysale) ;
        foodmenu[2] = findViewById(R.id.messages);
        foodmenu[3] =findViewById(R.id.contactus);
        toman = findViewById(R.id.toman);


        foodmenu[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabtable = new Intent(FoodActivity.this, TabTable.class);
                tabtable.putExtra("token",token);
                startActivity(tabtable);
                finish();
            }
        });


        foodmenu[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daysalefood = new Intent(FoodActivity.this, DaySaleFood.class);
                daysalefood.putExtra("token",token);
                startActivity(daysalefood);
                finish();
            }
        });

        foodmenu[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tabmessage = new Intent(FoodActivity.this, TabMessage.class);
                tabmessage.putExtra("token",token);
                startActivity(tabmessage);
                finish();
            }
        });

        foodmenu[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactus = new Intent(FoodActivity.this, ContactUsActivity.class);
                startActivity(contactus);
                finish();
            }
        });


        for (int i = 0; i <foodmenu.length ; i++) {
            foodmenu[i].setTypeface(typeFace);

        }
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        for (int i = 0; i < texts.length; i++) {
            texts[i].setTypeface(typeFace);
        }
        toman.setTypeface(typeFace);


        new MyTask().execute("get picture");


        String[] data = s.getFoodProfileData();
        String[] data_temp = new String[3];
        data_temp[0] = data[0] +" "+ data[1] ;

        data_temp[1] = data[2] ;
        data_temp[2] = data[3] ;

        for (int i = 0; i < 3; i++) {
            texts[i].setText((data_temp[i]).trim());
        }
        if(AppSessionManager.IsAccountNegative){
            texts[2].setTextColor(Color.RED);
        }
        else {
            texts[2].setTextColor(Color.BLACK);
        }

    }
    private class MyTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            getPicture();
            // Do something that takes a long time, for example:


            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            // Do things like hide the progress bar or change a TextView
        }
    }
    public void getPicture() {
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_food_picture + token, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getBoolean("success") == true){
                                byte[] decodedString = Base64.decode(response.getString("picture"), Base64.DEFAULT);
                                decodedBytePicture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                image.setImageBitmap(Bitmap.createScaledBitmap(decodedBytePicture,decodedBytePicture.getWidth(),decodedBytePicture.getHeight(),false));
                                image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> _params = new HashMap<String, String>();

                _params.put("Content-type", "application/json");

                return _params;

            }
        };

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        s.logout_dining();
        startActivity(new Intent(FoodActivity.this, SplashScreen.class ));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
