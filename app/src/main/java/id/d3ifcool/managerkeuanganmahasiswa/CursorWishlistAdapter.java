package id.d3ifcool.managerkeuanganmahasiswa;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import id.d3ifcool.managerkeuanganmahasiswa.database.Contract;

public class CursorWishlistAdapter extends CursorAdapter {
    public CursorWishlistAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_wishlist, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView namaBarangTextView = (TextView) view.findViewById(R.id.textView_barang_wishlist);
        namaBarangTextView.setText(cursor.getString(cursor.getColumnIndex(Contract.WishlistEntry.COLUMN_WISHLIST_NAMA)));
    }
}
