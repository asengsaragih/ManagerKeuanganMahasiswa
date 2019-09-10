package id.d3ifcool.managerkeuanganmahasiswa;


import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.d3ifcool.managerkeuanganmahasiswa.database.Contract;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeminjamFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEMINJAM_LOADER = 0;
    private static final int RESTART_MEMINJAM_LOADER = 1;

    private CursorHutangAdapter mCursorAdapter;

    private TextView mDatePickerTextView;

    private final Calendar calendar = Calendar.getInstance();

    public MeminjamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meminjam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mDatePickerTextView = view.findViewById(R.id.tv_datpicker_meminjam);

        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton floatingActionButton = getView().findViewById(R.id.fab_meminjam);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FormHutang.class);
                startActivity(intent);
            }
        });

        ListView meminjamListVIew = (ListView) getView().findViewById(R.id.list_item_meminjam);

        View emptyView = getView().findViewById(R.id.empty_view_meminjam);
        meminjamListVIew.setEmptyView(emptyView);

        //kalaueror perbaiki di sini cursorwindownya
        mCursorAdapter = new CursorHutangAdapter(getActivity(), null);
        meminjamListVIew.setAdapter(mCursorAdapter);

        meminjamListVIew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FormHutang.class);
                Uri currentMeminjamUri = ContentUris.withAppendedId(Contract.HutangEntry.CONTENT_URI, id);
                intent.setData(currentMeminjamUri);
                startActivity(intent);
            }
        });

        dateTextView();
        datePicker();
        LoaderManager.getInstance(getActivity()).initLoader(MEMINJAM_LOADER, null, MeminjamFragment.this);
    }

    private void dateTextView(){
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String date_str = df.format(cal.getTime());
        mDatePickerTextView.setText(date_str);
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

        mDatePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date,  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDatePickerTextView.setText(sdf.format(calendar.getTime()));

        //ditambah juga disini laoder callback untuk restart loadernya, jadi setiap kita memilih tanggal loader harus direstart
        LoaderManager.getInstance(getActivity()).restartLoader(RESTART_MEMINJAM_LOADER, null, MeminjamFragment.this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                Contract.HutangEntry._ID,
                Contract.HutangEntry.COLUMN_HUTANG_JUMLAH,
                Contract.HutangEntry.COLUMN_HUTANG_NAMA,
                Contract.HutangEntry.COLUMN_HUTANG_STATUS
        };

        String clausaWhere = Contract.HutangEntry.COLUMN_HUTANG_TANGGAL+"=?"+" AND "+Contract.HutangEntry.COLUMN_HUTANG_STATUS+"=?";
        String dateWhere = (String) mDatePickerTextView.getText();
        String whereStatus = "2";
        String [] whereArgs = {
                dateWhere.toString(),
                whereStatus.toString()};
        return new CursorLoader(getActivity(),
                Contract.HutangEntry.CONTENT_URI,
                projection,
                clausaWhere,
                whereArgs,
                null);
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
