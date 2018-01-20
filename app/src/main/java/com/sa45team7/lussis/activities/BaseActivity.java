package com.sa45team7.lussis.activities;

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
import com.sa45team7.lussis.fragments.HomeFragment;
import com.sa45team7.lussis.fragments.MyDelegateFragment;
import com.sa45team7.lussis.fragments.PendingReqFragment;
import com.sa45team7.lussis.rest.model.Employee;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener {

    private Employee employee;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Nice");
        setSupportActionBar(toolbar);

        employee = UserManager.getInstance().getCurrentEmployee();
        String role = employee.getJobTitle();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getFragments().size() > 1) {
                displayFragment(new HomeFragment());
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                break;
            case R.id.nav_pendingreq:
                displayFragment(new PendingReqFragment());
                break;
            case R.id.nav_delegate:
                displayFragment(new MyDelegateFragment());
                break;
            case R.id.nav_logout:
                UserManager.getInstance().clear();
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

}
