package fathi.shakhes.FoodMessageTab;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.icu.util.ULocale;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import fathi.shakhes.AppSessionManager;
import fathi.shakhes.JalaliCalendar;
import fathi.shakhes.MySingleton;
import shakhes.R;

public class ReadMessage extends Fragment {
    JalaliCalendar calendar ;
    TextView plain_body,plain_date ,nomessage_text,delete_message;
    View rootView ;
    AppSessionManager sessions ;
    Typeface typeFace ;
    String token ;
    String[] titles , bodies , sentDates ,date_food,time_food , titles_tmp,id_tmp;
    Boolean [] reads ;
    String url_food_messages = "https://dining.sharif.ir/api/messages?access_token=";
    String url_food_delete   = "https://dining.sharif.ir/api/delete-message?access_token=";
    int message_id[] ;
    ArrayList<String> ReadList , ReadId ;
    ListView listView ;
    public static ReadMessage newInstance(){
        return new ReadMessage();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sessions = new AppSessionManager(getActivity().getApplicationContext());
        ReadList = new ArrayList<String>();
        ReadId = new ArrayList<String>();
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSans.ttf");
        rootView = inflater.inflate(R.layout.activity_food_message,
                container, false);
        token = getActivity().getIntent().getStringExtra("token");
        listView = (ListView) rootView.findViewById(R.id.message_listview);
        nomessage_text = rootView.findViewById(R.id.nomessagetext);
        nomessage_text.setTypeface(typeFace);
        getMessages();
        return rootView;
    }
    public  void getMessages() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url_food_messages + token, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        message_id = new int[response.length()];
                        titles = new String[response.length()] ;
                        bodies = new String[response.length()] ;
                        sentDates = new String[response.length()] ;
                        reads = new Boolean[response.length()] ;
                        date_food = new String[response.length()];
                        time_food = new String[response.length()];
                        for (int i = 0; i <response.length() ; i++) {
                            try {
                                JSONObject message_number = response.getJSONObject(i);
                                reads[i] = message_number.getBoolean("read");
                                titles[i] = message_number.getString("title");
                                bodies[i] = Html.fromHtml(message_number.getString("body")).toString();
                                sentDates[i] = message_number.getString("sentDate");
                                message_id[i]=message_number.getInt("id");
                                String[] splited = sentDates[i].split("\\s+");
                                calendar = new JalaliCalendar(changetohejri(splited[0]));
                                date_food[i] = calendar.date + " " + calendar.strMonth + " " + calendar.year;
                                time_food[i] = splited[1];
                                if (reads[i] == true){
                                    ReadList.add(titles[i]);
                                    ReadId.add(String.valueOf(message_id[i]));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        titles_tmp = ReadList.toArray(new String[ReadList.size()]);
                        id_tmp = ReadId.toArray(new String[ReadId.size()]);
                        if (titles_tmp.length != 0){

                                nomessage_text.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);

                                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                                        R.layout.food_message_listview, titles_tmp)

                                {
                                    @NonNull
                                    @Override
                                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView textview = (TextView) view;
                                        textview.setTypeface(typeFace);
                                        return textview;
                                    }
                                };

                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        AlertNews(bodies[position],date_food[position]+"\n" + time_food[position]);
                                    }
                                });

                                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                     AlertNewsDelete(position);
                                        return true;
                                    }
                                });


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
    public  void DeleteMessages(String message_id) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url_food_delete + token + "&id=" + message_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        getActivity().finish();
                        getActivity().overridePendingTransition( 0, 0);
                        startActivity(getActivity().getIntent());
                        getActivity().overridePendingTransition( 0, 0);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void AlertNews(String body, String date) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity(),R.style.AppTheme_Dark_Dialog);
        // set dialog message
        alertDialogBuilder
                .setView(R.layout.foodmessage_plain)
                .setCancelable(false)
                .setPositiveButton("خواندم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

        plain_body=alertDialog.findViewById(R.id.foodmessage_plain_body);
        plain_date=alertDialog.findViewById(R.id.foodmessage_plain_date);

        plain_body.setTypeface(typeFace);
        plain_body.setText(body);
        plain_date.setTypeface(typeFace);
        plain_date.setText(date);


        alertDialog.findViewById(android.R.id.button1).setBackgroundResource(R.drawable.button_dining);
        Window window = alertDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongConstant")
    private void AlertNewsDelete(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity(),R.style.AppTheme_Dark_Dialog);
        // set dialog message
        alertDialogBuilder
                .setView(R.layout.foodmessage_delete)
                .setCancelable(false)


                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteMessages(String.valueOf(id_tmp[position]));
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

        delete_message= alertDialog.findViewById(R.id.deletemessage_text);

        delete_message.setTypeface(typeFace);

        alertDialog.findViewById(android.R.id.button1).setBackgroundResource(R.drawable.button_dining);
        alertDialog.findViewById(android.R.id.button2).setBackgroundResource(R.drawable.button_dining);

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParamsPositive = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        LinearLayout.LayoutParams layoutParamsNegative = (LinearLayout.LayoutParams) btnNegative.getLayoutParams();


        layoutParamsPositive.setMargins(10,0,0,0);

        btnPositive.setLayoutParams(layoutParamsPositive);

        Window window = alertDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private Date changetohejri(String gregorianData){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedCurrentDate = null;
        try {
            convertedCurrentDate = sdf.parse(gregorianData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedCurrentDate;
    }
}