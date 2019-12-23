package fathi.shakhes;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fathi.shakhes.FoodMessageTab.TabMessage;
import shakhes.R;


public class ContactUsActivity extends AppCompatActivity {
    String url = "https://dining.sharif.ir/api/contactus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
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
        myTextView2a.setText("ارتباط با ما");
        myTextView2a.setTypeface(typeFace);
    }

    public void submit(View v) {
        if (((EditText) findViewById(R.id.mobile)).getText().toString().length() != 11) {
            ((EditText) findViewById(R.id.mobile)).setError("شماره موبایل را به درستی وارد کنید.");
            ((EditText) findViewById(R.id.mobile)).requestFocus();
            return;
        }
        if (!((EditText) findViewById(R.id.email)).getText().toString().contains("@")) {
            ((EditText) findViewById(R.id.email)).setError("ایمیل را به درستی وارد کنید.");
            ((EditText) findViewById(R.id.email)).requestFocus();
            return;
        }
        if (((EditText) findViewById(R.id.body)).getText().length() < 5) {
            ((EditText) findViewById(R.id.body)).setError("متن را به درستی وارد کنید.");
            ((EditText) findViewById(R.id.body)).requestFocus();
            return;
        }

        String tmp = ((EditText) findViewById(R.id.body)).getText().toString().replaceAll("\n", "-");
        tmp = tmp.replaceAll(" ", "%20");
        url += "?phone=" +((EditText) findViewById(R.id.mobile)).getText().toString() + "&email="
                + ((EditText) findViewById(R.id.email)).getText().toString() + "&body="
                + tmp +"\n" +"From Shakhes App";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("success").equals("true")) {
                                TastyToast
                                        .makeText(getApplicationContext(), "پیام شما ارسال شد", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                                        .setMargin(0.015f,0f);

                                ((EditText) findViewById(R.id.mobile)).setText("");
                                ((EditText) findViewById(R.id.email)).setText("");
                                ((EditText) findViewById(R.id.body)).setText("");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            TastyToast
                                    .makeText(getApplicationContext(), "مشکلی درارسال پیام پیش آمده است", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                                    .setMargin(0.015f,0f);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TastyToast
                        .makeText(getApplicationContext(), "مشکلی درارسال پیام پیش آمده است", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        .setMargin(0.015f,0f);
                ((EditText) findViewById(R.id.mobile)).setText("");
                ((EditText) findViewById(R.id.email)).setText("");
                ((EditText) findViewById(R.id.body)).setText("");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> _params = new HashMap();

                _params.put("Content-type", "application/json");

                return _params;

            }
        };
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void  onBackPressed() {
        finish();
        Intent myIntent = new Intent(ContactUsActivity.this, FoodActivity.class);
        startActivity(myIntent);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent myIntent = new Intent(ContactUsActivity.this, FoodActivity.class);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
