package fathi.shakhes;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fathi.shakhes.loginShareBook.LoginShareBook;
import shakhes.R;

public class MainActivity extends AppCompatActivity
   {
    boolean BackPress = false;
    Dialog getUnder_const_refah,getUnder_const_behdasht,getUnder_const_sharebook,
       getUnder_const_dorm;
    private Typeface typeFace;

       @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.shariflogo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        View v = LayoutInflater
                .from(getSupportActionBar().getThemedContext())
                .inflate(R.layout.action_bar1, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(v, params);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        TextView myTextView1 = (TextView) findViewById(R.id.text1);
        TextView myTextView2 = (TextView) findViewById(R.id.text2);
        myTextView1.setTypeface(typeFace);
        myTextView2.setTypeface(typeFace);
    }
    public void news(View view) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent NewsIntent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(NewsIntent);
            }
        }, 200);
    }
    public void food(View view) {
           final Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               public void run() {
                   Intent FoodIntent = new Intent(MainActivity.this, LoginFood.class);
                   startActivity(FoodIntent);
               }
           }, 270);
       }
    public void welfare(View view) {
        getUnder_const_refah = new Dialog(this);
                            getUnder_const_refah.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            getUnder_const_refah.setContentView(R.layout.layout_under_const_refah);
                            Button btnCancel = (Button) getUnder_const_refah.findViewById(R.id.btnCancel);
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getUnder_const_refah.dismiss();
                                }
                            });
                            // Make dialog box visible.
                            getUnder_const_refah.show();


                        }
    public void dorm(View view) {
//           final Handler handler1 = new Handler();
//           handler1.postDelayed(new Runnable() {
//               @Override
//               public void run() {
//                   Intent DormIntent = new Intent(MainActivity.this, LoginDorm.class);
//                   startActivity(DormIntent);
//               }
//           }, 270);

        getUnder_const_dorm = new Dialog(this);
        getUnder_const_dorm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getUnder_const_dorm.setContentView(R.layout.layout_under_const_dorm);
        Button btnCancel = (Button) getUnder_const_dorm.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUnder_const_dorm.dismiss();
            }
        });
        // Make dialog box visible.
        getUnder_const_dorm.show();


       }
    public void ShareBook(View view) {


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent SharebookIntent = new Intent(MainActivity.this, LoginShareBook.class);
                startActivity(SharebookIntent);
            }
        }, 270);

//        getUnder_const_sharebook = new Dialog(this);
//        getUnder_const_sharebook.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getUnder_const_sharebook.setContentView(R.layout.layout_under_const_sharebook);
//        Button btnCancel = (Button)  getUnder_const_sharebook.findViewById(R.id.btnCancel);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getUnder_const_sharebook.dismiss();
//            }
//        });
//
//   //        Make dialog box visible.
//        getUnder_const_sharebook.show();
    }
    public void health(View view) {
        getUnder_const_behdasht = new Dialog(this);
                    getUnder_const_behdasht.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    getUnder_const_behdasht.setContentView(R.layout.layout_under_const_behdasht);
                    Button btnCancel = (Button) getUnder_const_behdasht.findViewById(R.id.btnCancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getUnder_const_behdasht.dismiss();
                        }
                    });
                    // Make dialog box visible.
                    getUnder_const_behdasht.show();
                }
     @Override
      public void  onBackPressed() {
         if (BackPress) {
//             SplashScreen.SplashHistory = false ;
             super.onBackPressed();
             overridePendingTransition(0,R.anim.fade_out);
             MainActivity.this.finish();
         }
         this.BackPress = true;
         Toast toast=Toast.makeText(getApplicationContext(),"برای خروج دو بار کلیک کنید",Toast.LENGTH_SHORT);
         toast.setMargin(0,-0.4f);
         View view=toast.getView();
         TextView view1=(TextView)view.findViewById(android.R.id.message);
         view1.setTextSize(18);
         view1.setTextColor(Color.WHITE);
         view.setBackgroundResource(R.color.red);
         view1.setTypeface(typeFace);
         toast.show();
         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 BackPress =false;
             }
         },2000);
    }

}