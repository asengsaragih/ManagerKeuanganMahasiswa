package id.d3ifcool.managerkeuanganmahasiswa;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class KeuanganActivityTest {

    @Rule
    public ActivityTestRule<KeuanganActivity> keuanganActivityActivityTestRule =
            new ActivityTestRule<>(KeuanganActivity.class);

    @Test
    public void tambahdata() {
        onView(withId(R.id.fab_keuangan)).perform(click());

    }
}
