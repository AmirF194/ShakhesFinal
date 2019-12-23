package fathi.shakhes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.TimeZone;

import shakhes.R;


public class BehdashtListActivity extends AppCompatActivity {

    TextView[] titles;
    TextView[] abstracs;
    TextView[] times;
    LinearLayout[] layouts;
    Typeface typeFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behdasht_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.drawable.shariflogo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        View v = LayoutInflater
                .from(getSupportActionBar().getThemedContext())
                .inflate(R.layout.action_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(v, params);
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        TextView myTextView2a = (TextView) findViewById(R.id.text2);
        myTextView2a.setText("اداره بهداشت و درمان");
        myTextView2a.setTypeface(typeFace);

        String data = getIntent().getExtras().getString("data");

        String[][] data_behdasht = new String[10][4];
        JalaliCalendar calendar;
        try {
            JSONArray data_tmp = new JSONArray(data);
            data_behdasht = new String[data_tmp.length()][4];
            JSONObject tmp;
            for (int i = 0; i < data_tmp.length(); i++) {
                tmp = data_tmp.getJSONObject(i);
                data_behdasht[i][0] = tmp.getString("id");
              //  getDate(tmp.getLong("time")*1000)
                data_behdasht[i][1] = String.valueOf(tmp.getLong("time")*1000);
                data_behdasht[i][2] = tmp.getString("title");
                data_behdasht[i][3] = tmp.getString("abstract");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        layouts = new LinearLayout[10];
        layouts[0] = (LinearLayout) findViewById(R.id.inner_1);
        layouts[1] = (LinearLayout) findViewById(R.id.inner_2);
        layouts[2] = (LinearLayout) findViewById(R.id.inner_3);
        layouts[3] = (LinearLayout) findViewById(R.id.inner_4);
        layouts[4] = (LinearLayout) findViewById(R.id.inner_5);
        layouts[5] = (LinearLayout) findViewById(R.id.inner_6);
        layouts[6] = (LinearLayout) findViewById(R.id.inner_7);
        layouts[7] = (LinearLayout) findViewById(R.id.inner_8);
        layouts[8] = (LinearLayout) findViewById(R.id.inner_9);
        layouts[9] = (LinearLayout) findViewById(R.id.inner_10);

        //------------------------------------------------------------------
        titles = new TextView[10];
        titles[0] = (TextView) findViewById(R.id.inner_1_title);
        titles[1] = (TextView) findViewById(R.id.inner_2_title);
        titles[2] = (TextView) findViewById(R.id.inner_3_title);
        titles[3] = (TextView) findViewById(R.id.inner_4_title);
        titles[4] = (TextView) findViewById(R.id.inner_5_title);
        titles[5] = (TextView) findViewById(R.id.inner_6_title);
        titles[6] = (TextView) findViewById(R.id.inner_7_title);
        titles[7] = (TextView) findViewById(R.id.inner_8_title);
        titles[8] = (TextView) findViewById(R.id.inner_9_title);
        titles[9] = (TextView) findViewById(R.id.inner_10_title);

        abstracs = new TextView[10];
        abstracs[0] = (TextView) findViewById(R.id.inner_1_abs);
        abstracs[1] = (TextView) findViewById(R.id.inner_2_abs);
        abstracs[2] = (TextView) findViewById(R.id.inner_3_abs);
        abstracs[3] = (TextView) findViewById(R.id.inner_4_abs);
        abstracs[4] = (TextView) findViewById(R.id.inner_5_abs);
        abstracs[5] = (TextView) findViewById(R.id.inner_6_abs);
        abstracs[6] = (TextView) findViewById(R.id.inner_7_abs);
        abstracs[7] = (TextView) findViewById(R.id.inner_8_abs);
        abstracs[8] = (TextView) findViewById(R.id.inner_9_abs);
        abstracs[9] = (TextView) findViewById(R.id.inner_10_abs);

        times = new TextView[10];
        times[0] = (TextView) findViewById(R.id.inner_1_date);
        times[1] = (TextView) findViewById(R.id.inner_2_date);
        times[2] = (TextView) findViewById(R.id.inner_3_date);
        times[3] = (TextView) findViewById(R.id.inner_4_date);
        times[4] = (TextView) findViewById(R.id.inner_5_date);
        times[5] = (TextView) findViewById(R.id.inner_6_date);
        times[6] = (TextView) findViewById(R.id.inner_7_date);
        times[7] = (TextView) findViewById(R.id.inner_8_date);
        times[8] = (TextView) findViewById(R.id.inner_9_date);
        times[9] = (TextView) findViewById(R.id.inner_10_date);

        final String[][] finalData_behdasht = data_behdasht;
        for (int i = 0; i < data_behdasht.length; i++) {
            final int finalI = i;
            layouts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(BehdashtListActivity.this, SingleNewsActivity.class);
                    myIntent.putExtra("id", finalData_behdasht[finalI][0]);
                    myIntent.putExtra("time", finalData_behdasht[finalI][1]);
                    myIntent.putExtra("title", finalData_behdasht[finalI][2]);
                    myIntent.putExtra("abstract", finalData_behdasht[finalI][3]);
                   // myIntent.putExtra("body", finalData_behdasht[finalI][4]);
                    myIntent.putExtra("type", 4);
                    startActivity(myIntent);
                }
            });

            titles[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(BehdashtListActivity.this, SingleNewsActivity.class);
                    myIntent.putExtra("id", finalData_behdasht[finalI][0]);
                    myIntent.putExtra("time", finalData_behdasht[finalI][1]);
                    myIntent.putExtra("title", finalData_behdasht[finalI][2]);
                    myIntent.putExtra("abstract", finalData_behdasht[finalI][3]);
                    // myIntent.putExtra("body", finalData_behdasht[finalI][4]);
                    myIntent.putExtra("type", 4);
                    startActivity(myIntent);
                }
            });
            abstracs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(BehdashtListActivity.this, SingleNewsActivity.class);
                    myIntent.putExtra("id", finalData_behdasht[finalI][0]);
                    myIntent.putExtra("time", finalData_behdasht[finalI][1]);
                    myIntent.putExtra("title", finalData_behdasht[finalI][2]);
                    myIntent.putExtra("abstract", finalData_behdasht[finalI][3]);
                    // myIntent.putExtra("body", finalData_behdasht[finalI][4]);
                    myIntent.putExtra("type", 4);
                    startActivity(myIntent);
                }
            });
        }
        for (int i = data_behdasht.length; i < 10; i++) {
            layouts[i].setVisibility(View.GONE);
        }

        for (int i = 0; i < data_behdasht.length; i++) {
            titles[i].setTypeface(typeFace);
            abstracs[i].setTypeface(typeFace);
            titles[i].setText(convertNums(data_behdasht[i][2]));
            abstracs[i].setText(convertNums(data_behdasht[i][3]));
            calendar = new JalaliCalendar(getDateTimeFromTimestamp(Long.parseLong(data_behdasht[i][1])));
            data_behdasht[i][1] = convertNums(calendar.date + " " + calendar.strMonth + " " + calendar.year);
            times[i].setText(data_behdasht[i][1]);
        }

        TextView myTextView2 = (TextView) findViewById(R.id.nothing);
        myTextView2.setTypeface(typeFace);
        TxtPresentation();
        DoRefreshPage();
    }
    private void TxtPresentation(){
        for (int i = 0; i <titles.length ; i++) {
            titles[i].setTypeface(typeFace);
            abstracs[i].setTypeface(typeFace);
            times[i].setTypeface(typeFace);

            titles[i].setTextSize(18);
            abstracs[i].setTextSize(14);
            times[i].setTextSize(16);


        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
