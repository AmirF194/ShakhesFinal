package fathi.shakhes.loginShareBook;

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
import android.widget.Toast;

import fathi.shakhes.Internet;
import fathi.shakhes.LoginFood;
import fathi.shakhes.MySingleton;
import fathi.shakhes.SplashScreen;
import shakhes.R;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fathi.shakhes.AppSessionManager;


public class LoginShareBook extends AppCompatActivity {
    private TextView tvRegister;
    private TextView tvRestore;
    CheckBox checkBox ;
    private Button btnLogin;
    AppSessionManager sessions ;
    private String url_sharebook_login= "https://sharebook.sharif.ir/users/sign_in.json?";
    Typeface typeFace ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessions = new AppSessionManager(getApplicationContext());
//        if(!sessions.getsharebooklogininfo().equals("-")){
//            startActivity(new Intent(LoginShareBook.this,OpenLogSharebook.class));
//            finish();
//        }
        setContentView(R.layout.login_sharebook);
        ui();
        CreatingLogin();
    }
    private void ui() {
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessions = new AppSessionManager(getApplicationContext());
        getSupportActionBar().hide();
        setContentView(R.layout.login_sharebook);
        checkBox = (CheckBox) findViewById(R.id.checkboxlogin_sharebook);
        checkBox.setTypeface(typeFace);

        if (sessions.getcheckboxstate_sharebook()) {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }
        TextView login_title_sharebook = (TextView)findViewById(R.id.login_title_sharebook);
        TextView Txt_User_sharebook = (TextView)findViewById(R.id.username_login_sharebook);
        TextView Txt_Pass_sharebook = (TextView)findViewById(R.id.password_login_sharebook);
        TextView Txt_CheckBox_sharebook = (TextView)findViewById(R.id.checkboxlogin_sharebook);
        TextView Txt_Login_sharebook = (TextView)findViewById(R.id.btn_login_sharebook);
        TextView Txt_Restore_sharebook = (TextView)findViewById(R.id.Restorepass_sharebook);
        TextView Txt_Register_sharebook = (TextView)findViewById(R.id.Register_sharebook);
        btnLogin = findViewById(R.id.btn_login_sharebook);

        login_title_sharebook.setTypeface(typeFace);
        Txt_User_sharebook.setTypeface(typeFace);
        Txt_Pass_sharebook.setTypeface(typeFace);
        Txt_CheckBox_sharebook.setTypeface(typeFace);
        Txt_Login_sharebook.setTypeface(typeFace);
        Txt_Restore_sharebook.setTypeface(typeFace);
        Txt_Register_sharebook.setTypeface(typeFace);
        btnLogin.setTypeface(typeFace);

    }
    public void EmptyLoginBox() {
        final EditText txtUsername = (EditText) findViewById(R.id.username_login_sharebook);
        final EditText txtPassword = (EditText) findViewById(R.id.password_login_sharebook);
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
    public void LoginCheckBox(View view) {
        if (checkBox.isChecked()) {
            sessions.setcheckboxstate_sharebook(true);
        }
        if (!checkBox.isChecked()){
            sessions.setcheckboxstate_sharebook(false);
        }
    }
    private void CreatingLogin() {
        Button btnLogin = (Button) findViewById(R.id.btn_login_sharebook);
        final EditText txtUsername = (EditText) findViewById(R.id.username_login_sharebook);
        final EditText txtPassword = (EditText) findViewById(R.id.password_login_sharebook);
        tvRegister = (TextView) findViewById(R.id.Register_sharebook);
        tvRestore = (TextView) findViewById(R.id.Restorepass_sharebook);

        if(sessions.getusersharebook()!="" || sessions.getpasssharebook()!=""){
            txtUsername.setText(sessions.getusersharebook());
            txtPassword.setText(sessions.getpasssharebook());
        }

        txtUsername.setSelection(txtUsername.getText().length());
        txtPassword.setSelection(txtPassword.getText().length());
        // Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Internet.IsConnectionAvailable(getApplicationContext())){
                    TastyToast
                            .makeText(getApplicationContext(), "به اینترنت دسترسی ندارید!", Toast.LENGTH_SHORT, TastyToast.ERROR)
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

                        Login((txtUsername.getText().toString()), txtPassword.getText().toString());

                    }
                }

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginShareBook.this, RegisterShareBook.class));
                finish();
            }
        });

        tvRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginShareBook.this, RestorePasswordShareBook.class));
                finish();
            }
        });

    }
    public void Login(final String username, final String password) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_sharebook_login+"user[username]="
                        +username + "&user[password]="+password, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            sessions.savesharebooklogininfo(response.toString());
                        if (sessions.getcheckboxstate_sharebook()) {
                            sessions.saveusersharebook(username);
                            sessions.savepasssharebook(password);
                                                                      }
                         if (!checkBox.isChecked()) {
                              sessions.saveusersharebook("");
                              sessions.savepasssharebook("");
            }

            startActivity(new Intent(LoginShareBook.this, OpenLogSharebook.class));
            finish();
                        } catch (Exception e) {
                            TastyToast
                                    .makeText(getApplicationContext(), "نام کاربری یا گذرواژه اشتباه است !", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                                    .setMargin(0.015f, 0f);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TastyToast
                        .makeText(getApplicationContext(), "نام کاربری یا گذرواژه اشتباه است !", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        .setMargin(0.015f, 0f);
            }
        });

        {
// Adding request to request queue
            MySingleton.getInstance().addToRequestQueue(jsonObjReq);
        }
    }
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        Intent BackToMain = new Intent(LoginShareBook.this, SplashScreen.class);
        startActivity(BackToMain);
        finish();
    }

}