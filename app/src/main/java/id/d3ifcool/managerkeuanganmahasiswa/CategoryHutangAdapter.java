package id.d3ifcool.managerkeuanganmahasiswa;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

public class CategoryHutangAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryHutangAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0){
            return new DipinjamkanFragment();
        } else {
            return new MeminjamFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        if (position == 0){
            return mContext.getString(R.string.dipinjamkan);
        } else {
            return mContext.getString(R.string.meminjam);
        }
    }
}
