package com.linhnv.foodsy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.fragment.HomeFragment;
import com.linhnv.foodsy.fragment.MapFragment;
import com.linhnv.foodsy.fragment.NotificationFragment;
import com.linhnv.foodsy.fragment.BookmarkFragment;
import com.linhnv.foodsy.fragment.ViewPagerAdapter;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigation;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private SP sp;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView txtNameUser, txtEmailUser;
    String username, display_name, email, phone_number, address, gender, role, status;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_search:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_maps:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        sp = new SP(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        int s = getIntent().getExtras().getInt("search");
        if (s == 1){
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
        }

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //view pager
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //khi keo viewpager thi bottom navigation chay theo
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                menuItem = bottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        try {
            getUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        checkRoleUser();

    }

    private void init() {
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigationBottom);
        viewPager = (ViewPager) findViewById(R.id.viewPager1);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void getUser() throws JSONException {
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        String user = sp.getUser();
        JSONObject root = new JSONObject(user);

        txtNameUser = (TextView) header.findViewById(R.id.txtNameUser);
        txtEmailUser = (TextView) header.findViewById(R.id.txtEmailUser);
        JSONObject jsonObject = root.getJSONObject("data");
        User u = new User();
        username = jsonObject.getString("username");
        display_name = jsonObject.getString("display_name");
        email = jsonObject.getString("email");
        phone_number = jsonObject.getString("phone_number");
        address = jsonObject.getString("address");
        gender = jsonObject.getString("gender");
        role = jsonObject.getString("role");
        status = jsonObject.getString("status");

        u.setUsername(username);
        u.setDisplay_name(display_name);
        u.setEmail(email);
        u.setPhone_number(phone_number);
        u.setAddress(address);
        u.setGender(gender);
        u.setRole(role);
        u.setStatus(status);

        if (email == null) {
            txtNameUser.setText(username);
            txtEmailUser.setText("no email register");
        } else {
            txtNameUser.setText(username);
            txtEmailUser.setText(email);
        }

    }

    private void checkRoleUser() {
        Log.e("role", role);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu  = navigationView.getMenu();
        MenuItem owner, admin, user;

        if (role.equals("owner")) {
            owner = menu.findItem(R.id.nav_owner);
            owner.setVisible(true);
            admin = menu.findItem(R.id.nav_admin);
            admin.setVisible(false);
            user = menu.findItem(R.id.nav_register_admin);
            user.setVisible(false);
        }else if (role.equals("admin")) {
            owner = menu.findItem(R.id.nav_owner);
            owner.setVisible(false);
            admin = menu.findItem(R.id.nav_admin);
            admin.setVisible(true);
            user = menu.findItem(R.id.nav_register_admin);
            user.setVisible(false);
        }else {
            owner = menu.findItem(R.id.nav_owner);
            owner.setVisible(false);
            admin = menu.findItem(R.id.nav_admin);
            admin.setVisible(false);
            user = menu.findItem(R.id.nav_register_admin);
            user.setVisible(true);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new HomeFragment());
        viewPagerAdapter.addFrag(new BookmarkFragment());
        viewPagerAdapter.addFrag(new NotificationFragment());
        viewPagerAdapter.addFrag(new MapFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                //finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_register_admin:
                startActivity(new Intent(HomeActivity.this, RegisterOwnerActivity.class));
                break;
            case R.id.nav_owner:
                startActivity(new Intent(HomeActivity.this, PlacesOwnerActivity.class));
                break;
            case R.id.nav_admin:
                startActivity(new Intent(HomeActivity.this, AdminActivity.class));
                break;
            case R.id.log_out:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Xác nhận");
                alertDialog.setMessage("Bạn có muốn đăng xuất khỏi tài khoản này?");
                alertDialog.setPositiveButton(R.string.dialog_button_Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp.setStateLogin(false);
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        finish();
                    }
                });
                alertDialog.setNegativeButton(R.string.dialog_button_Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.create().show();
                break;
        }
        if (id == R.id.nav_home) {
            viewPager.setCurrentItem(0);
            // Handle the camera action
        } else if (id == R.id.nav_bookmark) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_location) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_register_admin) {
            startActivity(new Intent(HomeActivity.this, RegisterOwnerActivity.class));
        } else if (id == R.id.nav_owner) {
            startActivity(new Intent(HomeActivity.this, PlacesOwnerActivity.class));
        } else if (id == R.id.nav_admin) {
            startActivity(new Intent(HomeActivity.this, AdminActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
