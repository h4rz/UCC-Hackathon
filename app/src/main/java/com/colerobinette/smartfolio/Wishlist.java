package com.colerobinette.smartfolio;

/**
 * Created by YASH on 09-06-2018.
 */

import android.os.Bundle;
import android.app.Activity;

public class Wishlist extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.wishlist);
    }
}
