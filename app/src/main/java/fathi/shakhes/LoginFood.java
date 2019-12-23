package fathi.shakhes;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sdsmdg.tastytoast.TastyToast;
import org.json.JSONException;
import org.json.JSONObject;

import shakhes.R;

public class LoginFood extends AppCompatActivity {
    AppSessionManager sessions ;
    CheckBox checkBox  ;
    String url_food_login = "https://dining.sharif.ir/api/login?";
    String url_food_profile = "https://dining.sharif.ir/api/profile?access_token=";
    Typeface typeFace ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui();
        CreatingLogin();
    }
    public void foodLogin(final String username, final String pass) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url_food_login + "username=" + username + "&password=" + pass, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("status").matches("success")) {
                                    sessions.createFoodLoginSession(username,
                                            response.getString("access_token"));
                                    getFoodData(response.getString("access_token"), username);

                                    if(sessions.getcheckboxstate_food()){
                                    sessions.saveuserfood(username);
                                    sessions.savepassfood(pass);
                                    }
                                    if (!checkBox.isChecked()) {
                                        sessions.saveuserfood("");
                                        sessions.savepassfood("");
                                    }
                                } else {
                                    TastyToast
                                            .makeText(getApplicationContext(), "نام کاربری یا گذرواژه اشتباه است !", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                                            .setMargin(0.015f,0f);
                                }
                            } catch (Exception e) {
                                TastyToast
                                        .makeText(getApplicationContext(), "نام کاربری یا گذرواژه اشتباه است !", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                                        .setMargin(0.015f,0f);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    TastyToast
                            .makeText(getApplicationContext(), error.getMessage().toLowerCase(), TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                            .setMargin(0.015f,0f);
                }
            }) ;

// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void getFoodData(String acc, final String stunum) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_food_profile + acc, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.getString("success").matches("true")) {
                                TastyToast
                                        .makeText(getApplicationContext(), "اشکالی رخ داده است", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                                        .setMargin(0.015f,0f);
                            } else {
                                sessions.insertFoodProfileData(response.getString("name"), response.getString("family"),
                                        stunum, String.valueOf(response.getInt("balance")));

                                TastyToast
                                        .makeText(getApplicationContext(), "شما وارد شدید!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                                        .setMargin(0.015f,0f);

                                Intent FoodActivityIntent1 = new Intent(LoginFood.this, FoodActivity.class);
                                startActivity(FoodActivityIntent1);
                                finish();
                            }
                        } catch (Exception e) {
                            try {
                                sessions.insertFoodProfileData(response.getString("name"), response.getString("family"),
                                        stunum, String.valueOf(response.getInt("balance")));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            TastyToast
                                    .makeText(getApplicationContext(), "شما وارد شدید!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                                    .setMargin(0.015f,0f);
                            Intent FoodActivityIntent = new Intent(LoginFood.this, FoodActivity.class);
                            startActivity(FoodActivityIntent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TastyToast
                        .makeText(getApplicationContext(), error.getMessage().toLowerCase(), TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        .setMargin(0.015f,0f);
            }
        });

//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                Map<String, String> _params = new HashMap<String, String>();
//
//                _params.put("Content-type", "application/json");
//                _params.put("X-Token", sessions.getUserDetails().get("access_token"));
//
//                return _params;
//            }
//        };

// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void ui() {
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessions = new AppSessionManager(getApplicationContext());
        getSupportActionBar().hide();
        setContentView(R.layout.login_food);
        checkBox = (CheckBox) findViewById(R.id.food_login_checkbox);
         checkBox.setTypeface(typeFace);

        if (sessions.getcheckboxstate_food()) {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }
        TextView login_title = (TextView)findViewById(R.id.login_title_food);
        TextView Txt_User_dorm = (TextView)findViewById(R.id.Txt_User_food);
        TextView btn_login_dorm = (TextView)findViewById(R.id.btn_login_food);

        login_title.setTypeface(typeFace);
        Txt_User_dorm.setTypeface(typeFace);
        btn_login_dorm.setTypeface(typeFace);

    }
    private String convertNum(String in) {
        String ret = in;
        ret = ret.replaceAll("۰", "0");
        ret = ret.replaceAll("۱", "1");
        ret = ret.replaceAll("۲", "2");
        ret = ret.replaceAll("۳", "3");
        ret = ret.replaceAll("۴", "4");
        ret = ret.replaceAll("۵", "5");
        ret = ret.replaceAll("۶", "6");
        ret = ret.replaceAll("۷", "7");
        ret = ret.replaceAll("۸", "8");
        ret = ret.replaceAll("۹", "9");
        return ret;
    }
    private void CreatingLogin() {
        Button btnLogin = (Button) findViewById(R.id.btn_login_food);

        final EditText txtUsername = (EditText) findViewById(R.id.Txt_User_food);
        final EditText txtPassword = (EditText) findViewById(R.id.Txt_Pass_food);

        if(sessions.getuserfood()!="" || sessions.getpassfood()!=""){
            txtUsername.setText(sessions.getuserfood());
            txtPassword.setText(sessions.getpassfood());
        }

        txtUsername.setSelection(txtUsername.getText().length());
        txtPassword.setSelection(txtPassword.getText().length());
        // Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Internet.IsConnectionAvailable(getApplicationContext())){
                    TastyToast
                            .makeText(getApplicationContext(), "به اینترنت دسترسی ندارید!", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                            .setMargin(0.015f,0f);
                    if (Build.VERSION.SDK_INT >= 26) {
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
                    } else {
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
                    }


                }
                else {
                     EmptyLoginBox();

                    if (txtUsername.getText().toString().trim().length() > 0 && txtPassword.getText().toString().trim().length() > 0) {

                        TastyToast
                                .makeText(getApplicationContext(), "در حال ورود...", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT)
                                .setMargin(0.015f,0f);

                        foodLogin(convertNum(txtUsername.getText().toString()), txtPassword.getText().toString());
                    }
                }

            }
        });

    }
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        Intent BackToMain = new Intent(LoginFood.this, SplashScreen.class);
        startActivity(BackToMain);
        finish();
    }
    public void EmptyLoginBox() {
        final EditText txtUsername = (EditText) findViewById(R.id.Txt_User_food);
        final EditText txtPassword = (EditText) findViewById(R.id.Txt_Pass_food);
        String email = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty()) {
            txtUsername.setError("لطفا اطلاعات را کامل کنید");
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
        }
        if (password.isEmpty()) {
            txtPassword.setError("لطفا اطلاعات را کامل کنید");
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
        }
        else {
            txtUsername.setError(null);
        }
    }
    public void FoodLoginCheckBox(View view) {
        if (checkBox.isChecked()) {
            sessions.setcheckboxstate_food(true);
        }
        if (!checkBox.isChecked()){
            sessions.setcheckboxstate_food(false);
        }
    }
}