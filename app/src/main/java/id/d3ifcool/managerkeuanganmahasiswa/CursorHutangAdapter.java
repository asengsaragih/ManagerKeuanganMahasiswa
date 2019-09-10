package id.d3ifcool.managerkeuanganmahasiswa;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import id.d3ifcool.managerkeuanganmahasiswa.database.Contract;

public class CursorHutangAdapter extends CursorAdapter {
    public CursorHutangAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_hutang, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_nama = (TextView) view.findViewById(R.id.textView_hutamg_nama);
        tv_nama.setText(cursor.getString(cursor.getColumnIndex(Contract.HutangEntry.COLUMN_HUTANG_NAMA)));

        TextView tv_total = (TextView) view.findViewById(R.id.textView_hutang_jumlah);
        tv_total.setText(cursor.getString(cursor.getColumnIndex(Contract.HutangEntry.COLUMN_HUTANG_JUMLAH)));
    }
}
