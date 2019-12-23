package fathi.shakhes;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import shakhes.R ;
public class DaySaleFood extends AppCompatActivity {


    Button button;
    Typeface typeFace;
    String url_food_place   = "https://dining.sharif.ir/api/food-places?access_token=";
    String url_food_table   = "https://dining.sharif.ir/api/reserve-table?access_token=";
    String url_food_status  = "https://dining.sharif.ir/api/reserve-status?access_token=";
    String url_food_reserve = "https://dining.sharif.ir/api/daysale-buy?access_token=";
    JSONArray tmp, meal_foods;
    JSONObject tempobject;
    String meal_tmp = "";
    String food_name_tmp,food_id_tmp;
    String token;
    JSONArray foods = null;
    JSONObject food_status ;
    TextView daysale_food ;
    String[] ids,names,food_data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        token = getIntent().getStringExtra("token");
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_daysale);
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
        myTextView2a.setText("روز خرید");
        myTextView2a.setTypeface(typeFace);
        daysale_food = findViewById(R.id.daysale_food);
        daysale_food.setTypeface(typeFace);
        button = findViewById(R.id.daysale_submit);
        button.setTypeface(typeFace);

        new MyTask().execute("soss");

        DoRefreshPage();
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
        getTable();
            // Do something that takes a long time, for example:


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


            // Do things like hide the progress bar or change a TextView
        }
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
                                switch (ids[i]) {
                                    case "35" :
                                        getData("35");
                                        break ;

                                    case "31" :
                                        getData("31");
                                        break ;
                                }
                                i++;
                            }

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })  {
            @Override
            public Priority getPriority() {
                return Priority.LOW ;
            }
        };
        jsonObjReq.setShouldCache(false);

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void getData(final String place_id) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_YEAR,-5);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = df.format(c.getTime());
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url_food_table + token + "&place_id=" + place_id + "&start_date=" + formattedDate, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            getid(response.toString(),place_id);
                        } catch (JSONException e) {
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

                return _params;

            }
        };

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getid(String data_food , final String place_id) throws JSONException {
                tmp = new JSONArray(data_food);
        for (int j = 0; j < 7; j++) {
            tempobject = tmp.getJSONObject(j);

            if (tempobject.getString("meal_foods").equalsIgnoreCase("هیچ غذایی برای این روز تعریف نشده است."))
            {

            } else {
                meal_foods = new JSONArray(tempobject.getString("meal_foods"));
                for (int i = 0; i < meal_foods.length(); i++) {
                    meal_tmp = meal_foods.getJSONObject(i).getString("meal") + "\n" + meal_tmp;
                    foods = new JSONArray(meal_foods.getJSONObject(i).getString("foods"));
                    for (int k = 0; k < foods.length(); k++) {
                        food_id_tmp = foods.getJSONObject(k).getString("id");
                        food_name_tmp = foods.getJSONObject(k).getString("name");
                        CheckDaySale(food_id_tmp,food_name_tmp,place_id);
                    }
                }
                meal_tmp = "";
                food_name_tmp = "";
                food_id_tmp = "";
            }
        }
    }

    private void CheckDaySale(final String diet_id , final String food_name,final String place_id) {
       JsonObjectRequest check = new JsonObjectRequest(Request.Method.POST,
               url_food_status + token + "&diet_id=" + diet_id+"&place_id=" +place_id, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               try {
                   food_status = new JSONObject(response.toString());
                   if (food_status.getBoolean("daybuy") == true) {
                       daysale_food.setText(food_name);
                       button.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               DaySaleSubmit(diet_id,place_id);

                           }
                       });


                   }


               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(DaySaleFood.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
           }
       }

       );

        MySingleton.getInstance().addToRequestQueue(check);
    }

    private void DaySaleSubmit(final String diet_id ,final String place_id) {

        JsonObjectRequest check = new JsonObjectRequest(Request.Method.POST,
                url_food_reserve + token + "&diet_id="+diet_id +"&place_id=" +place_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TastyToast
                        .makeText(getApplicationContext(), response.toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                        .setMargin(0.015f,0f);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaySaleFood.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();


            }
        }

        );

        MySingleton.getInstance().addToRequestQueue(check);
    }

    private void DoRefreshPage() {
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.daysale_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new
                                                        SwipeRefreshLayout.OnRefreshListener() {
                                                            @Override
                                                            public void onRefresh() {
                                                                startActivity(getIntent());
                                                                finish();
                                                            }
                                                        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DaySaleFood.this,FoodActivity.class ));
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(DaySaleFood.this, FoodActivity.class ));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
