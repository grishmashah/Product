package com.whoopeelab.products;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by grishmashah on 9/22/14.
 */
public class AddProductActivityTest extends ActivityInstrumentationTestCase2<AddProductActivty> {

    private AddProductActivty addProductActivty;
    private Button addProductBtn;

    public AddProductActivityTest() {
        super(AddProductActivty.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        addProductActivty = getActivity();

        //Get references to all views
        addProductBtn = (Button) addProductActivty.findViewById(R.id.btn_add);
    }

    @MediumTest
    public void testStartSecondActivity() throws Exception {
        final String fieldValue = "Logitech Mouse";

        // Set a value into the text field
        final TextView etResult = (TextView) addProductActivty.findViewById(R.id.txt_product_name);
        final CheckBox checkBox = (CheckBox) addProductActivty.findViewById(R.id.check_product);
        addProductActivty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkBox.setChecked(true);
                etResult.setText(fieldValue);
            }
        });

        // Add monitor to check for the second activity
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(
                MainActivity.class.getName(), null, false);

        TouchUtils.clickView(this, addProductBtn);

        // Wait 2 seconds for the start of the activity
        MainActivity secondActivity = (MainActivity) monitor
                .waitForActivityWithTimeout(2000);
        assertNotNull(secondActivity);

        // Search for the textView
        TextView textView = (TextView) secondActivity
                .findViewById(R.id.txt_prod);

        // check that the TextView is on the screen
        ViewAsserts.assertOnScreen(secondActivity.getWindow().getDecorView(),
                textView);

        // Validate the text on the TextView
        assertEquals("Text should be the field value", fieldValue, textView
                .getText().toString());

        // Press back to return to original activity
        // We have to manage the initial position within the emulator
        this.sendKeys(KeyEvent.KEYCODE_BACK);
    }


}
