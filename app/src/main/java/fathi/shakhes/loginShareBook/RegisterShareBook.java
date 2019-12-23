package fathi.shakhes.loginShareBook;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import fathi.shakhes.Internet;
import fathi.shakhes.MySingleton;
import shakhes.R;


public class RegisterShareBook extends AppCompatActivity {
    private EditText name_register,
            lastname_register, username_register, phone_number_register,
            mail_register, password_register,
            password_repeat_register,national_identifier_register,
            student_identifier,
            bank_account_number,
            shabaa_identifier;
    Typeface typeFace ;
    private Button btnRegister;
    AppSessionManager sessions ;
    private String url_sharebook_Sign_up= "https://sharebook.sharif.ir/users.json?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessions = new AppSessionManager(getApplicationContext());
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.register_sharebook);
        initUI();
    }

    private void initUI() {
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        name_register = findViewById(R.id.name_register);
        lastname_register = findViewById(R.id.last_name_register);
        username_register = findViewById(R.id.user_name_register);
        phone_number_register = findViewById(R.id.phone_number_register);
        mail_register = findViewById(R.id.mail_register);
        password_register = findViewById(R.id.password_register);
        password_repeat_register = findViewById(R.id.password_repeat_register);
        btnRegister = (Button) findViewById(R.id.btn_register_sharebook);
        national_identifier_register = findViewById(R.id.national_identifier_register);
        student_identifier  = findViewById(R.id.student_identifier_register);
        bank_account_number =findViewById(R.id.bank_account_number_register);
        shabaa_identifier = findViewById(R.id.shabaa_identifier_register);

        name_register.setTypeface(typeFace);
        lastname_register.setTypeface(typeFace);
        username_register.setTypeface(typeFace);
        phone_number_register.setTypeface(typeFace);
        mail_register.setTypeface(typeFace);
        password_register.setTypeface(typeFace);
        password_repeat_register.setTypeface(typeFace);
        national_identifier_register.setTypeface(typeFace);
        student_identifier.setTypeface(typeFace);
        bank_account_number.setTypeface(typeFace);
        shabaa_identifier.setTypeface(typeFace);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp(name_register.getText().toString(),lastname_register.getText().toString()
                        ,username_register.getText().toString(),phone_number_register.getText().toString(),
                        mail_register.getText().toString(),password_register.getText().toString(),
                        password_repeat_register.getText().toString(),
                        national_identifier_register.getText().toString(),
                        student_identifier.getText().toString(),bank_account_number.getText().toString(),
                        shabaa_identifier.getText().toString()
                        );
            }
        });

    }

    public void SignUp(final String name_register,
                       final String lastname_register, final String username_register,
                       final String phone_number_register,
                       final String mail_register,final String password_register,
                       final String password_repeat_register,final String national_identifier_register,
                       final String student_identifier,
                       final String bank_account_number,
                       final String shabaa_identifier) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_sharebook_Sign_up
                        +
                        "user[first_name]=" + name_register +
                        "&user[last_name]=" + lastname_register +
                        "&user[cell_phone_number]=" + phone_number_register +
                        "&user[national_identifier]=" + national_identifier_register +
                        "&user[email]=" + mail_register +
                        "&user[username]=" + username_register +
                        "&user[password]=" + password_register +
                        "&user[password_confirmation]=" + password_repeat_register +
                        "&user[student_identifier]=" + student_identifier +
                        "&user[bank_account_number]=" + bank_account_number +
                        "&user[shabaa_identifier]=" + shabaa_identifier, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(RegisterShareBook.this, response.toString(), Toast.LENGTH_SHORT).show();

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
    public Boolean EmptyNot(EditText editText) {
        if(!(editText.getId() == R.id.mail_register)){
            if (editText.getText().toString().equals("")) {
                editText.setError(getString(R.string.essential_filed));
                return false;
            }
        }
        switch (editText.getId()) {
            case R.id.phone_number_register:
                if (phone_number_register.getText().length() !=11){
                    phone_number_register.setError(getString(R.string.phone_example));
                    return false ;
                }
                break ;
            case R.id.mail_register:
                if ( !mail_register.getText().toString().equals("")){
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail_register.getText().toString()).matches()) {
                        mail_register.setError(getString(R.string.wrong_mail));
                        return false ;
                    }
                }
                break ;
            case R.id.password_register:
                if (password_register.getText().length() <8 ) {
                    password_register.setError(getString(R.string.minimum_length));
                    return false ;
                }
                break ;
        }
        return true;
    }
    public void  onBackPressed() {
        Intent myIntent = new Intent(RegisterShareBook.this, LoginShareBook.class);
        startActivity(myIntent);
        finish();
    }

}