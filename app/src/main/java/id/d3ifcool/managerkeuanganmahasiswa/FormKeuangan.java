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

public class FormKeuangan extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_KEUANGAN_LOADER = 0;

    private final Calendar calendar = Calendar.getInstance();

    private Uri mCurrentKeuanganUri;

    private EditText mTanggalEditText;
    private EditText mJumlahEditText;
    private EditText mKeteranganEditText;
    private Spinner mStatusSpinner;

    private int mStatus = Contract.KeuanganEntry.KEUANGAN_UNKNOW;

    private boolean mKeuanganHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mKeuanganHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_keuangan);

        Intent intent = getIntent();
        mCurrentKeuanganUri = intent.getData();

        if (mCurrentKeuanganUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_data));
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_data));
            LoaderManager.getInstance(this).initLoader(EXISTING_KEUANGAN_LOADER, null, this);
        }

        mTanggalEditText = (EditText) findViewById(R.id.editText_tanggal_keuangan);
        mJumlahEditText = (EditText) findViewById(R.id.editText_jumlah_keuangan);
        mKeteranganEditText = (EditText) findViewById(R.id.editText_keterangan_keuangan);
        mStatusSpinner = (Spinner) findViewById(R.id.spinner_status_keuangan);

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
                new DatePickerDialog(FormKeuangan.this, date,  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTanggalEditText.setText(sdf.format(calendar.getTime()));
    }

    private void setupSpinner(){
        ArrayAdapter statusKeuanganSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_keuangan_options, android.R.layout.simple_spinner_item);

        statusKeuanganSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mStatusSpinner.setAdapter(statusKeuanganSpinnerAdapter);

        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals(getString(R.string.pemasukkan))){
                        mStatus = Contract.KeuanganEntry.KEUANGAN_PEMASUKKAN;
                    } else if (selection.equals(getString(R.string.pengeluaran))){
                        mStatus = Contract.KeuanganEntry.KEUANGAN_PENGELUARAN;
                    } else {
                        mStatus = Contract.KeuanganEntry.KEUANGAN_UNKNOW;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mStatus = Contract.KeuanganEntry.KEUANGAN_UNKNOW;
            }
        });
    }

    private void saveKeuangan() {
        String tanggalString = mTanggalEditText.getText().toString().trim();
        String jumlahString = mJumlahEditText.getText().toString().trim();
        String keteranganString = mKeteranganEditText.getText().toString().trim();

        if (mCurrentKeuanganUri == null &&
                TextUtils.isEmpty(tanggalString) && TextUtils.isEmpty(jumlahString) &&
                TextUtils.isEmpty(keteranganString) && mStatus == Contract.KeuanganEntry.KEUANGAN_UNKNOW){
            return;
        }

        int jumlahTotal = 0;
        if (!TextUtils.isEmpty(jumlahString)) {
            jumlahTotal = Integer.parseInt(jumlahString);
        }

        ContentValues values = new ContentValues();
        values.put(Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL, tanggalString);
        values.put(Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN, keteranganString);
        values.put(Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS, mStatus);
        values.put(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH, jumlahTotal);

        if (mCurrentKeuanganUri == null){
            Uri newUri = getContentResolver().insert(Contract.KeuanganEntry.CONTENT_URI, values);

            if (newUri == null){
                Toast.makeText(this, getString(R.string.insert_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_success), Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowAffected = getContentResolver().update(mCurrentKeuanganUri, values, null, null);

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
        if (mCurrentKeuanganUri == null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveKeuangan();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mKeuanganHasChanged) {
                    NavUtils.navigateUpFromSameTask(FormKeuangan.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(FormKeuangan.this);
                    }
                };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mKeuanganHasChanged){
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
                Contract.KeuanganEntry._ID,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN,
                Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS
        };
        return new CursorLoader(this,
                mCurrentKeuanganUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null && cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()){
            int tanggalColumnIndex = cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL);
            int jumlahColumnIndex = cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH);
            int keteranganColumnIndex = cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN);
            int statusColumnIndex = cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS);

            String StringTanggal = cursor.getString(tanggalColumnIndex);
            String StringKeterangan = cursor.getString(keteranganColumnIndex);
            int IntJumlah = cursor.getInt(jumlahColumnIndex);
            int IntStatus = cursor.getInt(statusColumnIndex);

            mTanggalEditText.setText(StringTanggal);
            mKeteranganEditText.setText(StringKeterangan);
            mJumlahEditText.setText(Integer.toString(IntJumlah));

            switch (IntStatus) {
                case Contract.KeuanganEntry.KEUANGAN_PEMASUKKAN:
                    mStatusSpinner.setSelection(1);
                    break;
                case Contract.KeuanganEntry.KEUANGAN_PENGELUARAN:
                    mStatusSpinner.setSelection(2);
                default:
                    mStatusSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mTanggalEditText.setText("");
        mJumlahEditText.setText("");
        mKeteranganEditText.setText("");
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
                deleteDataKeuangan();
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

    private void deleteDataKeuangan(){
        if (mCurrentKeuanganUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentKeuanganUri, null, null);

            if (rowsDeleted == 0){
                Toast.makeText(this, getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
