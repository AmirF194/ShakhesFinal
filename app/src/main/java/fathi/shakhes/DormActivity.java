package fathi.shakhes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import shakhes.R;

public class DormActivity extends AppCompatActivity {

    TextView[] texts;
    String url_dorm_picture = "https://dorm.sharif.ir/api/picture";
    String url_dorm_text1 = "https://dorm.sharif.ir/api/description/get?id=2";
    String url_dorm_text2 = "https://dorm.sharif.ir/api/description/get?id=3";
    AppSessionManager s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dorm);
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
        myTextView2a.setText("سامانه خوابگاه ها");
        myTextView2a.setTypeface(typeFace);

        texts = new TextView[8];
        texts[0] = (TextView) findViewById(R.id.text_1);
        texts[1] = (TextView) findViewById(R.id.text_2);
        texts[2] = (TextView) findViewById(R.id.text_3);
        texts[3] = (TextView) findViewById(R.id.text_4);
        texts[4] = (TextView) findViewById(R.id.text_5);
        texts[5] = (TextView) findViewById(R.id.text_6);
        texts[6] = (TextView) findViewById(R.id.text_7);
        texts[7] = (TextView) findViewById(R.id.text_8);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        for (int i = 0; i < 8; i++) {
            texts[i].setTypeface(typeFace);
        }

        s = new AppSessionManager(this);
        String[] data = s.getDormProfileData();
        for (int i = 0; i < 8; i++) {
            texts[i].setText(convertNums(data[i]));
        }

        getPicture();
        getTexts();
        getTexts2();
    }

    public void getPicture() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_dorm_picture, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("status").equals("success")) {
                                byte[] decodedString = Base64.decode(response.getString("picture"), Base64.DEFAULT);
                                Bitmap decodedBytePicture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                ImageView image = (ImageView) findViewById(R.id.dorm_image);
                                image.setImageBitmap(Bitmap.createScaledBitmap(decodedBytePicture,decodedBytePicture.getWidth(),decodedBytePicture.getHeight()-86,false));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> _params = new HashMap<String, String>();

                _params.put("Content-type", "application/json");
                _params.put("X-Token", s.getUserDetails().get("access_token"));

                return _params;

            }
        };

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void getTexts() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url_dorm_text1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("this is content_main information  : " +response + "======");
                        try {
                            if (response.getString("status").equals("success"))
                                s.saveText1(response.getString("text"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> _params = new HashMap<>();

                _params.put("Content-type", "application/json");

                return _params;

            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject _jsonRequest = new JSONObject();
                    _jsonRequest.put("id", "2");
                    return _jsonRequest.toString().getBytes(getParamsEncoding());
                } catch (Exception uee) {
                    return null;
                }
            }
        };

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void getTexts2() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url_dorm_text2, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response + "======");
                        try {
                            if (response.getString("status").equals("success"))
                                s.saveText2(response.getString("text"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
        };

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void guestClick(View v) {
        Intent myIntent = new Intent(DormActivity.this, GuestActivity.class);
        startActivity(myIntent);
    }

    //ToDo Debug this
    public void tasisatClick(View v) {
        Intent myIntent = new Intent(DormActivity.this, TasisatActivity.class);
        startActivity(myIntent);
    }

    private String convertNums(String input) {
        String ret = input;
        ret = ret.replaceAll("0", "۰");
        ret = ret.replaceAll("1", "۱");
        ret = ret.replaceAll("2", "۲");
        ret = ret.replaceAll("3", "۳");
        ret = ret.replaceAll("4", "۴");
        ret = ret.replaceAll("5", "۵");
        ret = ret.replaceAll("6", "۶");
        ret = ret.replaceAll("7", "۷");
        ret = ret.replaceAll("8", "۸");
        ret = ret.replaceAll("9", "۹");
        return ret;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DormActivity.this, MainActivity.class ));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(DormActivity.this, MainActivity.class ));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
