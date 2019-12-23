package fathi.shakhes;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.DecimalFormat;
import java.util.Calendar;

import java.util.HashMap;
import java.util.Hashtable;

public class AppSessionManager {
    // Shared Preferences
    SharedPreferences pref;
    public static boolean IsAccountNegative = false ;

    // Editor for Shared preferences
     Editor  editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ShakhesPrefs";
    public static final String EXP_TIME = "expiration_time";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_STU_NUM = "student_num";
    public static final String KEY_NAME = "name";
    public static final String KEY_DORM = "dorm";
    public static final String KEY_FIELD = "field";
    public static final String KEY_FIRST_NAME = "fname";
    public static final String KEY_LAST_NAME = "lname";
    public static final String KEY_ROOM = "room";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_STUDENT_NUM = "stunum";
    public static final String KEY_SECTION = "section";
    public static final String FOOD_USERNAME = "fusername";
    public static final String FOOD_PASSWORD = "fpass";
    public static final String FOOD_ACC_T = "facc";
    public static final String FOOD_REF_T = "fref";
    public static final String FOOD_STUNUM = "fstunum";
    public static final String FOOD_FNAME = "ffname";
    public static final String FOOD_LNAME = "flname";
    public static final String FOOD_ACC = "faccount";
    public static final String KEY_COROOM = "coroom";
    public static final String KEY_TEXT_1 = "text1";
    public static final String KEY_TEXT_2 = "text2";
    public static final String data_food_temp = "data_food_temp";



    // Constructor
    public AppSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createDormLoginSession(String username, int ttl,
                                       String access_token, String refresh_token,
                                       String student_num) {
        Calendar now = Calendar.getInstance();

        Calendar tmp = (Calendar) now.clone();
        tmp.add(Calendar.MINUTE, ttl);
        Calendar nowPlusTTL = tmp;

        // Storing login value as TRUE
        editor.putLong(EXP_TIME, nowPlusTTL.getTimeInMillis());

        editor.putString(KEY_USERNAME, username);

        editor.putString(KEY_ACCESS_TOKEN, access_token);

        editor.putString(KEY_REFRESH_TOKEN, refresh_token);

        editor.putString(KEY_STU_NUM, student_num);

        // commit changes
        editor.apply();
    }

    public void createFoodLoginSession(String username, String acc) {
        editor.putString(FOOD_USERNAME, username);
        editor.putString(FOOD_STUNUM, username);
        editor.putString(FOOD_ACC_T, acc);

        // commit changes
        editor.apply();
    }


    public void savefoodpicture(String t) {
        editor.putString("foodpicture", t);

        // commit changes
        editor.apply();
    }

    public String getfoodpicture() {
        return pref.getString("foodpicture", "");
    }


    public void savesharebooklogininfo(String t) {
        editor.putString("sharebooklogininfo", t);

        // commit changes
        editor.apply();
    }

    public String getsharebooklogininfo() {
        return pref.getString("sharebooklogininfo", "-");
    }



    public void savedormnews(String t) {
        editor.putString("dormnews", t);

        // commit changes
        editor.apply();
    }

    public String getdormnews() {
        return pref.getString("dormnews", "");
    }




    public void savefoodnews(String t) {
        editor.putString("foodnews", t);

        // commit changes
        editor.apply();
    }

    public String getfoodnews() {
        return pref.getString("foodnews", "");
    }

    public void savewelfarenews(String t) {
        editor.putString("welfarenews", t);

        // commit changes
        editor.apply();
    }

    public String getwelfarenews() {
        return pref.getString("welfarenews", "");
    }

    public void savehealthnews(String t) {
        editor.putString("healthnews", t);

        // commit changes
        editor.apply();
    }

    public String gethealthnews() {
        return pref.getString("healthnews", "");
    }

    public void saveothernews(String t) {
        editor.putString("othernews", t);

        // commit changes
        editor.apply();
    }

    public String getotherhnews() {
        return pref.getString("othernews", "");
    }


    public void saveText1(String t) {
        editor.putString(KEY_TEXT_1, t);

        // commit changes
        editor.apply();
    }

    public void saveText2(String t) {
        editor.putString(KEY_TEXT_2, t);

        // commit changes
        editor.apply();
    }

    public String getText1() {
        return pref.getString(KEY_TEXT_1, "-");
    }

    public String getText2() {
        return pref.getString(KEY_TEXT_2, "-");
    }

    public void saveuserdorm(String t) {
        editor.putString("dormuser", t);

        // commit changes
        editor.apply();
    }

    public String getuserdorm() {
        return pref.getString("dormuser", "");
    }
    public void savepassdorm(String t) {
        editor.putString("dormpass", t);

        // commit changes
        editor.apply();
    }

    public String getpassdorm() {
        return pref.getString("dormpass", "");
    }

    public void saveuserfood(String t) {
        editor.putString("dininguser", t);

        // commit changes
        editor.apply();
    }

    public String getuserfood() {
        return pref.getString("dininguser", "");
    }
    public void savepassfood(String t) {
        editor.putString("diningpass", t);

        // commit changes
        editor.apply();
    }

    public String getpassfood() {
        return pref.getString("diningpass", "");
    }

    public void saveusersharebook(String t) {
        editor.putString("sharebookuser", t);

        // commit changes
        editor.apply();
    }

    public String getusersharebook() {
        return pref.getString("sharebookuser", "");
    }
    public void savepasssharebook(String t) {
        editor.putString("sharebookpass", t);

        // commit changes
        editor.apply();
    }

    public String getpasssharebook() {
        return pref.getString("sharebookpass", "");
    }



    public void setcheckboxstate_food(Boolean b){
        editor.putBoolean("ischecked_food",b);
        // commit changes
        editor.apply();
    }

    public  boolean getcheckboxstate_food (){
        return pref.getBoolean("ischecked_food" ,false) ;
    }

    public  boolean getcheckboxstate_dorm (){
        return pref.getBoolean("ischecked_dorm" ,false) ;
    }

    public void setcheckboxstate_dorm(Boolean b){
        editor.putBoolean("ischecked_dorm",b);

        // commit changes
        editor.apply();
    }

    public void setcheckboxstate_sharebook(Boolean b){
        editor.putBoolean("ischecked_sharebook",b);
        // commit changes
        editor.apply();
    }

    public  boolean getcheckboxstate_sharebook (){
        return pref.getBoolean("ischecked_sharebook" ,false) ;
    }


    /**
     * Insert profile data
     */
    public void insertDormProfileData(String dorm, String field, String fname,
                                      String lname, String room,
                                      String mobile, String stunum, String section) {
        // Storing login value as TRUE
        if(dorm=="null"){
            dorm = "-----------";
        }
        if(field=="null"){
            field = "-----------";
        }
        if(fname=="null"){
            fname = "-----------";
        }
        if(lname=="null"){
            lname = "-----------";
        }
        if(room=="null"){
            room = "-----------";
        }
        if(mobile=="null"){
            mobile = "-----------";
        }
        if(section=="null"){
            section = "-----------";
        }

        editor.putString(KEY_DORM, dorm);
        editor.putString(KEY_FIELD, field);
        editor.putString(KEY_FIRST_NAME, fname);
        editor.putString(KEY_LAST_NAME, lname);
        editor.putString(KEY_ROOM, room);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_STUDENT_NUM, stunum);
        editor.putString(KEY_SECTION, section);

        // commit changes
        editor.apply();
    }


    public void insertFoodProfileData(String fname, String lname, String stunum, String account) {
        // Storing login value as TRUE
        if(fname=="null"){
            fname = "-----------";
        }
        if(lname=="null"){
            lname = "-----------";
        }
        if(stunum=="null"){
            stunum = "-----------";
        }
        if(account=="null"){
            account = "-----------";
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        double Account1 = Double.parseDouble(account);
        if(Account1 < 0 ){
            IsAccountNegative = true ;
        }else {
            IsAccountNegative = false ;
        }

        String AccountNumber = formatter.format(Account1);

        editor.putString(FOOD_ACC,  AccountNumber);
        editor.putString(FOOD_FNAME, fname);
        editor.putString(FOOD_LNAME, lname);
        editor.putString(FOOD_STUNUM, stunum);

        // commit changes
        editor.apply();
    }

    public String[] getDormProfileData() {
        String[] ret = new String[8];
        ret[0] = pref.getString(KEY_DORM, "");
        ret[1] = pref.getString(KEY_FIELD, "");
        ret[2] = pref.getString(KEY_FIRST_NAME, "");
        ret[3] = pref.getString(KEY_LAST_NAME, "");
        ret[4] = pref.getString(KEY_ROOM, "");
        ret[5] = pref.getString(KEY_MOBILE, "");
        ret[6] = pref.getString(KEY_STUDENT_NUM, "");
        ret[7] = pref.getString(KEY_SECTION, "");
        return ret;
    }

    public String[] getFoodProfileData() {
        String[] ret = new String[4];
        ret[0] = pref.getString(FOOD_FNAME, "");
        ret[1] = pref.getString(FOOD_LNAME, "");
        ret[2] = pref.getString(FOOD_STUNUM, "");
        ret[3] = pref.getString(FOOD_ACC, "");
        return ret;
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

        user.put(KEY_ACCESS_TOKEN, pref.getString(KEY_ACCESS_TOKEN, null));

        user.put(KEY_REFRESH_TOKEN, pref.getString(KEY_REFRESH_TOKEN, null));

        return user;
    }

    public HashMap<String, String> getDiningData() {
        HashMap<String, String> user = new HashMap();

        user.put(KEY_USERNAME, pref.getString(FOOD_USERNAME, null));

        user.put(KEY_ACCESS_TOKEN, pref.getString(FOOD_ACC_T, null));

        return user;
    }


    public void insertcoroom(String d) {
        editor.putString(KEY_COROOM, d);
        editor.apply();
    }
    public String getcoroom() {
        return pref.getString(KEY_COROOM, null);
    }
    public void daysale_food_set(String foodid){
        editor.putString("daysalefoodname",foodid);
        editor.apply();
    }
    public String daysale_food_get() {
        return pref.getString("daysalefoodname", "غذایی برای روزخرید وجود ندارد.");
    }



    public void logout_dining(){
        editor.remove("data_food_json").apply();
        editor.remove("data_food_temp").apply();
    }
}
