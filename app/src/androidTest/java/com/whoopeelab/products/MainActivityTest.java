package com.whoopeelab.products;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextMenu;

/**
 * Created by grishmashah on 9/22/14.
 */
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private MainActivity mainActivity;
    private Intent mLaunchIntent;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
                MainActivity.class);
        startActivity(mLaunchIntent, null, null);
        mainActivity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SmallTest
    public void testLayoutExists() {
        // Verifies the button and text field exist
        assertNotNull(mainActivity.findViewById(R.id.list_products));
    }
}
