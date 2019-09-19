package id.d3ifcool.managerkeuanganmahasiswa;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AllActivityTest {
    @Rule
    public ActivityTestRule<KeuanganActivity> keuanganActivityActivityTestRule =
            new ActivityTestRule<>(KeuanganActivity.class);

    @Test
    public void testKeuanganAll() {
        onView(withId(R.id.fab_keuangan)).perform(click());
        onView(withId(R.id.editText_tanggal_keuangan)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 9, 19));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.editText_jumlah_keuangan)).perform(typeText("50000"));
        onView(withId(R.id.editText_keterangan_keuangan)).perform(typeText("Makan Siang"));
        onView(withId(R.id.action_save)).perform(click());

        //test masuk hutang
        onView(withId(R.id.drawer_keuangan))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nv_keuangan))
                .perform(NavigationViewActions.navigateTo(R.id.hutang));
        onView(withText(R.string.meminjam)).perform(click());
        onView(withText(R.string.dipinjamkan)).perform(click());

        //test input utang
        onView(withId(R.id.fab_dipinjamkan)).perform(click());
        onView(withId(R.id.editText_tanggal_hutang)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 9, 19));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.editText_jumlah_hutang)).perform(typeText("50000"));
        onView(withId(R.id.editText_nama_hutang)).perform(typeText("Devi"));
        onView(withId(R.id.action_save)).perform(click());

        //tes masuk ke wishlist
        onView(withId(R.id.drawer_hutang))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nv_hutang))
                .perform(NavigationViewActions.navigateTo(R.id.wishlist));

//        //tes input wishlist
        onView(withId(R.id.fab_wishlist)).perform(click());
        onView(withId(R.id.editText_wishlist_nama)).perform(typeText("Shampoo Kuda"));
        onView(withId(R.id.action_save)).perform(click());

        //test masuk ke pengaturan
        onView(withId(R.id.drawer_wishlist))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nv_wishlist))
                .perform(NavigationViewActions.navigateTo(R.id.setting));

        //tes masuk ke tentang aplikasi
        onView(withId(R.id.textView_Tentang_Aplikasi)).perform(click());
    }
}
