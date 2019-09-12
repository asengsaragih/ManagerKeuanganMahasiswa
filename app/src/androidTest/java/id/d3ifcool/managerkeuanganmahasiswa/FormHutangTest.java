package id.d3ifcool.managerkeuanganmahasiswa;

import android.app.DatePickerDialog;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FormHutangTest {

    @Rule
    public ActivityTestRule<FormHutang> formHutangTestRule =
            new ActivityTestRule<>(FormHutang.class);

    @Test
    public void tes_tombol_tambah_data() {
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 11));
//        onView(withId(R.id.editText_tanggal_hutang)).perform(click(PickerActions.setDate(2019, 11, 11)));

        onView(withId(R.id.editText_tanggal_hutang)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 10 + 1, 12));
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.editText_jumlah_hutang)).perform(typeText("50000"));
        onView(withId(R.id.editText_nama_hutang)).perform(typeText("Aseng"));
        onView(withId(R.id.action_save)).perform(click());
    }
}