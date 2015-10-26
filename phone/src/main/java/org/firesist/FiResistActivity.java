package org.firesist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FiResistActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.firesist_layout);
    }

    @Override
    public void onStart() {
	super.onStart();
	TextView textView = (TextView) findViewById(R.id.text_view);
	textView.setText("Hello");
    }



    
    
}
