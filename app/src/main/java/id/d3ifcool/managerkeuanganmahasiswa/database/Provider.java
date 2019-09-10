package id.d3ifcool.managerkeuanganmahasiswa.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class Provider extends ContentProvider {
    public static final String LOG_TAG = Provider.class.getSimpleName();

    private static final int USER = 400;
    private static final int KEUANGAN = 100;
    private static final int HUTANG = 200;
    private static final int WISHLIST = 300;

    private static final int USER_ID = 401;
    private static final int KEUANGAN_ID = 101;
    private static final int HUTANG_ID = 201;
    private static final int WISHLIST_ID = 301;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_USER, USER);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_KEUANGAN, KEUANGAN);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_HUTANG, HUTANG);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_WISHLIST, WISHLIST);

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_USER + "/#", USER_ID);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_KEUANGAN + "/#", KEUANGAN_ID);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_HUTANG + "/#", HUTANG_ID);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_WISHLIST + "/#", WISHLIST_ID);
    }

    private Helper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new Helper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case WISHLIST:
                cursor = database.query(Contract.WishlistEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case KEUANGAN:
                cursor = database.query(Contract.KeuanganEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HUTANG:
                cursor = database.query(Contract.HutangEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER:
                cursor = database.query(Contract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case WISHLIST_ID:
                selection = Contract.WishlistEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(Contract.WishlistEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case KEUANGAN_ID:
                selection = Contract.KeuanganEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(Contract.KeuanganEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HUTANG_ID:
                selection = Contract.HutangEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(Contract.HutangEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER_ID:
                selection = Contract.UserEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(Contract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType( Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WISHLIST:
                return Contract.WishlistEntry.CONTENT_LIST_TYPE;
            case KEUANGAN:
                return Contract.KeuanganEntry.CONTENT_LIST_TYPE;
            case HUTANG:
                return Contract.HutangEntry.CONTENT_LIST_TYPE;
            case USER:
                return Contract.UserEntry.CONTENT_LIST_TYPE;
            case  WISHLIST_ID:
                return Contract.WishlistEntry.CONTENT_ITEM_TYPE;
            case  KEUANGAN_ID:
                return Contract.KeuanganEntry.CONTENT_ITEM_TYPE;
            case  HUTANG_ID:
                return Contract.HutangEntry.CONTENT_ITEM_TYPE;
            case  USER_ID:
                return Contract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }



    @Override
    public Uri insert( Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WISHLIST:
                return insertWishlist(uri, values);
            case KEUANGAN:
                return insertKeuangan(uri, values);
            case HUTANG:
                return insertHutang(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertKeuangan(Uri uri, ContentValues values){
        String tanggal = values.getAsString(Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL);
        if (tanggal == null) {
            throw new IllegalArgumentException("Masukkan Tanggal Yang Valid");
        }

        Integer jumlah = values.getAsInteger(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH);
        if (jumlah != null && jumlah < 0){
            throw new IllegalArgumentException("Masukkan Jumlah Yang Valid");
        }

        String keterangan = values.getAsString(Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN);
        if (keterangan == null) {
            throw new IllegalArgumentException("Masukkan Keterangan Yang Valid");
        }

        Integer status = values.getAsInteger(Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS);
        if (status == null ||!Contract.KeuanganEntry.isValidStatusKeuangan(status)){
            throw new IllegalArgumentException("Masukkan Status Yang Valid");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Contract.KeuanganEntry.TABLE_NAME, null, values);

        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertHutang(Uri uri, ContentValues values){
        String tanggal = values.getAsString(Contract.HutangEntry.COLUMN_HUTANG_TANGGAL);
        if (tanggal == null){
            throw new IllegalArgumentException("Masukkan Tanggal Yang Valid");
        }

        Integer jumlah = values.getAsInteger(Contract.HutangEntry.COLUMN_HUTANG_JUMLAH);
        if (jumlah != null && jumlah < 0){
            throw new IllegalArgumentException("Masukkan Jumlah Yang Valid");
        }

        String nama = values.getAsString(Contract.HutangEntry.COLUMN_HUTANG_NAMA);
        if (nama == null){
            throw new IllegalArgumentException("Masukkan Nama Yang Valid");
        }

        Integer status = values.getAsInteger(Contract.HutangEntry.COLUMN_HUTANG_STATUS);
        if (status == null ||!Contract.HutangEntry.isValidStatusHutang(status)){
            throw new IllegalArgumentException("Masukkan Status Yang Valid");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Contract.HutangEntry.TABLE_NAME, null, values);

        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertWishlist(Uri uri, ContentValues values){
        String name = values.getAsString(Contract.WishlistEntry.COLUMN_WISHLIST_NAMA);
        if (name == null) {
            throw new IllegalArgumentException("Wishlist requires a name");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Contract.WishlistEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match){
            case WISHLIST:
                rowsDeleted = database.delete(Contract.WishlistEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case KEUANGAN:
                rowsDeleted = database.delete(Contract.KeuanganEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HUTANG:
                rowsDeleted = database.delete(Contract.HutangEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case WISHLIST_ID:
                selection = Contract.WishlistEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Contract.WishlistEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case KEUANGAN_ID:
                selection = Contract.KeuanganEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Contract.KeuanganEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HUTANG_ID:
                selection = Contract.HutangEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Contract.HutangEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WISHLIST:
                return updateWishlist(uri, values, selection, selectionArgs);
            case KEUANGAN:
                return updateKeuangan(uri, values, selection, selectionArgs);
            case HUTANG:
                return  updateHutang(uri, values, selection, selectionArgs);
            case WISHLIST_ID:
                selection = Contract.WishlistEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateWishlist(uri, values, selection, selectionArgs);
            case KEUANGAN_ID:
                selection = Contract.KeuanganEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateKeuangan(uri, values, selection, selectionArgs);
            case HUTANG_ID:
                selection = Contract.HutangEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateHutang(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateKeuangan(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH)){
            Integer jumlah = values.getAsInteger(Contract.KeuanganEntry.COLUMN_KEUANGAN_JUMLAH);
            if (jumlah != null && jumlah < 0){
                throw new IllegalArgumentException("Masukkan Jumlah Yang Valid");
            }
        }

        if (values.containsKey(Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN)){
            String keterangan = values.getAsString(Contract.KeuanganEntry.COLUMN_KEUANGAN_KETERANGAN);
            if (keterangan == null){
                throw new IllegalArgumentException("Masukkan Keterangan Yang Valid");
            }
        }

        if (values.containsKey(Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL)){
            String tanggal  = values.getAsString(Contract.KeuanganEntry.COLUMN_KEUANGAN_TANGGAL);
            if (tanggal == null){
                throw new IllegalArgumentException("Masukkan Tanggal Yang Valid");
            }
        }

        if (values.containsKey(Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS)){
            Integer status = values.getAsInteger(Contract.KeuanganEntry.COLUMN_KEUANGAN_STATUS);
            if (status == null || !Contract.KeuanganEntry.isValidStatusKeuangan(status)){
                throw new IllegalArgumentException("Masukkan Status Yang Valid");
            }
        }

        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(Contract.KeuanganEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private int updateHutang(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(Contract.HutangEntry.COLUMN_HUTANG_TANGGAL)){
            String tanggal = values.getAsString(Contract.HutangEntry.COLUMN_HUTANG_TANGGAL);
            if (tanggal == null){
                throw new IllegalArgumentException("Masukkan Tanggal Yang Valid");
            }
        }

        if (values.containsKey(Contract.HutangEntry.COLUMN_HUTANG_JUMLAH)){
            Integer jumlah = values.getAsInteger(Contract.HutangEntry.COLUMN_HUTANG_JUMLAH);
            if (jumlah != null && jumlah < 0){
                throw new IllegalArgumentException("Masukkan Jumlah Yang Valid");
            }
        }

        if (values.containsKey(Contract.HutangEntry.COLUMN_HUTANG_STATUS)){
            Integer status = values.getAsInteger(Contract.HutangEntry.COLUMN_HUTANG_STATUS);
            if (status == null || !Contract.HutangEntry.isValidStatusHutang(status)){
                throw new IllegalArgumentException("Masukkan Status Yang Valid");
            }
        }

        if (values.containsKey(Contract.HutangEntry.COLUMN_HUTANG_NAMA)){
            String nama = values.getAsString(Contract.HutangEntry.COLUMN_HUTANG_NAMA);
            if (nama == null){
                throw new IllegalArgumentException("Masukkan Nama Yang Valid");
            }
        }

        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(Contract.HutangEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private int updateWishlist(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(Contract.WishlistEntry.COLUMN_WISHLIST_NAMA)) {
            String name = values.getAsString(Contract.WishlistEntry.COLUMN_WISHLIST_NAMA);
            if (name == null) {
                throw new IllegalArgumentException("Wishlist requires a name");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(Contract.WishlistEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

}
