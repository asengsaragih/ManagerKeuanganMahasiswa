package id.d3ifcool.managerkeuanganmahasiswa;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import id.d3ifcool.managerkeuanganmahasiswa.database.Contract;

public class WishlistActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //untuk si navigasi
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //untuk loader
    private static final int WISHLIST_LOADER = 1;
    private CursorWishlistAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        //buat si drawer
        mDrawerLayout = findViewById(R.id.drawer_wishlist);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_wishlist);
        setupDrawerContent(navigationView);

        //buat floating button add
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_wishlist);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WishlistActivity.this, FormWishlist.class);
                startActivity(intent);
            }
        });

        //untuk listview nya
        ListView wishlistListView = (ListView) findViewById(R.id.list_item_wishlist);

        View emptyView = (View) findViewById(R.id.empty_view_wishlist);
        wishlistListView.setEmptyView(emptyView);

        mCursorAdapter = new CursorWishlistAdapter(this, null);
        wishlistListView.setAdapter(mCursorAdapter);

        wishlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WishlistActivity.this, FormWishlist.class);
                Uri currentWishlistUri = ContentUris.withAppendedId(Contract.WishlistEntry.CONTENT_URI, id);
                intent.setData(currentWishlistUri);
                startActivity(intent);
            }
        });

        LoaderManager.getInstance(this).initLoader(WISHLIST_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                Contract.WishlistEntry._ID,
                Contract.WishlistEntry.COLUMN_WISHLIST_NAMA };
        return new CursorLoader(WishlistActivity.this,
                Contract.WishlistEntry.CONTENT_URI,
                projection,
                null,
                null,
                null );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
