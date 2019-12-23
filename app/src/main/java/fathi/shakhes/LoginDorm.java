package fathi.shakhes;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sdsmdg.tastytoast.TastyToast;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import shakhes.R;

public class LoginDorm extends AppCompatActivity {
   AppSessionManager sessions ;
    String url_dorm_login = "https://dorm.sharif.ir/api/auth/login";
    String url_dorm_profile = "https://dorm.sharif.ir/api/profile";
     CheckBox checkBox ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui();
        CreatingLogin();
    }

    private void ui() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessions = new AppSessionManager(getApplicationContext());
        getSupportActionBar().hide();
        setContentView(R.layout.login_dorm);
        checkBox = (CheckBox) findViewById(R.id.dorm_login_checkbox);

        if (sessions.getcheckboxstate_dorm()) {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }
        TextView login_title = (TextView)findViewById(R.id.login_title_dorm);
        TextView Txt_User_dorm = (TextView)findViewById(R.id.Txt_User_dorm);
        TextView btn_login_dorm = (TextView)findViewById(R.id.btn_login_dorm);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
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

    public void dormLogin(final String username, final String pass) {
        if (!username.equals("84106161")) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url_dorm_login, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("status").matches("success")) {
                                    sessions.createDormLoginSession(username, response.getInt("ttl"),
                                            response.getString("access_token"), response.getString("refresh_token"), "");
                                    getPersonalData();
                                    if(sessions.getcheckboxstate_dorm()){
                                        sessions.saveuserdorm(username);
                                        sessions.savepassdorm(pass);
                                    }
                                    if (!checkBox.isChecked()) {
                                        sessions.saveuserdorm("");
                                        sessions.savepassdorm("");
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
                            .makeText(getApplicationContext(), "مشکل در اتصال به سرور", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                            .setMargin(0.015f,0f);
                }
            })


                {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> _params = new HashMap<String, String>();

                    _params.put("Content-type", "application/json");

                    return _params;

                }

                    @Override
                    public byte[] getBody() {
                    try {
                        JSONObject _jsonRequest = new JSONObject();
                        _jsonRequest.put("username", username);
                        _jsonRequest.put("password", pass);
                        return _jsonRequest.toString().getBytes(getParamsEncoding());
                    } catch (Exception uee) {
                        return null;
                    }
                }

            };

// Adding request to request queue
            MySingleton.getInstance().addToRequestQueue(jsonObjReq);
        }


    }

    private void getPersonalData() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_dorm_profile, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println( response + "======");
                        try {
                            if (response.getString("status").matches("error")) {
                                Toast.makeText(LoginDorm.this,
                                        "Something is Wrong! Try again.", Toast.LENGTH_SHORT).show();
                            } else {

                                sessions.insertDormProfileData(response.getString("dormitory"), response.getString("field"),
                                        response.getString("first_name"), response.getString("last_name"),
                                        response.getString("room"), response.getString("mobile_number"),
                                        response.getString("student_number"), response.getString("section"));
                                sessions.insertcoroom(response.getJSONArray("corooms").toString());
                                TastyToast
                                        .makeText(getApplicationContext(), "شما وارد شدید!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                                        .setMargin(0.015f,0f);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent myIntent = new Intent(LoginDorm.this, DormActivity.class);
                                        startActivity(myIntent);
                                    }
                                },1000);
                            }
                        } catch (Exception e) {
                            try {
                                sessions.insertDormProfileData(response.getString("dormitory"), response.getString("field"),
                                        response.getString("first_name"), response.getString("last_name"),
                                        response.getString("room"), response.getString("mobile_number"),
                                        response.getString("student_number"), response.getString("section"));
                                sessions.insertcoroom(response.getJSONArray("corooms").toString());
                                System.out.println(response.getJSONArray("corooms").toString() + "^^^^^^^^");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            TastyToast
                                    .makeText(getApplicationContext(), "شما وارد شدید!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                                    .setMargin(0.015f,0f);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent myIntent = new Intent(LoginDorm.this, DormActivity.class);
                                    startActivity(myIntent);
                                }
                            },1000);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TastyToast
                        .makeText(getApplicationContext(), "مشکل در اتصال به سرور", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        .setMargin(0.015f,0f);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> _params = new HashMap<String, String>();

                _params.put("Content-type", "application/json");
                _params.put("X-Token", sessions.getUserDetails().get("access_token"));

                return _params;

            }
        };

// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void CreatingLogin() {

        Button btnLogin = (Button)findViewById(R.id.btn_login_dorm);
        HashMap<String, String> temp = sessions.getUserDetails();
        final EditText txtUsername = (EditText) findViewById(R.id.Txt_User_dorm);
        final EditText txtPassword = (EditText) findViewById(R.id.Txt_Pass_dorm);

        if(sessions.getuserdorm()!="" || sessions.getpassdorm()!=""){
            txtUsername.setText(sessions.getuserdorm());
            txtPassword.setText(sessions.getpassdorm());
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

                        dormLogin(convertNum(txtUsername.getText().toString()), txtPassword.getText().toString());
                    }
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        Intent BackToMain = new Intent(LoginDorm.this, SplashScreen.class);
        startActivity(BackToMain);
        finish();
    }


    public void EmptyLoginBox() {
        final EditText txtUsername = (EditText) findViewById(R.id.Txt_User_dorm);
        final EditText txtPassword = (EditText) findViewById(R.id.Txt_Pass_dorm);

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

    public void DormLoginCheckBox(View view) {

        if (checkBox.isChecked()) {
            sessions.setcheckboxstate_dorm(true);
        }
        if (!checkBox.isChecked()){
            sessions.setcheckboxstate_dorm(false);
        }
    }
}