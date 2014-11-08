package mralucp.game.androvader;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		MainGamePanel R = new MainGamePanel(this);
		setContentView(new MainGamePanel(this));
		showSimplePopUp(R.getpoints());

	}
/*
 * onStart()
 * public void onRestoreInstanceState(Bundle savedInstanceState)
 * onStop()
 * public Bundle saveState()
 * 
 * */
	private void showSimplePopUp(int i) {

		 AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		 helpBuilder.setTitle("Your score");
		 helpBuilder.setMessage("Score: "+i);
		 helpBuilder.setPositiveButton("Ok",
		   new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		     // Do nothing but close the dialog
		    }
		   });

		 // Remember, create doesn't show the dialog
		 AlertDialog helpDialog = helpBuilder.create();
		 helpDialog.show();
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onDestroy()
	{
		super.onDestroy();
	}
}
