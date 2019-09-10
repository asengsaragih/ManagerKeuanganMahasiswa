package id.d3ifcool.managerkeuanganmahasiswa;

import android.content.Context;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import id.d3ifcool.managerkeuanganmahasiswa.database.Contract;

public class CursorKeuanganAdapter extends CursorAdapter {

    public CursorKeuanganAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_keuangan, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraint_item_keuangan);

        String StatusKeuangan = cursor.getString(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS));
        TextView tv_total_list_keuangan = (TextView) view.findViewById(R.id.textView_total_keuangan);

        if (StatusKeuangan.equals("1")) {
            constraintLayout.setBackgroundResource(R.color.white);
            tv_total_list_keuangan.setText("+ "+cursor.getString(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH))+" IDR");
        } else if (StatusKeuangan.equals("2")){
            constraintLayout.setBackgroundResource(R.color.red_mix_yellow);
            tv_total_list_keuangan.setText("- "+cursor.getString(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH))+" IDR");
        } else {
            constraintLayout.setBackgroundResource(R.color.design_default_color_primary);
            tv_total_list_keuangan.setText("? "+cursor.getString(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH))+" IDR");
        }




        TextView tv_keterangan_list_keuangan = (TextView) view.findViewById(R.id.textView_keterangan_keuangan);
        tv_keterangan_list_keuangan.setText(cursor.getString(cursor.getColumnIndex(Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN)));
    }
}
