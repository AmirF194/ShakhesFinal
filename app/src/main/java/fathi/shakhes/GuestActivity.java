package fathi.shakhes;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import shakhes.R;


public class GuestActivity extends AppCompatActivity {

    Typeface typeFace;
    AppSessionManager sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geust);
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
        myTextView2a.setText("اقامت میهمان");
        myTextView2a.setTypeface(typeFace);

        sessions = new AppSessionManager(getApplicationContext());

        ((TextView) findViewById(R.id.message)).setText(Html.fromHtml(sessions.getText2()));

        Spinner spinner = (Spinner) findViewById(R.id.relation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_mehman_1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    ((EditText) findViewById(R.id.relation_other)).setVisibility(View.VISIBLE);
                } else {
                    ((EditText) findViewById(R.id.relation_other)).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((TimePicker) findViewById(R.id.in_time)).setIs24HourView(true);
        ((TimePicker) findViewById(R.id.out_time)).setIs24HourView(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void submit(View v) {
        String fname = ((EditText) findViewById(R.id.fname)).getText().toString();
        String lname = ((EditText) findViewById(R.id.lname)).getText().toString();
        String ncode = ((EditText) findViewById(R.id.mcode)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String stuNum = ((EditText) findViewById(R.id.stuNum)).getText().toString();
        int relation = ((Spinner) findViewById(R.id.relation)).getSelectedItemPosition();
        String relation_other = ((EditText) findViewById(R.id.relation_other)).getText().toString();
        PersianDatePicker in_date = (PersianDatePicker) findViewById(R.id.in_date);
        TimePicker in_time = (TimePicker) findViewById(R.id.in_time);
        PersianDatePicker out_date = (PersianDatePicker) findViewById(R.id.out_date);
        TimePicker out_time = (TimePicker) findViewById(R.id.out_time);
        CheckBox terms = (CheckBox) findViewById(R.id.terms);
        if (fname.length() < 1) {
            ((EditText) findViewById(R.id.fname)).setError("نام را وارد کنید.");
            ((EditText) findViewById(R.id.fname)).requestFocus();
            return;
        }
        if (lname.length() < 1) {
            ((EditText) findViewById(R.id.lname)).setError("نام خانوادگی را وارد کنید.");
            ((EditText) findViewById(R.id.lname)).requestFocus();
            return;
        }
        if (ncode.length() < 1) {
            ((EditText) findViewById(R.id.mcode)).setError("کد ملی را وارد کنید.");
            ((EditText) findViewById(R.id.mcode)).requestFocus();
            return;
        }
        if (phone.length() < 1) {
            ((EditText) findViewById(R.id.phone)).setError("شماره تلفن را وارد کنید.");
            ((EditText) findViewById(R.id.phone)).requestFocus();
            return;
        }
        if (relation == 0) {
            relation_other = "پدر";
        } else if (relation == 1) {
            relation_other = "برادر";
        } else if (relation_other.length() < 1) {
            ((EditText) findViewById(R.id.relation_other)).setError("نسبت را وارد کنید.");
            ((EditText) findViewById(R.id.relation_other)).requestFocus();
            return;
        }
        String indate = in_date.getDisplayPersianDate().getPersianYear() + "-" + in_date.getDisplayPersianDate().getPersianMonth() + "-" + in_date.getDisplayPersianDate().getPersianDay();
        String intime;
        if (Build.VERSION.SDK_INT >= 23)
            intime = in_time.getHour() + ":" + in_time.getMinute();
        else
            intime = in_time.getCurrentHour() + ":" + in_time.getCurrentMinute();
        String outdate = out_date.getDisplayPersianDate().getPersianYear() + "-" + out_date.getDisplayPersianDate().getPersianMonth() + "-" + out_date.getDisplayPersianDate().getPersianDay();
        String outtime;
        if (Build.VERSION.SDK_INT >= 23)
            outtime = out_time.getHour() + ":" + out_time.getMinute();
        else
            outtime = out_time.getCurrentHour() + ":" + out_time.getCurrentMinute();
        if (!terms.isChecked()) {
            terms.setError("شرایط را قبول کنید.");
            terms.requestFocus();
        }
        submit(fname, lname, ncode, phone, relation_other, indate, intime, outdate, outtime, stuNum);
    }

    private void submit(final String fname, final String lname, final String ncode, final String phone, final String relation_other,
                        final String indate, final String intime, final String outdate, final String outtime, final String stuNum) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://dorm.sharif.ir/api/guest/new", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").matches("error")) {
                                if (response.getJSONObject("message").has("entry_time")) {
                                    Toast.makeText(GuestActivity.this,
                                            response.getJSONObject("message").getString("entry_time"),
                                            Toast.LENGTH_LONG).show();
                                } else if (response.getJSONObject("message").has("exit_time")) {
                                    Toast.makeText(GuestActivity.this,
                                            response.getJSONObject("message").getString("exit_time"),
                                            Toast.LENGTH_LONG).show();
                                } else if (response.getJSONObject("message").has("national_code")) {
                                    Toast.makeText(GuestActivity.this,
                                            response.getJSONObject("message").getString("national_code"),
                                            Toast.LENGTH_LONG).show();
                                    (findViewById(R.id.mcode)).requestFocus();
                                } else if (response.getJSONObject("message").has("phone_number")) {
                                    Toast.makeText(GuestActivity.this,
                                            response.getJSONObject("message").getString("phone_number"),
                                            Toast.LENGTH_LONG).show();
                                    (findViewById(R.id.n_phone)).requestFocus();
                                } else {
                                    Toast.makeText(GuestActivity.this,
                                            response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(GuestActivity.this,
                                        "درخواست مهمان با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (Exception e) {
                            Toast.makeText(GuestActivity.this,
                                    "خطایی رخ داده است.", Toast.LENGTH_LONG).show();
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
                _params.put("X-Token", sessions.getUserDetails().get("access_token"));

                return _params;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject _params = new JSONObject();
                    _params.put("first_name", fname);
                    _params.put("last_name", lname);
                    _params.put("national_code", ncode);
                    _params.put("phone_number", phone);
                    _params.put("relation", relation_other);
                    _params.put("entry_date", indate);
                    System.out.println(indate + "----" + outdate);
                    _params.put("entry_time", intime);
                    _params.put("exit_date", outdate);
                    _params.put("exit_time", outtime);
                    _params.put("accept_terms", "true");
                    if (stuNum != null && stuNum.length() > 1)
                        _params.put("student_id", stuNum);
                    return _params.toString().getBytes(getParamsEncoding());
                } catch (Exception uee) {
                    return null;
                }
            }
        };

// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void overrideFonts(final View v) {
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeFace);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
