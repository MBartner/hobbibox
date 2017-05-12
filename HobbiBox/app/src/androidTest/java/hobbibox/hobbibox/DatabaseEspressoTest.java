package hobbibox.hobbibox;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hobbibox.hobbibox.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DatabaseEspressoTest extends BaseActivityEspressoTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    // Check to make sure all database functions are working properly
    // Change some info in the profile and make sure the information is updated from the query
    // Keyboard is closed after every typeText to make sure emulator can find each view

    @Test
    public void testProfileQuery() {
        // Wait 15 seconds to allow the app to start up
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_page_icon)).perform(click());

        // Phone number
        onView(withId(R.id.profile_phone_number_rel_layout)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_phone_number_edit_image_view)).perform(click());
        onView(withId(R.id.profile_phone_number_edit_text)).perform(clearText(), typeText("(123) 456 - 7890"));
        onView(withId(R.id.profile_phone_number_change_confirm_button)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Subscription
        onView(withId(R.id.profile_subscriptions_settings_rel_layout)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_sub_settings_edit_image_view)).perform(click());
        onView(withId(R.id.profile_subscription_settings_monthly_radioButton)).perform(click());
        onView(withId(R.id.profile_sub_settings_change_confirm_button)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shipping
        onView(withId(R.id.profile_shipping_info_rel_layout)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_shipping_info_edit_image_view)).perform(click());
        onView(withId(R.id.profile_shipping_first_name_edit_text)).perform(clearText(), typeText("Test 1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_shipping_last_name_edit_text)).perform(clearText(), typeText("Test 2"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_shipping_street_address1_edit_text)).perform(clearText(), typeText("Test 3"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_shipping_street_address2_edit_text)).perform(clearText(), typeText("Test 4"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_shipping_city_edit_text)).perform(clearText(), typeText("Test 5"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_shipping_state_edit_text)).perform(clearText(), typeText("Test 6"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_shipping_country_edit_text)).perform(clearText(), typeText("Test 7"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_shipping_zipcode_edit_text)).perform(scrollTo(), clearText(), typeText("12345"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_shipping_change_confirm_button)).perform(scrollTo(), click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Billing information
        onView(withId(R.id.profile_billing_info_rel_layout)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_billing_info_edit_image_view)).perform(click());
        onView(withId(R.id.profile_billing_first_name_edit_text)).perform(clearText(), typeText("Test 11"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_billing_last_name_edit_text)).perform(clearText(), typeText("Test 12"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_billing_street_address1_edit_text)).perform(clearText(), typeText("Test 13"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_billing_street_address2_edit_text)).perform(clearText(), typeText("Test 14"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_billing_city_edit_text)).perform(clearText(), typeText("Test 15"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_billing_state_edit_text)).perform(clearText(), typeText("Test 16"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_billing_country_edit_text)).perform(clearText(), typeText("Test 17"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_billing_zipcode_edit_text)).perform(clearText(), typeText("54321"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.profile_billing_change_confirm_button)).perform(scrollTo(), click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Sign out and sign back in
        onView(withId(R.id.profile_sign_out_button)).perform(click());
        onView(withId(R.id.signin_button)).perform(click());
        onView(withId(R.id.user_name_edit_text)).perform(typeText("test@test.com"));
        onView(withId(R.id.password_edit_text)).perform(typeText("testing"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_signin_button)).perform(click());

        // Wait 15 seconds to allow the app to start up
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check that info has been updated and loaded successfully
        onView(withId(R.id.profile_page_icon)).perform(click());
        onView(withId(R.id.profile_phone_number_preview_textview)).check(matches(withText("(123) 456 - 7890")));
        onView(withId(R.id.profile_sub_settings_choice_preview_textview)).check(matches(withText("Monthly")));
        onView(withId(R.id.profile_shipping_info_rel_layout)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_shipping_first_name_edit_text)).check(matches(withText("Test 1")));
        onView(withId(R.id.profile_shipping_last_name_edit_text)).check(matches(withText("Test 2")));
        onView(withId(R.id.profile_shipping_street_address1_edit_text)).check(matches(withText("Test 3")));
        onView(withId(R.id.profile_shipping_street_address2_edit_text)).check(matches(withText("Test 4")));
        onView(withId(R.id.profile_shipping_city_edit_text)).check(matches(withText("Test 5")));
        onView(withId(R.id.profile_shipping_state_edit_text)).check(matches(withText("Test 6")));
        onView(withId(R.id.profile_shipping_country_edit_text)).check(matches(withText("Test 7")));
        onView(withId(R.id.profile_shipping_zipcode_edit_text)).check(matches(withText("12345")));
        onView(allOf(withId(R.id.left_corner_icon),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_billing_info_rel_layout)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_billing_first_name_edit_text)).check(matches(withText("Test 11")));
        onView(withId(R.id.profile_billing_last_name_edit_text)).check(matches(withText("Test 12")));
        onView(withId(R.id.profile_billing_street_address1_edit_text)).check(matches(withText("Test 13")));
        onView(withId(R.id.profile_billing_street_address2_edit_text)).check(matches(withText("Test 14")));
        onView(withId(R.id.profile_billing_city_edit_text)).check(matches(withText("Test 15")));
        onView(withId(R.id.profile_billing_state_edit_text)).check(matches(withText("Test 16")));
        onView(withId(R.id.profile_billing_country_edit_text)).check(matches(withText("Test 17")));
        onView(withId(R.id.profile_billing_zipcode_edit_text)).check(matches(withText("54321")));
        onView(allOf(withId(R.id.left_corner_icon),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Activity getActivity() {
        return activityRule.getActivity();
    }
}
