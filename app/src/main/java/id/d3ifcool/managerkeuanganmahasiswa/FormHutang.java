package id.d3ifcool.managerkeuanganmahasiswa;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.d3ifcool.managerkeuanganmahasiswa.database.Contract;

public class FormHutang extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_HUTANG_LOADER = 0;

    private final Calendar calendar = Calendar.getInstance();

    private Uri mCurrentHutangUri;

    private EditText mTanggalEditText;
    private EditText mJumlahEditText;
    private EditText mNamaEditText;
    private Spinner mStatusSpinner;

    private int mStatus = Contract.HutangEntry.HUTANG_UNKNOW;

    private boolean mHutangHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mHutangHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_hutang);

        Intent intent = getIntent();
        mCurrentHutangUri = intent.getData();

        if (mCurrentHutangUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_data));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_data));
            LoaderManager.getInstance(this).initLoader(EXISTING_HUTANG_LOADER, null, this);
        }

        mTanggalEditText = (EditText) findViewById(R.id.editText_tanggal_hutang);
        mJumlahEditText = (EditText) findViewById(R.id.editText_jumlah_hutang);
        mNamaEditText = (EditText) findViewById(R.id.editText_nama_hutang);
        mStatusSpinner = (Spinner) findViewById(R.id.spinner_status_hutang);

        mTanggalEditText.setOnTouchListener(mTouchListener);
        mJumlahEditText.setOnTouchListener(mTouchListener);
        mNamaEditText.setOnTouchListener(mTouchListener);
        mStatusSpinner.setOnTouchListener(mTouchListener);

        datePicker();
        setupSpinner();
    }

    private void datePicker() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        mTanggalEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(FormHutang.this, date,  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTanggalEditText.setText(sdf.format(calendar.getTime()));
    }

    private void setupSpinner(){
        ArrayAdapter statusKeuanganSpinnerAdapter= ArrayAdapter.createFromResource(this,
                R.array.array_hutang_options, android.R.layout.simple_spinner_item);

        statusKeuanganSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mStatusSpinner.setAdapter(statusKeuanganSpinnerAdapter);

        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.hutang_dipinjamkan))){
                        mStatus = Contract.HutangEntry.HUTANG_DIPINJAMKAN;
                    } else if (selection.equals(getString(R.string.hutang_meminjam))){
                        mStatus = Contract.HutangEntry.HUTANG_MEMINJAM;
                    } else {
                        mStatus = Contract.HutangEntry.HUTANG_UNKNOW;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mStatus = Contract.HutangEntry.HUTANG_UNKNOW;
            }
        });
    }

    private void saveHutang() {
        String tanggalString = mTanggalEditText.getText().toString().trim();
        String jumlahString = mJumlahEditText.getText().toString().trim();
        String namaString = mNamaEditText.getText().toString().trim();

        if (mCurrentHutangUri == null &&
                TextUtils.isEmpty(tanggalString) && TextUtils.isEmpty(jumlahString) &&
                TextUtils.isEmpty(namaString) && mStatus == Contract.HutangEntry.HUTANG_UNKNOW){
            return;
        }

        int jumlahTotal = 0;
        if (!TextUtils.isEmpty(jumlahString)) {
            jumlahTotal = Integer.parseInt(jumlahString);
        }

        ContentValues values = new ContentValues();
        values.put(Contract.HutangEntry.COLUMN_HUTANG_TANGGAL, tanggalString);
        values.put(Contract.HutangEntry.COLUMN_HUTANG_NAMA, namaString);
        values.put(Contract.HutangEntry.COLUMN_HUTANG_STATUS, mStatus);
        values.put(Contract.HutangEntry.COLUMN_HUTANG_JUMLAH, jumlahTotal);

        if (mCurrentHutangUri == null){
            Uri newUri = getContentResolver().insert(Contract.HutangEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_success), Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowAffected = getContentResolver().update(mCurrentHutangUri, values, null, null);

            if (rowAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentHutangUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveHutang();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mHutangHasChanged) {
                    NavUtils.navigateUpFromSameTask(FormHutang.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(FormHutang.this);
                    }
                };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mHutangHasChanged){
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                Contract.HutangEntry._ID,
                Contract.HutangEntry.COLUMN_HUTANG_TANGGAL,
                Contract.HutangEntry.COLUMN_HUTANG_JUMLAH,
                Contract.HutangEntry.COLUMN_HUTANG_NAMA,
                Contract.HutangEntry.COLUMN_HUTANG_STATUS
        };

        return new CursorLoader(this,
                mCurrentHutangUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()) {
            int tanggalColumnIndex = cursor.getColumnIndex(Contract.HutangEntry.COLUMN_HUTANG_TANGGAL);
            int jumlahColumnIndex = cursor.getColumnIndex(Contract.HutangEntry.COLUMN_HUTANG_JUMLAH);
            int namaColumnIndex = cursor.getColumnIndex(Contract.HutangEntry.COLUMN_HUTANG_NAMA);
            int statusColumnIndex = cursor.getColumnIndex(Contract.HutangEntry.COLUMN_HUTANG_STATUS);

            String StringTanggal = cursor.getString(tanggalColumnIndex);
            String StringNama = cursor.getString(namaColumnIndex);
            int IntJumlah = cursor.getInt(jumlahColumnIndex);
            int IntStatus = cursor.getInt(statusColumnIndex);

            mTanggalEditText.setText(StringTanggal);
            mJumlahEditText.setText(Integer.toString(IntJumlah));
            mNamaEditText.setText(StringNama);

            switch (IntStatus) {
                case Contract.HutangEntry.HUTANG_DIPINJAMKAN:
                    mStatusSpinner.setSelection(1);
                    break;
                case Contract.HutangEntry.HUTANG_MEMINJAM:
                    mStatusSpinner.setSelection(2);
                default:
                    mStatusSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mNamaEditText.setText("");
        mTanggalEditText.setText("");
        mJumlahEditText.setText("");
        mStatusSpinner.setSelection(0);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.ya, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDataHutang();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteDataHutang() {
        if (mCurrentHutangUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentHutangUri, null, null);

            if (rowsDeleted == 0){
                Toast.makeText(this, getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
