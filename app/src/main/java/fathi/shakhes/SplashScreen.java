package fathi.shakhes;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import shakhes.R;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    public static  boolean SplashHistory = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SplashHistory) {
            getSupportActionBar().hide();
            setContentView(R.layout.activity_splash);
            TextView textView1 = (TextView)findViewById(R.id.textView1);
            TextView textView2 = (TextView)findViewById(R.id.textView2);
            TextView textView3 = (TextView)findViewById(R.id.textView3);

            Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
            textView1.setTypeface(typeFace);
            textView2.setTypeface(typeFace);
            textView3.setTypeface(typeFace);
            handler = new Handler() ;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }, 1000);
          SplashHistory = false ;
        }
        else {
            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }
    }

}