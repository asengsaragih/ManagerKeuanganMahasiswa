package id.d3ifcool.managerkeuanganmahasiswa;

import android.app.DatePickerDialog;
import android.content.ContentProviderClient;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.d3ifcool.managerkeuanganmahasiswa.database.Contract;
import id.d3ifcool.managerkeuanganmahasiswa.database.Helper;
import id.d3ifcool.managerkeuanganmahasiswa.database.Provider;

public class KeuanganActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private static final int KEUANGAN_LOADER = 0;
    private static final int RESTART_KEUANGAN_LOADER = 1;
    private CursorKeuanganAdapter mCursorAdapter;

    private TextView mPengeluaran;
    private TextView mPemasukkan;
    private TextView mTotalSaldo;
    private TextView mDatePickerKeuangan;

    private final Calendar calendar = Calendar.getInstance();

    private Helper mDbhelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keuangan);

        mDatePickerKeuangan = findViewById(R.id.textView_tanggal_keuangan);
        mPengeluaran = findViewById(R.id.textView_jumlah_pengeluaran);
        mPemasukkan = findViewById(R.id.textView_jumlah_pemasukkan);
        mTotalSaldo = findViewById(R.id.textView_saldo_keseluruhan);

        //buat si drawer
        mDrawerLayout = findViewById(R.id.drawer_keuangan);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_keuangan);
        setupDrawerContent(navigationView);

        //buat floating button
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_keuangan);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KeuanganActivity.this, FormKeuangan.class);
                startActivity(intent);
            }
        });

        //untuk listview yang bukan sub
        ListView keuanganListView = (ListView) findViewById(R.id.listView_keuangan);

        View emptyView = (View) findViewById(R.id.empty_view_keuangan);
        keuanganListView.setEmptyView(emptyView);

        mCursorAdapter = new CursorKeuanganAdapter(this, null);
        keuanganListView.setAdapter(mCursorAdapter);

        keuanganListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KeuanganActivity.this, FormKeuangan.class);
                Uri currentKeuanganUri = ContentUris.withAppendedId(Contract.KeuanganEntry.CONTENT_URI, id);

                intent.setData(currentKeuanganUri);
                startActivity(intent);
            }
        });

        //untuk memanggil date picker keuangan
        dateTextView();
        datePicker();
        LoaderManager.getInstance(this).initLoader(KEUANGAN_LOADER, null, this);

        //kalau total error bisa dihapus disini
        mDbhelper = new Helper(this);
    }

    //kalau total eror yang onstart juga dihapus
    @Override
    protected void onStart() {
        super.onStart();
        tampilTotal();
    }

    private void dateTextView(){
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String date_str = df.format(cal.getTime());
        mDatePickerKeuangan.setText(date_str);
    }


    private void datePicker(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        mDatePickerKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(KeuanganActivity.this, date,  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDatePickerKeuangan.setText(sdf.format(calendar.getTime()));

        //ditambah juga disini laoder callback untuk restart loadernya, jadi setiap kita memilih tanggal loader harus direstart
        LoaderManager.getInstance(this).restartLoader(RESTART_KEUANGAN_LOADER, null, this);
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
                Contract.KeuanganEntry._ID,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL
        };

        String clauseWhere = Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL+"=?";
        String dateWhere = (String) mDatePickerKeuangan.getText();
        String[] whereArgsClause = {dateWhere.toString()};


        return new CursorLoader(KeuanganActivity.this,
                Contract.KeuanganEntry.CONTENT_URI,
                projection,
                clauseWhere,
                whereArgsClause,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

        int totalPengeluaran = 0;
        int totalPemasukkan = 0;
//        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            int StatusKeuangan = cursor.getInt(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS));
//            switch (StatusKeuangan){
//                case 1:
//                    //pemasukkan
//                    totalPemasukkan += cursor.getInt(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH));
//                    break;
//                case 2:
//                    //pengeluaran
//                    totalPengeluaran += cursor.getInt(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH));
//                    break;
//            }
            if (StatusKeuangan == 1){
                totalPemasukkan += cursor.getInt(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH));
            }
            else if (StatusKeuangan == 2){
                totalPengeluaran += cursor.getInt(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH));
            }
        }
        mPengeluaran.setText("Rp. " + totalPengeluaran);
        mPemasukkan.setText("Rp. "+ totalPemasukkan);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void tampilTotal() {
        SQLiteDatabase db = mDbhelper.getReadableDatabase();

        String[] projection = {
                Contract.KeuanganEntry._ID,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH
        };

        Cursor cursor = db.query(Contract.KeuanganEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        try {
            int total_kel = 0;
            int pengeluaran_total = 0;
            int pemasukkan_total = 0;

            while (cursor.moveToNext()){
                int StatusKeuangan_Total = cursor.getInt(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS));
                if (StatusKeuangan_Total == 1){
                    pemasukkan_total += cursor.getInt(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH));
                } else if (StatusKeuangan_Total == 2){
                    pengeluaran_total += cursor.getInt(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH));
                }
                total_kel = pemasukkan_total - pengeluaran_total;
                mTotalSaldo.setText("Rp. " + total_kel);
            }
        } finally {
            cursor.close();
        }
    }
}

