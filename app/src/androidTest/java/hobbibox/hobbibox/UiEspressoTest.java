package hobbibox.hobbibox;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hobbibox.hobbibox.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UiEspressoTest extends BaseActivityEspressoTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    // Check to make sure all UI elements are functional and populating properly
    // Views are checked within test due to emulator lag

    @Test
    public void testUiElements() {
        // Wait 15 seconds to allow the app to start up
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_page_icon)).perform(click());

        // Main navigation
        onView(withId(R.id.profile_username_preview_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_phone_number_preview_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_subscription_settings_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sub_settings_choice_preview_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.payment_info_card_type_preview_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.business_verified_preview_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sign_out_button)).check(matches(isDisplayed()));

        // Phone number
        onView(withId(R.id.profile_phone_number_rel_layout)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_phone_number_title_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_phone_number_edit_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_phone_number_edit_text)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.left_corner_icon),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());

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

        onView(withId(R.id.profile_subscription_settings_title_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_subscription_settings_weekly_radioButton)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_subscription_settings_biweekly_radioButton)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_subscription_settings_monthly_radioButton)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_subscription_settings_bimonthly_radioButton)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_subscription_settings_trimonthly_radioButton)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_subscription_settings_halfyear_radioButton)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sub_settings_weekly_price)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sub_settings_biweekly_price)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sub_settings_monthly_price)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sub_settings_bimonthly_price)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sub_settings_trimonthly_price)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sub_settings_halfyear_price)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_sub_settings_edit_image_view)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.left_corner_icon),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());

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

        onView(withId(R.id.profile_shipping_info_title_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_first_name_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_last_name_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_street_address1_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_street_address2_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_city_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_state_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_country_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_zipcode_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_shipping_info_edit_image_view)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.left_corner_icon),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());

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

        onView(withId(R.id.profile_billing_info_title_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_first_name_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_last_name_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_street_address1_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_street_address2_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_city_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_state_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_country_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_zipcode_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_billing_info_edit_image_view)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.left_corner_icon),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Payment
        onView(withId(R.id.profile_payment_info_rel_layout)).perform(click());

        // Wait a second to allow the emulator to catch up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.profile_payment_info_title_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_card_type_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_credit_card_button)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_debit_card_button)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_card_company_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_card_company_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_nameOnCard_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_nameOnCard_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_cardnumber_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_cardnumber_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_security_code_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_security_code_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_expiration_date_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_payment_info_edit_image_view)).check(matches(isDisplayed()));
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
