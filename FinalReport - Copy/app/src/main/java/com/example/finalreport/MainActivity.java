package com.example.finalreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.finalreport.GPT.GPTactivity;
import com.example.finalreport.authentication.ChangePasswordActivity;
import com.example.finalreport.authentication.LoginActivity;
import com.example.finalreport.authentication.ProfileActivity;
import com.example.finalreport.authentication.RegisterActivity;
import com.example.finalreport.leaderboard.LeaderboardActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private NavController navController;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    FirebaseUser firebaseUser;

    FirebaseAuth auth;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
//      nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navController = Navigation.findNavController(this, R.id.main_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

//        drawer
        drawerLayout = findViewById(R.id.my_drawer);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

    }

    //function of drawer

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        if(item.getItemId() == R.id.profile){
            if(firebaseUser != null){
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(auth != null){
            switch (menuItem.getItemId()){
                case R.id.leaderboard:
                    startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
                    break;
                case R.id.share:
                    startActivity(new Intent(MainActivity.this, GPTactivity.class));
                    break;
                case R.id.rate:
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    break;
                case R.id.change_pass:
                    startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                    break;
            }
        } else {
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
        }
//        switch (menuItem.getItemId()){
//            case R.id.leaderboard:
//                startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
//                break;
//            case R.id.share:
//                startActivity(new Intent(MainActivity.this, GPTactivity.class));
//                break;
//            case R.id.rate:
//                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//                break;
//            case R.id.change_pass:
//                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
//                break;
//        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}