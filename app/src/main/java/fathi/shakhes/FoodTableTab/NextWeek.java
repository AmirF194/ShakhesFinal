package fathi.shakhes.FoodTableTab;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CompoundButtonCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import fathi.shakhes.AppSessionManager;
import fathi.shakhes.MySingleton;
import shakhes.R;

public class NextWeek extends Fragment {
    Typeface typeFace;
    String url_food_place = "https://dining.sharif.ir/api/food-places?access_token=";
    String url_food_table = "https://dining.sharif.ir/api/reserve-table?access_token=";
    String[] ids, names, food_data;
    String url_food_status = "https://dining.sharif.ir/api/reserve-status?access_token=";
    JSONObject food_status;
    JSONArray[] meal_foods;
    JSONArray names_Json_Array;
    JSONArray[][] foods;
    LinearLayout parent;
    View child;
    TextView[] days, food_name, meal_name, titles;
    TextView waiting_nextweek ;
    View rootView;
    AppSessionManager sessions;
    String[] day_name, day_date, meal, food_name_table, food_id_table;
    List<String> categories;
    Spinner spinner;
    LinearLayout[] lx;
    String token;


    public static NextWeek newInstance() {
        return new NextWeek();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sessions = new AppSessionManager(getActivity().getApplicationContext());
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSans.ttf");
        rootView = inflater.inflate(R.layout.activity_food_table_nextweek,
                container, false);

        parent = (LinearLayout) rootView.findViewById(R.id.parent_layout);
        spinner = (Spinner) rootView.findViewById(R.id.spinner_reserve);
        categories = new ArrayList<String>();
        token = getActivity().getIntent().getStringExtra("token");
        waiting_nextweek = rootView.findViewById(R.id.waiting_nextweek_table);
        waiting_nextweek.setTypeface(typeFace);
        waiting_nextweek.setTextSize(25);

        lx = new LinearLayout[7];
        lx[0] = rootView.findViewById(R.id.l1_nextweek);
        lx[1] = rootView.findViewById(R.id.l2_nextweek);
        lx[2] = rootView.findViewById(R.id.l3_nextweek);
        lx[3] = rootView.findViewById(R.id.l4_nextweek);
        lx[4] = rootView.findViewById(R.id.l5_nextweek);
        lx[5] = rootView.findViewById(R.id.l6_nextweek);
        lx[6] = rootView.findViewById(R.id.l7_nextweek);

        new MyTask().execute("abcd");
        return rootView;
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

            spinner.setVisibility(View.VISIBLE);
            // dialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }

    private void CheckStatus(final String diet_id, final String place_id, final int j, final int k, final int m) {
        JsonObjectRequest check = new JsonObjectRequest(Request.Method.POST,
                url_food_status + token + "&diet_id=" + diet_id + "&place_id=" + place_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    food_status = new JSONObject(response.toString());

                    if (food_status.getBoolean("reserve") == true) {
                        food_name_table[j] = foods[j][k].getJSONObject(m).getString("name") + "(قابل رزرو)";
                        food_name_table[j] = food_name_table[j].trim();

                    }

                    if (food_status.getBoolean("reserved") == true) {
                        food_name_table[j] = foods[j][k].getJSONObject(m).getString("name") + "(رزرو)";
                        food_name_table[j] = food_name_table[j].trim();
                    }
                    if (food_status.getBoolean("eaten") == true) {
                        food_name_table[j] = foods[j][k].getJSONObject(m).getString("name") + "(خورده شده)";
                        food_name_table[j] = food_name_table[j].trim();
                    }
                    if (food_status.getBoolean("lavish") == true) {
                        food_name_table[j] = foods[j][k].getJSONObject(m).getString("name") + "(حرام شده)";
                        food_name_table[j] = food_name_table[j].trim();
                    }

                    if (food_status.getBoolean("food_ready") == true) {
                        food_name_table[j] = foods[j][k].getJSONObject(m).getString("name") + "(آماده دریافت)";
                        food_name_table[j] = food_name_table[j].trim();
                    }

                    parent.removeAllViews();
                    child = LayoutInflater.from(getActivity().getApplicationContext()).inflate(
                            R.layout.food_table_child_nextweek, null);

                    titles = new TextView[3];
                    titles[0] = child.findViewById(R.id.food_name_title);
                    titles[1] = child.findViewById(R.id.mill_name_title);
                    titles[2] = child.findViewById(R.id.day_name_title);

                    days = new TextView[7];
                    days[0] = (TextView) child.findViewById(R.id.day_1);
                    days[1] = (TextView) child.findViewById(R.id.day_2);
                    days[2] = (TextView) child.findViewById(R.id.day_3);
                    days[3] = (TextView) child.findViewById(R.id.day_4);
                    days[4] = (TextView) child.findViewById(R.id.day_5);
                    days[5] = (TextView) child.findViewById(R.id.day_6);
                    days[6] = (TextView) child.findViewById(R.id.day_7);


                    food_name = new TextView[7];
                    food_name[0] = (TextView) child.findViewById(R.id.food_1);
                    food_name[1] = (TextView) child.findViewById(R.id.food_2);
                    food_name[2] = (TextView) child.findViewById(R.id.food_3);
                    food_name[3] = (TextView) child.findViewById(R.id.food_4);
                    food_name[4] = (TextView) child.findViewById(R.id.food_5);
                    food_name[5] = (TextView) child.findViewById(R.id.food_6);
                    food_name[6] = (TextView) child.findViewById(R.id.food_7);


                    meal_name = new TextView[7];
                    meal_name[0] = (TextView) child.findViewById(R.id.mill_1);
                    meal_name[1] = (TextView) child.findViewById(R.id.mill_2);
                    meal_name[2] = (TextView) child.findViewById(R.id.mill_3);
                    meal_name[3] = (TextView) child.findViewById(R.id.mill_4);
                    meal_name[4] = (TextView) child.findViewById(R.id.mill_5);
                    meal_name[5] = (TextView) child.findViewById(R.id.mill_6);
                    meal_name[6] = (TextView) child.findViewById(R.id.mill_7);


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
                        days[i].setText(day_name[i] + "\n" + day_date[i]);
                        food_name[i].setText(food_name_table[i]);
                        meal_name[i].setText(meal[i]);

                    }
                    parent.addView(child);
                    parent.setVisibility(View.VISIBLE);
                    waiting_nextweek.setVisibility(View.GONE);

                    //CreateCheckBox(1,1);
//                    mProgressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }

        );

        MySingleton.getInstance().addToRequestQueue(check);
        food_name_table[j] = "";

    }

    private void CreateCheckBox(int number, int nlx) {
        LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) lx[nlx].getLayoutParams();
        lay.gravity = Gravity.CENTER;
        for (int i = 0; i < number; i++) {
            CheckBox cb = new CheckBox(getActivity().getApplicationContext());
            lx[nlx].addView(cb);
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

    private void SpinnerUi() {
        if (ids != null) {
            for (int m = 0; m < ids.length; m++) {
                switch (ids[m]) {
                    case "35":
                        categories.add("مرکزی-سلف دانشجویان خانم");
                        break;
                    case "39":
                        categories.add("خوابگاه-طرشت2(خواهران)");
                        break;
                    case "42":
                        categories.add("خوابگاه-شهید شوریده(خواهران)");
                        break;
                    case "31":
                        categories.add("مرکزی-سلف دانشجویان آقا");
                        break;
                    case "37":
                        categories.add("خوابگاه-طرشت 3");
                        break;
                    case "40":
                        categories.add("خوابگاه-شهید احمدی روشن");
                        break;
                    case "43":
                        categories.add("خوابگاه-شهید وزوایی");
                        break;
                    case "44":
                        categories.add("خوابگاه-شادمان");
                        break;
                    case "45":
                        categories.add("خوابگاه-آزادی");
                        break;
                    case "46":
                        categories.add("خوابگاه-12 واحدی");
                        break;
                    case "50":
                        categories.add("خوابگاه-ولیعصر");
                        break;
                }

            }

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTypeface(typeFace);
                    return view;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTypeface(typeFace);
                    return view;
                }
            };

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner

            spinner.setAdapter(dataAdapter);


            // Do things like hide the progress bar or change a TextView
            // spinner.setVisibility(View.VISIBLE);

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getData(ids[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getTable() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_food_place + token, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //ToDo check returning date
                            ids = new String[response.length()];
                            names = new String[response.length()];
                            food_data = new String[response.length()];
                            int i = 0;
                            Iterator iterator = response.keys();

                            while (iterator.hasNext()) {
                                ids[i] = (String) iterator.next();
                                names[i] = response.getString(ids[i]);
                                // data_food_tmp = "\""+ids[i]+"\"" +":"+ data_temp.get(i)+ ","+ data_food_tmp ;
                                i++;
                            }
                            SpinnerUi();
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
        c.add(Calendar.DATE,+7);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url_food_table + token + "&place_id=" + key + "&start_date=" + formattedDate, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  data_temp.put(num,response.toString());
//                        CreateTable(response.toString(),key);
                        CreateTable(response.toString(), key);
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

        parent.setVisibility(View.GONE);
        waiting_nextweek.setVisibility(View.VISIBLE);

    }

    public void CreateTable(final String DataArray, final String pid) {
        try {
            day_name = new String[7];
            day_date = new String[7];
            meal = new String[7];
            food_name_table = new String[7];
            food_id_table = new String[7];
            meal_foods = new JSONArray[7];
            foods = new JSONArray[7][2];
            names_Json_Array = new JSONArray(DataArray);
            for (int j = 0; j < names_Json_Array.length(); j++) {
                day_name[j] = names_Json_Array.getJSONObject(j).getString("day_name");
                day_date[j] = names_Json_Array.getJSONObject(j).getString("day_date");

                if (!names_Json_Array.getJSONObject(j).getString("meal_foods").equalsIgnoreCase("هیچ غذایی برای این روز تعریف نشده است."))

                {
                    meal_foods[j] = new JSONArray(names_Json_Array.getJSONObject(j).getString("meal_foods"));
                    //******************
                    meal[j] = "";
                    food_name_table[j] = "";
                    food_id_table[j] = "";
                    for (int k = 0; k < meal_foods[j].length(); k++) {
                        meal[j] = meal[j] + "\n" + meal_foods[j].getJSONObject(k).getString("meal");
                        meal[j] = meal[j].trim();
                        foods[j][k] = new JSONArray(meal_foods[j].getJSONObject(k).getString("foods"));


                        for (int m = 0; m < foods[j][k].length(); m++) {
                            food_id_table[j] = foods[j][k].getJSONObject(m).getString("id");
                            //*****************

                            CheckStatus(food_id_table[j], pid, j, k, m);
//                            food_name_table[j] = food_name_table[j] + "\n" + foods[j][k].getJSONObject(m).getString("name");

                        }
                    }
                } else {
                    meal[j] = " - ";
                    food_name_table[j] = "هیچ غذایی وجود ندارد.";

                    //no food define
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
