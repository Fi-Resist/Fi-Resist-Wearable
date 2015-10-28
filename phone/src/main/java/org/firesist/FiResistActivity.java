package org.firesist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;

public class FiResistActivity extends Activity {
	private Socket socket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firesist_layout);
		try {
			socket = IO.socket(getString(R.string.host));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		socket.connect();
		socket.emit(getString(R.string.connect), "hi there");
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		socket.disconnect();
	}



}
