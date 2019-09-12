package id.d3ifcool.managerkeuanganmahasiswa;

import android.app.DatePickerDialog;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class FormKeuanganTest {
    @Rule
    public ActivityTestRule<FormKeuangan> formKeuanganActivityTestRule =
            new ActivityTestRule<>(FormKeuangan.class);


//    public static Matcher<View> matchDate(final int year, final int month, final int day) {
//        return new BoundedMatcher<View, DatePicker>(DatePicker.class) {
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("matches date:");
//            }
//
//            @Override
//            protected boolean matchesSafely(DatePicker item) {
//                return (year == item.getYear() && month == item.getMonth() && day == item.getDayOfMonth());
//            }
//        };
//
//    }

    @Test
    public void tambah_data_keuangan() {
        onView(withId(R.id.editText_tanggal_keuangan)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 10 + 1, 12));
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(click());
        onView(withId(android.R.id.button1)).perform(click());


        onView(withId(R.id.editText_jumlah_keuangan)).perform(typeText("50000"));
        onView(withId(R.id.editText_keterangan_keuangan)).perform(typeText("Makan Siang"));
        onView(withId(R.id.action_save)).perform(click());
    }
}
