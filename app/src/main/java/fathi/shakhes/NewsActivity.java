package fathi.shakhes;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//import io.realm.Realm;
//import io.realm.RealmAsyncTask;
import shakhes.R;

public class NewsActivity extends AppCompatActivity

{

    // Tag used to cancel the request
    String tag_json_obj = "json_obj_req";
    String url_news_dorm = "https://dorm.sharif.ir/api/announcement/";
    String url_news_refah = "https://sws.sharif.ir/api/announcement/";
    String url_news_dining = "http://dining.sharif.ir/api/news";
    String url_modir_news = "http://213.233.161.98/api/index.php/modirNews";
    String url_news_behdasht = "https://med.sharif.ir/announcements.json" ;
    String[][] data_dorm,data_refah,data_dining,data_modir,data_behdasht;
    String dorm_data,refah_data,dining_data,modir_data,behdasht_data;
    LinearLayout dorm_inner,refah_inner,dining_inner,modir_inner,behdasht_inner;;
    TextView[] nothing;
    TextView [][] title,body,time,cont;
    TextView [] newsnames ;
    Typeface typeFace ;
    JalaliCalendar calendar;
    int cols  = 5 ;
    int row = 3 ;
    TextView ab;
    AppSessionManager s ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        s = new AppSessionManager(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

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

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        ab = (TextView) findViewById(R.id.text2);
        ab.setText("اخبار و رویدادها");
        ab.setTypeface(typeFace);

        overrideFonts(getApplicationContext(), (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0));

        DefineIds();
        Listeners();
        TxtPresentation();
        DoRefreshPage();

        if(!Internet.IsConnectionAvailable(getApplicationContext())) {

            welfareparsedata();
            otherparsedata();
            healthparsedata();
            foodparsedata();
            dormparsedata();
            TastyToast
                    .makeText(getApplicationContext(), "به اینترنت دسترسی ندارید!", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                    .setMargin(0.015f,0f);
        } else {
            new MyTask().execute("Get All News");
        }

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//myRealm.close();
//    }

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
            dormRequest();
            welfareRequest();
            diningRequest();
            OtherRequest();
            HealthRequest();

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

    private void TxtPresentation(){
        for (int i = 0; i <cols; i++) {
            for (int j = 0; j <row ; j++) {
                title[i][j].setTypeface(typeFace);
                body[i][j].setTypeface(typeFace);
                time[i][j].setTypeface(typeFace);
                cont[i][j].setTypeface(typeFace);

                title[i][j].setTextSize(15);
                body[i][j].setTextSize(13);
                time[i][j].setTextSize(10);
                cont[i][j].setTextSize(10);
            }
        }

        for (int i = 0; i <nothing.length ; i++) {
            nothing[i].setTypeface(typeFace);
        }
    }

    private void DoRefreshPage() {
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
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

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView ) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/nazli.ttf"));
            }
        } catch (Exception e) {
        }
    }

    private Date getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        Date currenTimeZone = (Date) cal.getTime();
        return currenTimeZone;
    }

    public Date getDateTimeFromTimestamp(Long value) {
        TimeZone timeZone = TimeZone.getDefault();
        long offset = timeZone.getOffset(value);
        if (offset < 0) {
            value -= offset;
        } else {
            value += offset;
        }
        return new Date(value);
    }

    public void Listeners(){
        //Create Dorm Listener
        LinearLayout dorm_1 = (LinearLayout) findViewById(R.id.dorm_inner_1);
        View dorm_1_title = findViewById(R.id.dorm_inner_1_title);
        View dorm_1_abs = findViewById(R.id.dorm_inner_1_abs);
        dorm_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[0][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });
        dorm_1_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[0][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });
        dorm_1_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[0][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });

        LinearLayout dorm_2 = (LinearLayout) findViewById(R.id.dorm_inner_2);
        View dorm_2_title = findViewById(R.id.dorm_inner_2_title);
        View dorm_2_abs = findViewById(R.id.dorm_inner_2_abs);
        dorm_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[1][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });
        dorm_2_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[1][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });
        dorm_2_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[1][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });

        LinearLayout dorm_3 = (LinearLayout) findViewById(R.id.dorm_inner_3);
        View dorm_3_title = findViewById(R.id.dorm_inner_3_title);
        View dorm_3_abs = findViewById(R.id.dorm_inner_3_abs);
        dorm_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[2][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });
        dorm_3_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[2][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });
        dorm_3_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dorm[2][0]);
                myIntent.putExtra("type", 1);
                startActivity(myIntent);
            }
        });


        //Create Welfare Listener
        LinearLayout welfare_1 = (LinearLayout) findViewById(R.id.refah_inner_1);
        View welfare_1_title = findViewById(R.id.refah_inner_1_title);
        View welfare_1_abs = findViewById(R.id.refah_inner_1_abs);
        welfare_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[0][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });
        welfare_1_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[0][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });
        welfare_1_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[0][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });

        LinearLayout welfare_2 = (LinearLayout) findViewById(R.id.refah_inner_2);
        View welfare_2_title = findViewById(R.id.refah_inner_2_title);
        View welfare_2_abs = findViewById(R.id.refah_inner_2_abs);
        welfare_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[1][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });
        welfare_2_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[1][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });
        welfare_2_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[1][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });

        LinearLayout welfare_3 = (LinearLayout) findViewById(R.id.refah_inner_3);
        View welfare_3_title = findViewById(R.id.refah_inner_3_title);
        View welfare_3_abs = findViewById(R.id.refah_inner_3_abs);
        welfare_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[2][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });
        welfare_3_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[2][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });
        welfare_3_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_refah[2][0]);
                myIntent.putExtra("type", 2);
                startActivity(myIntent);
            }
        });



        //Create Dining Listener
        LinearLayout dining_1 = (LinearLayout) findViewById(R.id.dining_inner_1);
        View dining_1_title = findViewById(R.id.dining_inner_1_title);
        View dining_1_abs = findViewById(R.id.dining_inner_1_abs);
        dining_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[0][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });
        dining_1_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[0][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });
        dining_1_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[0][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });

        LinearLayout dining_2 = (LinearLayout) findViewById(R.id.dining_inner_2);
        View dining_2_title = findViewById(R.id.dining_inner_2_title);
        View dining_2_abs = findViewById(R.id.dining_inner_2_abs);
        dining_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[1][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });
        dining_2_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[1][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });
        dining_2_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[1][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });

        LinearLayout dining_3 = (LinearLayout) findViewById(R.id.dining_inner_3);
        View dining_3_title = findViewById(R.id.dining_inner_3_title);
        View dining_3_abs = findViewById(R.id.dining_inner_3_abs);
        dining_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[2][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });
        dining_3_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[2][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });
        dining_3_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_dining[2][0]);
                myIntent.putExtra("type", 3);
                startActivity(myIntent);
            }
        });


        //Create Health Listener
        LinearLayout Health_1 = (LinearLayout) findViewById(R.id.behdasht_inner_1);
        View Health_1_title = findViewById(R.id.behdasht_inner_1_title);
        View Health_1_abs = findViewById(R.id.behdasht_inner_1_abs);
        Health_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[0][0]);
                myIntent.putExtra("time", data_behdasht[0][1]);
                myIntent.putExtra("title", data_behdasht[0][2]);
                myIntent.putExtra("abstract", data_behdasht[0][3]);

                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });
        Health_1_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[0][0]);
                myIntent.putExtra("time", data_behdasht[0][1]);
                myIntent.putExtra("title", data_behdasht[0][2]);
                myIntent.putExtra("abstract", data_behdasht[0][3]);

                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });
        Health_1_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[0][0]);
                myIntent.putExtra("time", data_behdasht[0][1]);
                myIntent.putExtra("title", data_behdasht[0][2]);
                myIntent.putExtra("abstract", data_behdasht[0][3]);

                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });

        LinearLayout Health_2 = (LinearLayout) findViewById(R.id.behdasht_inner_2);
        View Health_2_title = findViewById(R.id.behdasht_inner_2_title);
        View Health_2_abs = findViewById(R.id.behdasht_inner_2_abs);
        Health_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[1][0]);
                myIntent.putExtra("time", data_behdasht[1][1]);
                myIntent.putExtra("title", data_behdasht[1][2]);
                myIntent.putExtra("abstract", data_behdasht[1][3]);
                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });
        Health_2_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[1][0]);
                myIntent.putExtra("time", data_behdasht[1][1]);
                myIntent.putExtra("title", data_behdasht[1][2]);
                myIntent.putExtra("abstract", data_behdasht[1][3]);
                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });
        Health_2_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[1][0]);
                myIntent.putExtra("time", data_behdasht[1][1]);
                myIntent.putExtra("title", data_behdasht[1][2]);
                myIntent.putExtra("abstract", data_behdasht[1][3]);
                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });

        LinearLayout Health_3 = (LinearLayout) findViewById(R.id.behdasht_inner_3);
        View Health_3_title = findViewById(R.id.behdasht_inner_3_title);
        View Health_3_abs = findViewById(R.id.behdasht_inner_3_abs);
        Health_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[2][0]);
                myIntent.putExtra("time", data_behdasht[2][1]);
                myIntent.putExtra("title", data_behdasht[2][2]);
                myIntent.putExtra("abstract", data_behdasht[2][3]);
                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });
        Health_3_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[2][0]);
                myIntent.putExtra("time", data_behdasht[2][1]);
                myIntent.putExtra("title", data_behdasht[2][2]);
                myIntent.putExtra("abstract", data_behdasht[2][3]);
                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });
        Health_3_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_behdasht[2][0]);
                myIntent.putExtra("time", data_behdasht[2][1]);
                myIntent.putExtra("title", data_behdasht[2][2]);
                myIntent.putExtra("abstract", data_behdasht[2][3]);
                myIntent.putExtra("type", 4);
                startActivity(myIntent);
            }
        });


        //Create Manager Listener
        LinearLayout Manager_1 = (LinearLayout) findViewById(R.id.modir_inner_1);
        View Manager_1_title = findViewById(R.id.modir_inner_1_title);
        View Manager_1_abs = findViewById(R.id.modir_inner_1_abs);
        Manager_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[0][0]);
                myIntent.putExtra("time", data_modir[0][1]);
                myIntent.putExtra("title", data_modir[0][2]);
                myIntent.putExtra("abstract", data_modir[0][3]);
                myIntent.putExtra("body", data_modir[0][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });
        Manager_1_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[0][0]);
                myIntent.putExtra("time", data_modir[0][1]);
                myIntent.putExtra("title", data_modir[0][2]);
                myIntent.putExtra("abstract", data_modir[0][3]);
                myIntent.putExtra("body", data_modir[0][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });
        Manager_1_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[0][0]);
                myIntent.putExtra("time", data_modir[0][1]);
                myIntent.putExtra("title", data_modir[0][2]);
                myIntent.putExtra("abstract", data_modir[0][3]);
                myIntent.putExtra("body", data_modir[0][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });

        LinearLayout Manager_2 = (LinearLayout) findViewById(R.id.modir_inner_2);
        View Manager_2_title = findViewById(R.id.modir_inner_2_title);
        View Manager_2_abs = findViewById(R.id.modir_inner_2_abs);
        Manager_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[1][0]);
                myIntent.putExtra("time", data_modir[1][1]);
                myIntent.putExtra("title", data_modir[1][2]);
                myIntent.putExtra("abstract", data_modir[1][3]);
                myIntent.putExtra("body", data_modir[1][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });
        Manager_2_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[1][0]);
                myIntent.putExtra("time", data_modir[1][1]);
                myIntent.putExtra("title", data_modir[1][2]);
                myIntent.putExtra("abstract", data_modir[1][3]);
                myIntent.putExtra("body", data_modir[1][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });
        Manager_2_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[1][0]);
                myIntent.putExtra("time", data_modir[1][1]);
                myIntent.putExtra("title", data_modir[1][2]);
                myIntent.putExtra("abstract", data_modir[1][3]);
                myIntent.putExtra("body", data_modir[1][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });

        LinearLayout Manager_3 = (LinearLayout) findViewById(R.id.modir_inner_3);
        View Manager_3_title = findViewById(R.id.modir_inner_3_title);
        View Manager_3_abs = findViewById(R.id.modir_inner_3_abs);
        Manager_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[2][0]);
                myIntent.putExtra("time", data_modir[2][1]);
                myIntent.putExtra("title", data_modir[2][2]);
                myIntent.putExtra("abstract", data_modir[2][3]);
                myIntent.putExtra("body", data_modir[2][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });
        Manager_3_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[2][0]);
                myIntent.putExtra("time", data_modir[2][1]);
                myIntent.putExtra("title", data_modir[2][2]);
                myIntent.putExtra("abstract", data_modir[2][3]);
                myIntent.putExtra("body", data_modir[2][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });
        Manager_3_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewsActivity.this, SingleNewsActivity.class);
                myIntent.putExtra("id", data_modir[2][0]);
                myIntent.putExtra("time", data_modir[2][1]);
                myIntent.putExtra("title", data_modir[2][2]);
                myIntent.putExtra("abstract", data_modir[2][3]);
                myIntent.putExtra("body", data_modir[2][4]);
                myIntent.putExtra("type", 5);
                startActivity(myIntent);
            }
        });


        RelativeLayout more1 = (RelativeLayout) findViewById(R.id.news_dorm);
        more1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dorm_data != null && dorm_data.length() > 0) {
                    Intent myIntent = new Intent(NewsActivity.this, DormListActivity.class);
                    myIntent.putExtra("data", dorm_data);
                    startActivity(myIntent);
                }
            }
        });

        RelativeLayout more2 = (RelativeLayout) findViewById(R.id.news_refah);
        more2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refah_data != null && refah_data.length() > 0) {
                    Intent myIntent = new Intent(NewsActivity.this, RefahListActivity.class);
                    myIntent.putExtra("data", refah_data);
                    startActivity(myIntent);
                }
            }
        });

        RelativeLayout more3 = (RelativeLayout) findViewById(R.id.news_food);
        more3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dining_data != null && dining_data.length() > 0) {
                    Intent myIntent = new Intent(NewsActivity.this, FoodListActivity.class);
                    myIntent.putExtra("data", dining_data);
                    startActivity(myIntent);
                }
            }
        });

        RelativeLayout more4 = (RelativeLayout) findViewById(R.id.news_behdasht);
        more4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behdasht_data != null && behdasht_data.length() > 0) {
                    Intent myIntent = new Intent(NewsActivity.this, BehdashtListActivity.class);
                    myIntent.putExtra("data", behdasht_data);
                    startActivity(myIntent);
                }
            }
        });

        RelativeLayout more5 = (RelativeLayout) findViewById(R.id.news_modir);
        more5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modir_data != null && modir_data.length() > 0) {
                    Intent myIntent = new Intent(NewsActivity.this, OtherListActivity.class);
                    myIntent.putExtra("data", modir_data);
                    startActivity(myIntent);
                }
            }
        });
    }

    public void DefineIds(){
        title = new TextView [cols][row];
        body = new TextView [cols][row];
        time = new TextView [cols][row];
        cont = new TextView [cols][row];
        nothing = new TextView [cols];

        nothing[0] = (TextView) findViewById(R.id.nothing1);
        nothing[1] = (TextView ) findViewById(R.id.nothing2);
        nothing[2] = (TextView ) findViewById(R.id.nothing3);
        nothing[3] = (TextView ) findViewById(R.id.nothing4);
        nothing[4] = (TextView ) findViewById(R.id.nothing5);

        dorm_inner = (LinearLayout) findViewById(R.id.dorm_inner_holder);
        refah_inner = (LinearLayout) findViewById(R.id.refah_inner_holder);
        dining_inner = (LinearLayout) findViewById(R.id.dining_inner_holder);
        modir_inner = (LinearLayout) findViewById(R.id.modir_inner_holder);
        behdasht_inner = (LinearLayout) findViewById(R.id.behdasht_inner_holder);

        title[0][0] = (TextView ) findViewById(R.id.dorm_inner_1_title);
        title[0][1] = (TextView ) findViewById(R.id.dorm_inner_2_title);
        title[0][2] = (TextView ) findViewById(R.id.dorm_inner_3_title);
        body[0][0] = (TextView ) findViewById(R.id.dorm_inner_1_abs);
        body[0][1] = (TextView ) findViewById(R.id.dorm_inner_2_abs);
        body[0][2] = (TextView ) findViewById(R.id.dorm_inner_3_abs);
        time[0][0] = (TextView ) findViewById(R.id.dorm_inner_1_date);
        time[0][1] = (TextView ) findViewById(R.id.dorm_inner_2_date);
        time[0][2] = (TextView ) findViewById(R.id.dorm_inner_3_date);
        cont[0][0] = (TextView ) findViewById(R.id.dorm_inner_1_cont);
        cont[0][1] = (TextView ) findViewById(R.id.dorm_inner_2_cont);
        cont[0][2] = (TextView ) findViewById(R.id.dorm_inner_3_cont);

        title[1][0] = (TextView ) findViewById(R.id.refah_inner_1_title);
        title[1][1] = (TextView ) findViewById(R.id.refah_inner_2_title);
        title[1][2] = (TextView ) findViewById(R.id.refah_inner_3_title);
        body[1][0] = (TextView ) findViewById(R.id.refah_inner_1_abs);
        body[1][1] = (TextView ) findViewById(R.id.refah_inner_2_abs);
        body[1][2] = (TextView ) findViewById(R.id.refah_inner_3_abs);
        time[1][0] = (TextView ) findViewById(R.id.refah_inner_1_date);
        time[1][1] = (TextView ) findViewById(R.id.refah_inner_2_date);
        time[1][2] = (TextView ) findViewById(R.id.refah_inner_3_date);
        cont[1][0] = (TextView ) findViewById(R.id.refah_inner_1_cont);
        cont[1][1] = (TextView ) findViewById(R.id.refah_inner_2_cont);
        cont[1][2] = (TextView ) findViewById(R.id.refah_inner_3_cont);

        title[2][0] = (TextView ) findViewById(R.id.dining_inner_1_title);
        title[2][1] = (TextView ) findViewById(R.id.dining_inner_2_title);
        title[2][2] = (TextView ) findViewById(R.id.dining_inner_3_title);
        body[2][0] = (TextView ) findViewById(R.id.dining_inner_1_abs);
        body[2][1] = (TextView ) findViewById(R.id.dining_inner_2_abs);
        body[2][2] = (TextView ) findViewById(R.id.dining_inner_3_abs);
        time[2][0] = (TextView ) findViewById(R.id.dining_inner_1_date);
        time[2][1] = (TextView ) findViewById(R.id.dining_inner_2_date);
        time[2][2] = (TextView ) findViewById(R.id.dining_inner_3_date);
        cont[2][0] = (TextView ) findViewById(R.id.dining_inner_1_cont);
        cont[2][1] = (TextView ) findViewById(R.id.dining_inner_2_cont);
        cont[2][2] = (TextView ) findViewById(R.id.dining_inner_3_cont);

        title[3][0] = (TextView ) findViewById(R.id.behdasht_inner_1_title);
        title[3][1] = (TextView ) findViewById(R.id.behdasht_inner_2_title);
        title[3][2] = (TextView ) findViewById(R.id.behdasht_inner_3_title);
        body[3][0] = (TextView ) findViewById(R.id.behdasht_inner_1_abs);
        body[3][1] = (TextView ) findViewById(R.id.behdasht_inner_2_abs);
        body[3][2] = (TextView ) findViewById(R.id.behdasht_inner_3_abs);
        time[3][0] = (TextView ) findViewById(R.id.behdasht_inner_1_date);
        time[3][1] = (TextView ) findViewById(R.id.behdasht_inner_2_date);
        time[3][2] = (TextView ) findViewById(R.id.behdasht_inner_3_date);
        cont[3][0] = (TextView ) findViewById(R.id.behdasht_inner_1_cont);
        cont[3][1] = (TextView ) findViewById(R.id.behdasht_inner_2_cont);
        cont[3][2] = (TextView ) findViewById(R.id.behdasht_inner_3_cont);

        title[4][0] = (TextView ) findViewById(R.id.modir_inner_1_title);
        title[4][1] = (TextView ) findViewById(R.id.modir_inner_2_title);
        title[4][2] = (TextView ) findViewById(R.id.modir_inner_3_title);
        body[4][0] = (TextView ) findViewById(R.id.modir_inner_1_abs);
        body[4][1] = (TextView ) findViewById(R.id.modir_inner_2_abs);
        body[4][2] = (TextView ) findViewById(R.id.modir_inner_3_abs);
        time[4][0] = (TextView ) findViewById(R.id.modir_inner_1_date);
        time[4][1] = (TextView ) findViewById(R.id.modir_inner_2_date);
        time[4][2] = (TextView ) findViewById(R.id.modir_inner_3_date);
        cont[4][0] = (TextView ) findViewById(R.id.modir_inner_1_cont);
        cont[4][1] = (TextView ) findViewById(R.id.modir_inner_2_cont);
        cont[4][2] = (TextView ) findViewById(R.id.modir_inner_3_cont);

        newsnames = new TextView [5] ;
        newsnames[0] = findViewById(R.id.dormnews_name);
        newsnames[1] = findViewById(R.id.welfarenews_name);
        newsnames[2] = findViewById(R.id.foodnews_name) ;
        newsnames[3] = findViewById(R.id.healthnews_name);
        newsnames[4] = findViewById(R.id.othernews_name) ;

        for (int u = 0; u <newsnames.length ; u++) {
            newsnames[u].setTypeface(typeFace);
            newsnames[u].setTextSize(20);
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        Intent backtoMain = new Intent(NewsActivity.this, SplashScreen.class);
        startActivity(backtoMain);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
               onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void OtherRequest() {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url_modir_news, new Response.Listener<String>() {

            @Override
            public void onResponse(String resp) {
                s.saveothernews(resp);
                otherparsedata();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(strReq);
    }

    public void HealthRequest() {
        JsonArrayRequest req = new JsonArrayRequest(url_news_behdasht,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        s.savehealthnews(response.toString());
                        healthparsedata();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(req);


    }

    public void diningRequest() {
        JsonArrayRequest req = new JsonArrayRequest(url_news_dining,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        s.savefoodnews(response.toString());
                        foodparsedata();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(req);
    }

    public void dormRequest() {
        JsonArrayRequest req = new JsonArrayRequest(url_news_dorm,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        s.savedormnews(response.toString());
                        dormparsedata();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(req);
    }

    public void welfareRequest() {
        JsonArrayRequest req155 = new JsonArrayRequest(url_news_refah,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        s.savewelfarenews(response.toString());
                        welfareparsedata();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(req155);
    }



    public void dormparsedata(){

        if (!s.getdormnews().equals("")){
            try {
                JSONArray dorm_news  = new JSONArray(s.getdormnews());
                try {
                    data_dorm = new String[dorm_news.length()][4];
                    JSONObject tmp;
                    for (int i = 0; i < dorm_news.length(); i++) {
                        tmp = dorm_news.getJSONObject(i);
                        data_dorm[i][0] = String.valueOf(tmp.getInt("id"));
                        calendar = new JalaliCalendar(getDate(tmp.getLong("time")));
                        data_dorm[i][1] = calendar.date + " " + calendar.strMonth + " " + calendar.year;
                        data_dorm[i][2] = tmp.getString("title");
                        data_dorm[i][3] = tmp.getString("abstract");
                    }
                    for (int i = 0; i < 3; i++) {
                        title[0][i].setText(data_dorm[i][2]);
                        body[0][i].setText(data_dorm[i][3]);
                        time[0][i].setText(data_dorm[i][1]);
                    }

                    nothing[0].setVisibility(View.GONE);
                    dorm_inner.setVisibility(View.VISIBLE);

                    dorm_data = dorm_news.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void foodparsedata(){
        if (!s.getfoodnews().equals("")){
            try {
                JSONArray food_news  = new JSONArray(s.getfoodnews());
                try {
                    data_dining = new String[food_news.length()][5];
                    JSONObject tmp;
                    for (int i = 0; i < food_news.length(); i++) {
                        tmp = food_news.getJSONObject(i);
                        data_dining[i][0] = String.valueOf(tmp.getInt("id"));
                        calendar = new JalaliCalendar(getDateTimeFromTimestamp(tmp.getLong("create_date") * 1000));
                        data_dining[i][1] = calendar.date + " " + calendar.strMonth + " " + calendar.year;
                        data_dining[i][2] = tmp.getString("title");
                        data_dining[i][3] = tmp.getString("summary");
                        data_dining[i][4] = String.valueOf(tmp.getLong("create_date"));
                    }
                    Arrays.sort(data_dining, new Comparator<String[]>() {
                        @Override
                        public int compare(final String[] entry1, final String[] entry2) {
                            final String time1 = entry1[4];
                            final String time2 = entry2[4];

                            return time2.compareTo(time1);
                        }
                    });
                    for (int i = 0; i < 3; i++) {
                        title[2][i].setText(data_dining[i][2]);
                        body[2][i].setText(data_dining[i][3]);
                        time[2][i].setText(data_dining[i][1]);
                    }

                    nothing[2].setVisibility(View.GONE);
                    dining_inner.setVisibility(View.VISIBLE);

                    dining_data = food_news.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void healthparsedata(){
        if (!s.gethealthnews().equals("")){
            try {
                JSONArray health_news  = new JSONArray(s.gethealthnews());
                try {
                    data_behdasht = new String[health_news.length()][4];
                    JSONObject tmp;
                    for (int i = 0; i < health_news.length(); i++) {
                        tmp = health_news.getJSONObject(i);
                        data_behdasht[i][0] =tmp.getString("id");
                        calendar = new JalaliCalendar(getDate(tmp.getLong("time")*1000));
                        data_behdasht[i][1] = calendar.date + " " + calendar.strMonth + " " + calendar.year;
                        data_behdasht[i][2] = tmp.getString("title");
                        data_behdasht[i][3] = tmp.getString("abstract");
                    }
                    for (int i = 0; i < 3; i++) {
                        title[3][i].setText(data_behdasht[i][2]);
                        body[3][i].setText(data_behdasht[i][3]);
                        time[3][i].setText(data_behdasht[i][1]);
                    }
                    nothing[3].setVisibility(View.GONE);
                    behdasht_inner.setVisibility(View.VISIBLE);
                    behdasht_data = health_news.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void welfareparsedata(){

        if (!s.getwelfarenews().equals("")) {
            try {
                JSONArray welfare_news  = new JSONArray(s.getwelfarenews());
                data_refah = new String[welfare_news.length()][4];
                JSONObject tmp;
                for (int i = 0; i < welfare_news.length(); i++) {
                    tmp = welfare_news.getJSONObject(i);
                    data_refah[i][0] = String.valueOf(tmp.getInt("id"));
                    calendar = new JalaliCalendar(getDate(tmp.getLong("time")));
                    data_refah[i][1] = calendar.date + " " + calendar.strMonth + " " + calendar.year;
                    data_refah[i][2] = tmp.getString("title");
                    data_refah[i][3] = tmp.getString("abstract");
                }
                for (int i = 0; i < 3; i++) {
                    title[1][i].setText(data_refah[i][2]);
                    body[1][i].setText(data_refah[i][3]);
                    time[1][i].setText(data_refah[i][1]);
                }

                nothing[1].setVisibility(View.GONE);
                refah_inner.setVisibility(View.VISIBLE);

                refah_data = welfare_news.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public void otherparsedata(){
        if (!s.getotherhnews().equals("")){
            try {
                JSONArray response = new JSONArray(s.getotherhnews());
                data_modir = new String[response.length()][5];
                JSONObject tmp;
                for (int i = 0; i < response.length(); i++) {
                    tmp = response.getJSONObject(i);
                    data_modir[i][0] = String.valueOf(tmp.getInt("id"));
                    calendar = new JalaliCalendar(getDateTimeFromTimestamp(tmp.getLong("date") * 1000));
                    data_modir[i][1] = calendar.date + " " + calendar.strMonth + " " + calendar.year;
                    data_modir[i][2] = tmp.getString("title");
                    data_modir[i][3] = tmp.getString("abstract");
                    data_modir[i][4] = tmp.getString("body");
                }
                Arrays.sort(data_modir, new Comparator<String[]>() {
                    @Override
                    public int compare(final String[] entry1, final String[] entry2) {
                        final String time1 = entry1[1];
                        final String time2 = entry2[1];
                        return time2.compareTo(time1);
                    }
                });
                for (int i = 0; i < 3; i++) {
//                        title[4][i].setText(data_modir[i][2]);
//                        body[4][i].setText(data_modir[i][3]);
//                        time[4][i].setText(data_modir[i][1]);
                    title[4][i].setText("");
                    body[4][i].setText("");
                    time[4][i].setText("");
                }

                nothing[4].setVisibility(View.GONE);
                modir_inner.setVisibility(View.VISIBLE);

                modir_data = response.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
