package id.d3ifcool.managerkeuanganmahasiswa.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper {
    public static final String LOG_TAG = Helper.class.getSimpleName();
    private static final String DATABASE_NAME = "ManagerKeuanganMahasiswa.db";
    private static final int DATABASE_VERSION = 1;

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //saya merubah helper untuk medapatkan password dan saldo
    private ContentValues values = new ContentValues();

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_WISHLIST_TABLE = "CREATE TABLE " + Contract.WishlistEntry.TABLE_NAME + " ("
                + Contract.WishlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.WishlistEntry.COLUMN_WISHLIST_NAMA + " TEXT NOT NULL);";

        String SQL_CREATE_HUTANG_TABLE = "CREATE TABLE " + Contract.HutangEntry.TABLE_NAME + " ("
                + Contract.HutangEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.HutangEntry.COLUMN_HUTANG_TANGGAL + " TEXT NOT NULL, "
                + Contract.HutangEntry.COLUMN_HUTANG_JUMLAH + " INTEGER NOT NULL DEFAULT 0, "
                + Contract.HutangEntry.COLUMN_HUTANG_STATUS + " INTEGER NOT NULL, "
                + Contract.HutangEntry.COLUMN_HUTANG_NAMA + " TEXT NOT NULL);";

        String SQL_CREATE_KEUANGAN_TABLE = "CREATE TABLE " + Contract.KeuanganEntry.TABLE_NAME + " ("
                + Contract.KeuanganEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH + " INTEGER NOT NULL DEFAULT 0, "
                + Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN + " TEXT, "
                + Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL + " TEXT NOT NULL, "
                + Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS + " INTEGER NOT NULL);";

        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + Contract.UserEntry.TABLE_NAME + " ("
                + Contract.UserEntry.COLUMN_USER_PASSWORD + " TEXT, "
                + Contract.UserEntry.COLUMN_USER_SALDO + " INTEGER NOT NULL DEFAULT 0);";

        values.put(Contract.UserEntry.COLUMN_USER_PASSWORD, "12345678");
        values.put(Contract.UserEntry.COLUMN_USER_SALDO, "0");

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_KEUANGAN_TABLE);
        db.execSQL(SQL_CREATE_HUTANG_TABLE);
        db.execSQL(SQL_CREATE_WISHLIST_TABLE);
        db.insert(Contract.UserEntry.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
