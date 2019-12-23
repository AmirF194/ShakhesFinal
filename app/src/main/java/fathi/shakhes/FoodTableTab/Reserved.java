package fathi.shakhes.FoodTableTab;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CompoundButtonCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import fathi.shakhes.AppSessionManager;
import fathi.shakhes.JalaliCalendar;
import fathi.shakhes.MySingleton;
import fathi.shakhes.PersianDateConverter;
import shakhes.R;

public class Reserved extends Fragment  {
    String url_food_place = "https://dining.sharif.ir/api/food-places?access_token=";
    String url_food_table = "https://dining.sharif.ir/api/reserve-table?access_token=";
    Typeface typeFace;
    String url_food_table_reserve = "https://dining.sharif.ir/api/reserve-status-text?access_token=";
    View rootView;
    AppSessionManager sessions;
    String token;
    TextView waiting , food_plan ;
    TextView[] days, food_name, meal_name, titles;
    LinearLayout table_reserved ;

    public static Reserved newInstance() {
        return new Reserved();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sessions = new AppSessionManager(getActivity().getApplicationContext());
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSans.ttf");
        rootView = inflater.inflate(R.layout.food_table_child_reserved,
                container, false);

        waiting = rootView.findViewById(R.id.waiting_reserve_table);
        waiting.setTypeface(typeFace);
        waiting.setTextSize(25);

        food_plan = rootView.findViewById(R.id.food_plan);
        food_plan.setTypeface(typeFace);
        food_plan.setTextSize(15);

        table_reserved = rootView.findViewById(R.id.table_reserved) ;
        titles = new TextView[3];
        titles[0] =rootView.findViewById(R.id.food_name_title);
        titles[1] =rootView.findViewById(R.id.mill_name_title);
        titles[2] = rootView.findViewById(R.id.day_name_title);

        days = new TextView[7];
        days[0] = (TextView) rootView.findViewById(R.id.day_1);
        days[1] = (TextView) rootView.findViewById(R.id.day_2);
        days[2] = (TextView) rootView.findViewById(R.id.day_3);
        days[3] = (TextView) rootView.findViewById(R.id.day_4);
        days[4] = (TextView) rootView.findViewById(R.id.day_5);
        days[5] = (TextView) rootView.findViewById(R.id.day_6);
        days[6] = (TextView) rootView.findViewById(R.id.day_7);


        food_name = new TextView[7];
        food_name[0] = (TextView) rootView.findViewById(R.id.food_1);
        food_name[1] = (TextView) rootView.findViewById(R.id.food_2);
        food_name[2] = (TextView) rootView.findViewById(R.id.food_3);
        food_name[3] = (TextView) rootView.findViewById(R.id.food_4);
        food_name[4] = (TextView) rootView.findViewById(R.id.food_5);
        food_name[5] = (TextView) rootView.findViewById(R.id.food_6);
        food_name[6] = (TextView) rootView.findViewById(R.id.food_7);


        meal_name = new TextView[7];
        meal_name[0] = (TextView) rootView.findViewById(R.id.mill_1);
        meal_name[1] = (TextView) rootView.findViewById(R.id.mill_2);
        meal_name[2] = (TextView) rootView.findViewById(R.id.mill_3);
        meal_name[3] = (TextView) rootView.findViewById(R.id.mill_4);
        meal_name[4] = (TextView) rootView.findViewById(R.id.mill_5);
        meal_name[5] = (TextView) rootView.findViewById(R.id.mill_6);
        meal_name[6] = (TextView) rootView.findViewById(R.id.mill_7);

        for (int i = 0; i < titles.length; i++) {
            titles[i].setTextSize(13);
            titles[i].setTypeface(typeFace);
        }

        for (int i = 0; i < 7; i++) {
            days[i].setTypeface(typeFace);
            food_name[i].setTypeface(typeFace);
            meal_name[i].setTypeface(typeFace);
            days[i].setTextSize(11);
            food_name[i].setTextSize(11);
            meal_name[i].setTextSize(11);
        }

        token = getActivity().getIntent().getStringExtra("token");

        new MyTask().execute("12");

        return rootView;

    }

    public void TableReserve( final String meal_id ,final String date , final int k) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_food_table_reserve+token+
                        "&date="+date + "&food_meal_id=" + meal_id
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (meal_id.equalsIgnoreCase("1")){
                        meal_name[k].setText(Html.fromHtml( response.getString("message")).toString().trim());

                    }
                    if(meal_id.equalsIgnoreCase("2")){
                        food_name[k].setText(Html.fromHtml( response.getString("message")).toString().trim());
                    }

                    if (!food_name[6].getText().toString().equalsIgnoreCase("")){
                       waiting.setVisibility(View.GONE);
                        table_reserved.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(getActivity(),  Html.fromHtml( response.getString("message")).toString().trim()    , Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );

        jsonObjReq.setShouldCache(false);
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);

    }

    public void getTable() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_food_place + token, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //ToDo check returning date
                            Iterator iterator = response.keys();
                            getData((String) iterator.next());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        jsonObjReq.setShouldCache(false);

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);

    }

    public void getData(final String key) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url_food_table + token + "&place_id=" + key + "&start_date=" + formattedDate, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i <response.length() ; i++) {
                            try {
                                days[i].setText(
                                response.getJSONObject(i).getString("day_name")
                                        +"\n" +
                                        response.getJSONObject(i).getString("day_date")
                                );
                                String[] parts = response.getJSONObject(i).getString("day_date").split("/");
                                PersianDateConverter cc = new PersianDateConverter() ;
                                cc.setIranianDate(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
                                cc.getGregorianDate();

                                String inputdate = cc.getGregorianDate().replace("/","-") ;
                                TableReserve("1" ,  inputdate , i );
                                TableReserve("2",inputdate, i);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }
        };

        jsonObjReq.setShouldCache(false);
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
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

            // Do something that takes a long time, for example:
            getTable();

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
            // dialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }

    private void CreateCheckBox(int number, int nlx) {
        LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) table_reserved.getLayoutParams();
        lay.gravity = Gravity.CENTER;
        for (int i = 0; i < number; i++) {
            CheckBox cb = new CheckBox(getActivity().getApplicationContext());
            table_reserved.addView(cb);
            cb.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            cb.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            cb.setPadding(0, 2, 0, 2);

            if (Build.VERSION.SDK_INT < 21) {
                CompoundButtonCompat.setButtonTintList(cb, ColorStateList.valueOf(getResources().getColor(R.color.food)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
            } else {
                cb.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.food)));//setButtonTintList is accessible directly on API>19
            }

            cb.setLayoutParams(lay);
        }
    }

}
