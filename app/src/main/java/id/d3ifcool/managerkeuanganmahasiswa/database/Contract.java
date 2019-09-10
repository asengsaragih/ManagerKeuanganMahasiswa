package id.d3ifcool.managerkeuanganmahasiswa.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    private Contract() {}

    public static final String CONTENT_AUTHORITY = "id.d3ifcool.managerkeuanganmahasiswa";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_KEUANGAN = "keuangan";
    public static final String PATH_HUTANG = "hutang";
    public static final String PATH_WISHLIST = "wishlist";

    public static final class UserEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public final static String TABLE_NAME = "user";

        public final static String COLUMN_USER_PASSWORD = "user_password";
        public final static String COLUMN_USER_SALDO = "user_saldo";
    }

    public static final class KeuanganEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_KEUANGAN);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_KEUANGAN;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_KEUANGAN;


        public final static String TABLE_NAME = "keuangan";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_KEUANGAN_TANGGAL = "keuangan_tanggal";
        public final static String COLUMN_KEUANGAN_JUMLAH = "keuangan_jumlah";
        public final static String COLUMN_KEUANGAN_KETERANGAN = "keuangan_keterangan";
        public final static String COLUMN_KEUANGAN_STATUS = "keuangan_status";

        public static final int KEUANGAN_UNKNOW = 0;
        public static final int KEUANGAN_PEMASUKKAN = 1;
        public static final int KEUANGAN_PENGELUARAN = 2;

        public static boolean isValidStatusKeuangan(int statusKeuangan){
            if (statusKeuangan == KEUANGAN_UNKNOW || statusKeuangan == KEUANGAN_PEMASUKKAN || statusKeuangan == KEUANGAN_PENGELUARAN){
                return true;
            }
            return false;
        }
    }

    public static final class HutangEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HUTANG);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HUTANG;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HUTANG;

        public final static String TABLE_NAME = "hutang";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HUTANG_TANGGAL = "hutang_tanggal";
        public final static String COLUMN_HUTANG_JUMLAH = "hutang_jumlah";
        public final static String COLUMN_HUTANG_NAMA = "hutang_nama";
        public final static String COLUMN_HUTANG_STATUS = "hutang_status";

        public static final int HUTANG_UNKNOW = 0;
        public static final int HUTANG_DIPINJAMKAN = 1;
        public static final int HUTANG_MEMINJAM = 2;

        public static boolean isValidStatusHutang(int statusHutang){
            if (statusHutang == HUTANG_UNKNOW || statusHutang == HUTANG_DIPINJAMKAN || statusHutang == HUTANG_MEMINJAM){
                return true;
            }
            return false;
        }
    }

    public static final class WishlistEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_WISHLIST);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WISHLIST;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WISHLIST;


        public final static String TABLE_NAME = "wishlist";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_WISHLIST_NAMA = "wishlist_nama";
    }
}
