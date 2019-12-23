package fathi.shakhes;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.URL;

//import io.realm.Realm;
//import io.realm.RealmConfiguration;


public class MySingleton extends Application {

    public static final String TAG = MySingleton.class
            .getSimpleName();

    public static RequestQueue mRequestQueue;
    public static Cache cache ;
    public ImageLoader mImageLoader;

    private static MySingleton mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
//        Realm.init(this);
//
//        RealmConfiguration configuration = new RealmConfiguration.Builder()
//                .name("Shakhes.realm") // By default the name of db is "default.realm"
//                .build();
//
//        Realm.setDefaultConfiguration(configuration);

        mInstance = this;
    }

    public static synchronized MySingleton getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
       cache =  mRequestQueue.getCache();
        cache.initialize();
        return mRequestQueue;
    }

//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}