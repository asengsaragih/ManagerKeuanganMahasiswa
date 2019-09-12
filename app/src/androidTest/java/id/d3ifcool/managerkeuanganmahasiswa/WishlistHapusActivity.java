package id.d3ifcool.managerkeuanganmahasiswa;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class WishlistHapusActivity {
    @Rule
    public ActivityTestRule<WishlistActivity> wishlistActivityActivityTestRule =
            new ActivityTestRule<>(WishlistActivity.class);

    @Test
    public void test_tombol_hapus_wishlist() {
        onData(anything()).inAdapterView(withId(R.id.list_item_wishlist))
                .atPosition(0)
                .perform(click());
        onView(withId(R.id.editText_wishlist_nama)).perform(clearText());
        onView(withId(R.id.editText_wishlist_nama)).perform(typeText("sabun"));
        onView(withId(R.id.action_save)).perform(click());
    }
}
