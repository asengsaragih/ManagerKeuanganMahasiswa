package id.d3ifcool.managerkeuanganmahasiswa;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FormWishlistTest {

    @Rule
    public ActivityTestRule<FormWishlist> formWishlistActivityTestRule =
            new ActivityTestRule<>(FormWishlist.class);

    @Test
    public void test_tombol_tambah_wishlist() {
        onView(withId(R.id.editText_wishlist_nama)).perform(typeText("Shampoo"));
        onView(withId(R.id.action_save)).perform(click());
    }
}