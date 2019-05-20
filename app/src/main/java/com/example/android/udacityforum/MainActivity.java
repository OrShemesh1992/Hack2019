package com.example.android.udacityforum;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;
    private TextView UserEmail;
    private ImageView UserPic;
    private TextView UserName;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //navigation view.
        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //setting values to navigation drawer header.
        View header = navigationView.getHeaderView(0);
        UserName = (TextView) header.findViewById(R.id.tv_name);
        UserPic = (ImageView) header.findViewById(R.id.iv_profile);
        UserEmail = (TextView) header.findViewById(R.id.tv_email);

        //Firebase authentication.
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userDetails = firebaseAuth.getCurrentUser();
                if (userDetails != null) {
                    //user is signed in.
                    UserName.setText(userDetails.getDisplayName());
                    Glide.with(MainActivity.this).load(userDetails.getPhotoUrl()).into(UserPic);
                    UserEmail.setText(userDetails.getEmail());
                } else {
                    //user is signed out.
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build()))
                                    .setLogo(R.drawable.udacity_logo)
                                    .setTheme(R.style.FullscreenTheme)
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };

        //on item click in navigation side bar.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_profile:
                        Intent k = new Intent(MainActivity.this, activity_userpanel.class);
                        k.putExtra("name2", userDetails.getDisplayName());
                        k.putExtra("picURL2","data:image/png;base64,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");
                        k.putExtra("Email2", userDetails.getEmail());
                        startActivity(k);
                        break;

                    case R.id.menu_home:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case R.id.menu_logout:
                        AuthUI.getInstance().signOut(MainActivity.this);
                        finish();
                }
                return false;
            }
        });

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams
                .MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View actionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_custom_view, null);

        TextView actionBarTitle = (TextView) actionBarView.findViewById(R.id.tv_heading);
        actionBarTitle.setText(getString(R.string.activity_name));

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(actionBarView, params);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ham_icon);

        Button btn_forum = (Button) findViewById(R.id.btn_forum);
        btn_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(MainActivity.this, ForumActivity.class);
                j.putExtra("name", userDetails.getDisplayName());
               // j.putExtra("picURL", userDetails.getPhotoUrl().toString());
                j.putExtra("Email", userDetails.getEmail());
                startActivity(j);
            }
        });
        Button btn_user = (Button) findViewById(R.id.user_panel);
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(MainActivity.this, activity_userpanel.class);
                k.putExtra("name2", userDetails.getDisplayName());
                k.putExtra("picURL2","data:image/png;base64,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");
                k.putExtra("Email2", userDetails.getEmail());
                startActivity(k);
            }
        });


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(getApplicationContext().SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Toast.makeText(getApplicationContext(), "Action New Clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_top:
                Toast.makeText(getApplicationContext(), "Action Top Clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_badges:
                Toast.makeText(getApplicationContext(), "Action Badges Clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "Action Search Clicked", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void export() {

    }

}
