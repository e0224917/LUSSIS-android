package com.sa45team7.lussis.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.data.UserManager;
import com.sa45team7.lussis.fragments.CollectionPointFragment;
import com.sa45team7.lussis.fragments.HomeFragment;
import com.sa45team7.lussis.fragments.MyDelegateFragment;
import com.sa45team7.lussis.fragments.PendingReqFragment;
import com.sa45team7.lussis.rest.model.Employee;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener {

    private Employee employee;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        employee = UserManager.getInstance().getCurrentEmployee();
        String role = employee.getJobTitle();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        switch (role) {
            case "clerk":
                navigationView.inflateMenu(R.menu.clerk_drawer);
                break;
            case "supervisor":
                navigationView.inflateMenu(R.menu.supervisor_drawer);
                break;
            case "manager":
                break;
            case "staff":
                if (employee.isDelegated())
                    navigationView.inflateMenu(R.menu.staff_drawer);
                else
                    navigationView.inflateMenu(R.menu.staff_drawer);
                break;
            case "rep":
                navigationView.inflateMenu(R.menu.rep_drawer);
                break;
            case "head":
                navigationView.inflateMenu(R.menu.head_drawer);
                break;
            default:
                break;
        }

        View headerView = navigationView.getHeaderView(0);

        TextView nameText = headerView.findViewById(R.id.employee_name);
        nameText.setText(employee.getFirstName() + " " + employee.getLastName());

        String displayRole = role.substring(0, 1).toUpperCase() + role.substring(1);
        TextView deptText = headerView.findViewById(R.id.employee_role);
        deptText.setText(displayRole);

        fragmentManager = getSupportFragmentManager();
        HomeFragment fragment = new HomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.getClass().toString())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            if (getSupportFragmentManager().getFragments().size() > 1) {
//                displayFragment(new HomeFragment());
//            } else {
                super.onBackPressed();
//            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                displayFragment(new HomeFragment());
                break;
            case R.id.nav_pendingreq:
                displayFragment(new PendingReqFragment());
                break;
            case R.id.nav_delegate:
                displayFragment(new MyDelegateFragment());
                break;
            case R.id.nav_collection:
                displayFragment(new CollectionPointFragment());
                break;
            case R.id.nav_scan:
                Intent intent = new Intent(BaseActivity.this, ScanQRActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                UserManager.getInstance().clear();
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        toolbar.setTitle(item.getTitle());

        return true;
    }

    private void displayFragment(Fragment fragment) {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (!fragment.getClass().toString().equals(currentFragment.getTag())) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, fragment.getClass().toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
