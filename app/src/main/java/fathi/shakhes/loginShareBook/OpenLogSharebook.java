package fathi.shakhes.loginShareBook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import fathi.shakhes.AppSessionManager;
import fathi.shakhes.SplashScreen;
import shakhes.BuildConfig;
import shakhes.R;

public class OpenLogSharebook extends AppCompatActivity
        {
            AppSessionManager sessionManager ;
            private DrawerLayout mDrawerLayout;
            private RecyclerView mRecyclerView;
            private RecyclerView.Adapter mAdapter;
            private RecyclerView.LayoutManager mLayoutManager;
            private String[] myDataset;
            private ShareActionProvider mShareActionProvider;
            private Typeface typeFace ;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new AppSessionManager(getApplicationContext());
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        setContentView(R.layout.openlog_sharebook);
        Toolbar toolbar = findViewById(R.id.toolbar_Openlog_sharebook);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        ((TextView)toolbar.getChildAt(0)).setTypeface(typeFace);
        mDrawerLayout = findViewById(R.id.drawer_layout);
                // Initialize list to store collection of attractions
//                AttractionRepository repository = AttractionRepository.getInstance(this);
//                List<AttractionCollection> collections = repository.getCollections();

                // Hook the recycler view
                RecyclerView recyclerView = findViewById(R.id.main_recycler_view);

                // Set fixed size true and optimize recycler view performance
                // The data container has fixed number of attractions and not infinite list
                recyclerView.setHasFixedSize(true);

                // Connect the RecyclerView widget to the vertical linear layout
                // (not reverse layout - hence false)
                recyclerView.setLayoutManager(new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL, false));

                // Attach adapter to the RecyclerView widget which is connected to a layout manager
//                MasterAdapter collectionAdapter = new MasterAdapter(this, collections);
//                recyclerView.setAdapter(collectionAdapter);


                mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped

                        switch (menuItem.toString()) {
                            case "نمایه":
                                Toast.makeText(OpenLogSharebook.this, sessionManager.getsharebooklogininfo(), Toast.LENGTH_SHORT).show();
                                break;
                            case "کتاب ها":
                                Toast.makeText(OpenLogSharebook.this, "کتاب ها", Toast.LENGTH_SHORT).show();
                                break;
                            case "سفارش ها":
                                Toast.makeText(OpenLogSharebook.this, "سفارش ها", Toast.LENGTH_SHORT).show();
                                break;
                            case "کیف پول":
                                Toast.makeText(OpenLogSharebook.this, "کیف پول", Toast.LENGTH_SHORT).show();
                                break;
                            case "تراکنش های بانکی":
                                Toast.makeText(OpenLogSharebook.this, "تراکنش های بانکی", Toast.LENGTH_SHORT).show();
                                break;
                            case "معرفی به دوستان":
                                SharedLink();
                                break;
                            case "درباره ما":
                                ContactUs();
                                break;
                            case "خروج":
                                Exit();
                                break;
                        }


                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

    }


            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    }
                    else {
                        mDrawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }
                return false;
            }

            private void ContactUs() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        OpenLogSharebook.this,R.style.Theme_AppCompat);
                // set dialog message
                alertDialogBuilder
                        .setView(R.layout.rules_sharebook)
                        .setCancelable(false)
                        .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //   Toast.makeText(RegisterActivity.this, "خواندم ", Toast.LENGTH_SHORT).show();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }

            private void Exit(){
                AlertDialog alertDialog = new AlertDialog.Builder(OpenLogSharebook.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                        //set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        //set positive button
                        .setMessage("می خواهید خارج شوید؟           ")
                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                startActivity(new Intent(OpenLogSharebook.this, LoginShareBook.class));
                                finish();
                            }
                        })
                        //set negative button
                        .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                 //               Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }

            private void SharedLink(){
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "شاخص");
                    String shareMessage= "\nشاخص را از اینجا دریافت کنید\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "شاخص"));
                }
                catch(Exception e) {
                    Toast.makeText(OpenLogSharebook.this, "مشکلی پیش آمده است!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBackPressed() {
                // Disable going back to the MainActivity
                Intent BackToMain = new Intent(OpenLogSharebook.this, SplashScreen.class);
                startActivity(BackToMain);
                finish();
            }

            }