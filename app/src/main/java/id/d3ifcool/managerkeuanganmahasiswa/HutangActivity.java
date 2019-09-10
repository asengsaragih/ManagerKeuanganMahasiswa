package id.d3ifcool.managerkeuanganmahasiswa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HutangActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hutang);

        //buat si drawer
        mDrawerLayout = findViewById(R.id.drawer_hutang);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_hutang);
        setupDrawerContent(navigationView);

        //buat si slideder nya atau view pager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_Hutang);
        CategoryHutangAdapter hutangAdapter = new CategoryHutangAdapter(HutangActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(hutangAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_hutang);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }

        //untuk manggil si history dari si hutang
        int id = item.getItemId();
        if (id == R.id.action_history){
            Intent intent = new Intent(HutangActivity.this, HistoryHutangActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.history_hutang_menu, menu);
//        return true;
//    }

    private void selectItemDrawer(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.keuangan:
                Intent keuanganClass = new Intent(this, KeuanganActivity.class);
                startActivity(keuanganClass);
                break;
            case R.id.hutang:
                Intent hutangClass = new Intent(this, HutangActivity.class);
                startActivity(hutangClass);
                break;
            case R.id.wishlist:
                Intent wishlistClass = new Intent(this, WishlistActivity.class);
                startActivity(wishlistClass);
                break;
            case R.id.setting:
                Intent settingClass = new Intent(this, SettingActivity.class);
                startActivity(settingClass);
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectItemDrawer(menuItem);
                return true;
            }
        });
    }
}
