package fathi.shakhes;

import android.graphics.Typeface;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shakhes.R;

public class TasisatActivity extends AppCompatActivity {

    Typeface typeFace;
    CheckBox[] checkBoxes;
    String[] keys , names , nums;
    AppSessionManager s;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasisat);
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
        myTextView2a.setText("خدمات تاسیساتی");
        myTextView2a.setTypeface(typeFace);

        Spinner spinner = (Spinner) findViewById(R.id.namayande);
        ArrayList<String> contacts = new ArrayList<>();

        s = new AppSessionManager(getApplicationContext());

        ((TextView) findViewById(R.id.message)).setText(Html.fromHtml(s.getText1()));
        try {
            JSONArray tmp = new JSONArray(s.getcoroom());
            names = new String[tmp.length()];
            nums = new String[tmp.length()];
            for (int i = 0; i < tmp.length(); i++) {
                names[i] = tmp.getJSONObject(i).getString("first_name") + " " + tmp.getJSONObject(i).getString("last_name");
                nums[i] = tmp.getJSONObject(i).getString("mobile_number");
            }
        } catch (Exception e) {
            e.printStackTrace();
            names = new String[1];
            nums = new String[1];
            names[0] = "کاربر تست";
            nums[0] = "96102030";
            return;
        }
        for (int i = 0; i < names.length; i++) {
            contacts.add(names[i]);
        }
        ((EditText) findViewById(R.id.n_phone)).setText(nums[0]);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, contacts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((EditText) findViewById(R.id.n_phone)).setText(nums[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checkBoxes = new CheckBox[50];
        keys = new String[50];

        checkBoxes[0] = (CheckBox) findViewById(R.id.chbox_1);
        checkBoxes[1] = (CheckBox) findViewById(R.id.chbox_2);
        checkBoxes[2] = (CheckBox) findViewById(R.id.chbox_3);
        checkBoxes[3] = (CheckBox) findViewById(R.id.chbox_4);
        checkBoxes[4] = (CheckBox) findViewById(R.id.chbox_5);
        checkBoxes[5] = (CheckBox) findViewById(R.id.chbox_b1);
        checkBoxes[6] = (CheckBox) findViewById(R.id.chbox_b2);
        checkBoxes[7] = (CheckBox) findViewById(R.id.chbox_b3);
        checkBoxes[8] = (CheckBox) findViewById(R.id.chbox_b4);
        checkBoxes[9] = (CheckBox) findViewById(R.id.chbox_b5);
        checkBoxes[10] = (CheckBox) findViewById(R.id.chbox_b6);
        checkBoxes[11] = (CheckBox) findViewById(R.id.chbox_c1);
        checkBoxes[12] = (CheckBox) findViewById(R.id.chbox_c2);
        checkBoxes[13] = (CheckBox) findViewById(R.id.chbox_c3);
        checkBoxes[14] = (CheckBox) findViewById(R.id.chbox_e1);
        checkBoxes[15] = (CheckBox) findViewById(R.id.chbox_e2);
        checkBoxes[16] = (CheckBox) findViewById(R.id.chbox_e3);
        checkBoxes[17] = (CheckBox) findViewById(R.id.chbox_e4);
        checkBoxes[18] = (CheckBox) findViewById(R.id.chbox_e5);
        checkBoxes[19] = (CheckBox) findViewById(R.id.chbox_e6);
        checkBoxes[20] = (CheckBox) findViewById(R.id.chbox_f1);
        checkBoxes[21] = (CheckBox) findViewById(R.id.chbox_f2);
        checkBoxes[22] = (CheckBox) findViewById(R.id.chbox_f3);
        checkBoxes[23] = (CheckBox) findViewById(R.id.chbox_f4);
        checkBoxes[24] = (CheckBox) findViewById(R.id.chbox_g1);
        checkBoxes[25] = (CheckBox) findViewById(R.id.chbox_g2);
        checkBoxes[26] = (CheckBox) findViewById(R.id.chbox_g3);
        checkBoxes[27] = (CheckBox) findViewById(R.id.chbox_h1);
        checkBoxes[28] = (CheckBox) findViewById(R.id.chbox_h2);
        checkBoxes[29] = (CheckBox) findViewById(R.id.chbox_h3);
        checkBoxes[30] = (CheckBox) findViewById(R.id.chbox_h4);
        checkBoxes[31] = (CheckBox) findViewById(R.id.chbox_h5);
        checkBoxes[32] = (CheckBox) findViewById(R.id.chbox_h6);
        checkBoxes[33] = (CheckBox) findViewById(R.id.chbox_h7);
        checkBoxes[34] = (CheckBox) findViewById(R.id.chbox_h8);
        checkBoxes[35] = (CheckBox) findViewById(R.id.chbox_h9);
        checkBoxes[36] = (CheckBox) findViewById(R.id.chbox_h10);
        checkBoxes[37] = (CheckBox) findViewById(R.id.chbox_h11);
        checkBoxes[38] = (CheckBox) findViewById(R.id.chbox_h12);
        checkBoxes[39] = (CheckBox) findViewById(R.id.chbox_d1);
        checkBoxes[40] = (CheckBox) findViewById(R.id.chbox_d2);
        checkBoxes[41] = (CheckBox) findViewById(R.id.chbox_d3);
        checkBoxes[42] = (CheckBox) findViewById(R.id.other_1);
        checkBoxes[43] = (CheckBox) findViewById(R.id.other_2);
        checkBoxes[44] = (CheckBox) findViewById(R.id.other_3);
        checkBoxes[45] = (CheckBox) findViewById(R.id.other_4);
        checkBoxes[46] = (CheckBox) findViewById(R.id.other_5);
        checkBoxes[47] = (CheckBox) findViewById(R.id.other_6);
        checkBoxes[48] = (CheckBox) findViewById(R.id.other_7);
        checkBoxes[49] = (CheckBox) findViewById(R.id.other_8);

        keys[0] = "m1_1";
        keys[1] = "m1_2";
        keys[2] = "m1_3";
        keys[3] = "m1_4";
        keys[4] = "m1_5";
        keys[42] = "m1_6";
        keys[5] = "m2_1";
        keys[6] = "m2_2";
        keys[7] = "m2_3";
        keys[8] = "m2_4";
        keys[9] = "m2_5";
        keys[10] = "m2_6";
        keys[43] = "m2_7";
        keys[11] = "m3_1";
        keys[12] = "m3_2";
        keys[13] = "m3_3";
        keys[44] = "m3_4";
        keys[39] = "m4_1";
        keys[40] = "m4_2";
        keys[41] = "m4_3";
        keys[45] = "m4_4";
        keys[14] = "m5_1";
        keys[15] = "m5_2";
        keys[16] = "m5_3";
        keys[17] = "m5_4";
        keys[18] = "m5_5";
        keys[19] = "m5_6";
        keys[46] = "m5_7";
        keys[20] = "m6_1";
        keys[21] = "m6_2";
        keys[22] = "m6_3";
        keys[23] = "m6_4";
        keys[47] = "m6_5";
        keys[24] = "m7_1";
        keys[25] = "m7_2";
        keys[26] = "m7_3";
        keys[48] = "m7_4";
        keys[27] = "m8_1";
        keys[28] = "m8_2";
        keys[29] = "m8_3";
        keys[30] = "m8_4";
        keys[31] = "m8_5";
        keys[32] = "m8_6";
        keys[33] = "m8_7";
        keys[34] = "m8_8";
        keys[35] = "m8_10";
        keys[36] = "m8_11";
        keys[37] = "m8_12";
        keys[38] = "m8_13";
        keys[49] = "m8_14";
    }

    public void submit(View v) {
        boolean check = false;
        data = new ArrayList<>();
        final String dscr = ((EditText) findViewById(R.id.desc)).getText().toString();
        final String n_name = names[((Spinner) findViewById(R.id.namayande)).getSelectedItemPosition()];
        final String n_num = nums[((Spinner) findViewById(R.id.namayande)).getSelectedItemPosition()];
        final String n_phone = ((EditText) findViewById(R.id.n_phone)).getText().toString();
        if (n_phone.length() < 2) {
            ((EditText) findViewById(R.id.n_phone)).setError("شماره تلفن را وارد کنید");
            ((EditText) findViewById(R.id.n_phone)).requestFocus();
            return;
        }
        if (dscr.length() < 2) {
            ((EditText) findViewById(R.id.desc)).setError("شرح را وارد کنید");
            ((EditText) findViewById(R.id.desc)).requestFocus();
            return;
        }
        for (int i = 0; i < 50; i++) {
            if (checkBoxes[i].isChecked()) {
                check = true;
                data.add(keys[i]);
            }
        }
        if (check) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    "https://dorm.sharif.ir/api/reparation/new", null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                if (response.getString("status").matches("error")) {
                                    Toast.makeText(TasisatActivity.this,
                                            response.getString("message"), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(TasisatActivity.this,
                                            "گزارش مشکل با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(TasisatActivity.this,
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
                    _params.put("X-Token", s.getUserDetails().get("access_token"));

                    return _params;
                }

                @Override
                public byte[] getBody() {
                    try {
                        JSONObject _params = new JSONObject();
                        _params.put("description", dscr);
                        _params.put("student_1_name", s.getDormProfileData()[2] + " " + s.getDormProfileData()[3]);
                        _params.put("student_1_phone", s.getDormProfileData()[5]);
                        _params.put("student_2", n_num);
                        _params.put("student_2_phone", n_phone);
                        for (int i = 0; i < data.size(); i++) {
                            _params.put(data.get(i), "on");
                        }
                        if (checkBoxes[42].isChecked() && ((EditText) findViewById(R.id.edit_1)).getText() != null) {
                            _params.put("m1_6", "true");
                            _params.put("m1_7", ((EditText) findViewById(R.id.edit_1)).getText().toString());
                        }
                        if (checkBoxes[43].isChecked() && ((EditText) findViewById(R.id.edit_2)).getText() != null) {
                            _params.put("m2_7", "true");
                            _params.put("m2_8", ((EditText) findViewById(R.id.edit_2)).getText().toString());
                        }
                        if (checkBoxes[44].isChecked() && ((EditText) findViewById(R.id.edit_3)).getText() != null) {
                            _params.put("m3_4", "true");
                            _params.put("m3_5", ((EditText) findViewById(R.id.edit_3)).getText().toString());
                        }
                        if (checkBoxes[45].isChecked() && ((EditText) findViewById(R.id.edit_4)).getText() != null) {
                            _params.put("m4_4", "true");
                            _params.put("m4_5", ((EditText) findViewById(R.id.edit_4)).getText().toString());
                        }
                        if (checkBoxes[46].isChecked() && ((EditText) findViewById(R.id.edit_5)).getText() != null) {
                            _params.put("m5_7", "true");
                            _params.put("m5_8", ((EditText) findViewById(R.id.edit_5)).getText().toString());
                        }
                        if (checkBoxes[47].isChecked() && ((EditText) findViewById(R.id.edit_6)).getText() != null) {
                            _params.put("m6_5", "true");
                            _params.put("m6_6", ((EditText) findViewById(R.id.edit_6)).getText().toString());
                        }
                        if (checkBoxes[48].isChecked() && ((EditText) findViewById(R.id.edit_7)).getText() != null) {
                            _params.put("m7_4", "true");
                            _params.put("m7_5", ((EditText) findViewById(R.id.edit_7)).getText().toString());
                        }
                        if (checkBoxes[49].isChecked() && ((EditText) findViewById(R.id.edit_8)).getText() != null) {
                            _params.put("m8_14", "true");
                            _params.put("m8_15", ((EditText) findViewById(R.id.edit_8)).getText().toString());
                        }
                        return _params.toString().getBytes(getParamsEncoding());
                    } catch (Exception uee) {
                        return null;
                    }
                }
            };

// Adding request to request queue
            MySingleton.getInstance().addToRequestQueue(jsonObjReq);
        } else {
            Toast.makeText(TasisatActivity.this,
                    "یکی از گزینه‌ها را انتخاب کنید.", Toast.LENGTH_LONG).show();
        }
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

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.other_1:
                if (checked)
                    ((EditText) findViewById(R.id.edit_1)).setVisibility(View.VISIBLE);
                else
                    ((EditText) findViewById(R.id.edit_1)).setVisibility(View.GONE);
                break;
            case R.id.other_2:
                if (checked)
                    ((EditText) findViewById(R.id.edit_2)).setVisibility(View.VISIBLE);
                else
                    ((EditText) findViewById(R.id.edit_2)).setVisibility(View.GONE);
                break;
            case R.id.other_3:
                if (checked)
                    ((EditText) findViewById(R.id.edit_3)).setVisibility(View.VISIBLE);
                else
                    ((EditText) findViewById(R.id.edit_3)).setVisibility(View.GONE);
                break;
            case R.id.other_4:
                if (checked)
                    ((EditText) findViewById(R.id.edit_4)).setVisibility(View.VISIBLE);
                else
                    ((EditText) findViewById(R.id.edit_4)).setVisibility(View.GONE);
                break;
            case R.id.other_5:
                if (checked)
                    ((EditText) findViewById(R.id.edit_5)).setVisibility(View.VISIBLE);
                else
                    ((EditText) findViewById(R.id.edit_5)).setVisibility(View.GONE);
                break;
            case R.id.other_6:
                if (checked)
                    ((EditText) findViewById(R.id.edit_6)).setVisibility(View.VISIBLE);
                else
                    ((EditText) findViewById(R.id.edit_6)).setVisibility(View.GONE);
                break;
            case R.id.other_7:
                if (checked)
                    ((EditText) findViewById(R.id.edit_7)).setVisibility(View.VISIBLE);
                else
                    ((EditText) findViewById(R.id.edit_7)).setVisibility(View.GONE);
                break;
            case R.id.other_8:
                if (checked)
                    ((EditText) findViewById(R.id.edit_8)).setVisibility(View.VISIBLE);
                else
                    ((EditText) findViewById(R.id.edit_8)).setVisibility(View.GONE);
                break;
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
