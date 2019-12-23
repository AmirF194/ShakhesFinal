package fathi.shakhes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import shakhes.R;

public class SingleNewsActivity extends AppCompatActivity {
    Typeface typeFace;
    String id = "",
     url_dorm_profile = "https://dorm.sharif.ir/api/announcement/",
     url_refah_profile = "https://sws.sharif.ir/api/announcement/",
     url_dining_profile = "http://dining.sharif.ir/api/news/",
     url_dorm_webpage = "https://dorm.sharif.ir/announcement/",
     url_dining_webpage = "https://dining.sharif.ir/news/default/view?id=",
     url_refah_webpage = "https://sws.sharif.ir/announcement/",
     url_behdasht_webpage = "https://med.sharif.ir/fa/announcements/" ;
    TextView title,time,abst,body , loading;
    private WebView mywebview ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type", 0);
        switch (type){
            case 1 :
                setContentView(R.layout.activity_single_news_dorm);
                mywebview=findViewById(R.id.web_dorm);
                break;
            case 2 :
                setContentView(R.layout.activity_single_news_refah);
                mywebview=findViewById(R.id.web_refah);
                break;
            case 3 :
                setContentView(R.layout.activity_single_news_dining);
                mywebview=findViewById(R.id.web_dining);
                break;
            case 4 :
                setContentView(R.layout.activity_single_news_behdasht);
                  mywebview=findViewById(R.id.web_health);

                break;
            case 5 :
                setContentView(R.layout.activity_single_news_modir);
                  body = (TextView) findViewById(R.id.body);
                  typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
                  body.setTypeface(typeFace);
                  body.setTextSize(12);
                break;
        }

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

        switch (type){
            case 1 :
                myTextView2a.setText("اداره امور خوابگاه ها");
                break;
            case 2 :
                myTextView2a.setText("اداره رفاه دانشجویان");
                break;
            case 3 :
                myTextView2a.setText("اداره امور تغذیه");
                break;
            case 4 :
                myTextView2a.setText("مرکز بهداشت و درمان");
                break;
            case 5 :
                myTextView2a.setText("سایر خدمات");
                break;
        }
        myTextView2a.setTypeface(typeFace);
        id = getIntent().getStringExtra("id");
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        abst = (TextView) findViewById(R.id.abst);
        loading =findViewById(R.id.loading);



        if (id != "") {
            switch (type){
                case 1 :
                    dormRequest();
                    break;
                case 2 :
                    refahRequest();
                    break;
                case 3 :
                    diningRequest();
                    break;
                case 4 :
                    behdashtRequest();
                    break;
                case 5 :
                    modirRequest();
                    break;
            }
        }
        if (type != 5){
            TxtPresentation();
        }

        if(!Internet.IsConnectionAvailable(getApplicationContext())) {
            TastyToast
                    .makeText(getApplicationContext(), "به اینترنت دسترسی ندارید!", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                    .setMargin(0.015f,0f);
        }

        DoRefreshPage();
    }

    private void TxtPresentation(){
        title.setTypeface(typeFace);
        time.setTypeface(typeFace);
        abst.setTypeface(typeFace);
        loading.setTypeface(typeFace);
            title.setTextSize(15);
            time.setTextSize(10);
            abst.setTextSize(13);
            loading.setTextSize(18);
    }

    private void diningRequest() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url_dining_profile + id, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        try {
                            JSONObject tmp = response.getJSONObject(0);
                            title.setText( convertNums( tmp.getString("title")  ));
                            JalaliCalendar calendar;
                            calendar = new JalaliCalendar(getDateTimeFromTimestamp(tmp.getLong("create_date") * 1000));
                            time.setText(convertNums(calendar.date + " " + calendar.strMonth + " " + calendar.year));
//                            body.setText(Html.fromHtml(tmp.getString("text")));
                            abst.setText(convertNums(tmp.getString("summary")));
                            loadweb(url_dining_webpage+id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
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

    private void behdashtRequest() {
        title.setText(getIntent().getStringExtra("title"));
        time.setText(getIntent().getStringExtra("time"));
        abst.setText(getIntent().getStringExtra("abstract"));
        loadweb(url_behdasht_webpage+id);
    }

    private void modirRequest() {
//        title.setText(getIntent().getStringExtra("title"));
//        time.setText(getIntent().getStringExtra("time"));
//        abst.setText(getIntent().getStringExtra("abstract"));
//       body.setText(getIntent().getStringExtra("body"));
        title.setText("");
        time.setText("");
        abst.setText("");
        body.setText("");
    }

    private void dormRequest() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_dorm_profile + id, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            title.setText(convertNums(response.getString("title") ));
                            JalaliCalendar calendar;
                            calendar = new JalaliCalendar(getDate(response.getLong("time")));
                            time.setText(convertNums( convertNums(calendar.date + " " + calendar.strMonth + " " + calendar.year) ));
                            abst.setText( convertNums( response.getString("abstract")  ));
//                            body.setText(Html.fromHtml(response.getString("body")));
                          loadweb(url_dorm_webpage+id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void refahRequest() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_refah_profile + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            title.setText( convertNums(  response.getString("title")   ));
                            JalaliCalendar calendar;
                            calendar = new JalaliCalendar(getDate(response.getLong("time")));
                            time.setText(convertNums(calendar.date + " " + calendar.strMonth + " " + calendar.year));
                            abst.setText( convertNums( response.getString("abstract")  ));
//                            body.setText(Html.fromHtml(response.getString("body")));
                            loadweb(url_refah_webpage+id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
// Adding request to request queue
        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void loadweb(String WebAddress){
        mywebview.getSettings().setBuiltInZoomControls(true);
        mywebview.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loading.setVisibility(View.GONE);
                mywebview.setVisibility(View.VISIBLE);

            }
        });
        mywebview.getSettings().setDisplayZoomControls(false);
        mywebview.getSettings().setLoadWithOverviewMode(true);
        mywebview.getSettings().setUseWideViewPort(true);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.loadUrl(WebAddress);
    }

    private Date getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        Date currenTimeZone = (Date) cal.getTime();
        return currenTimeZone;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

}
