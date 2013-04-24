package newcanuck.client.views.setting;

import newcanuck.client.userProfile.UserProfile;
import newcanuck.client.views.HomeActivity;
import newcanuck.client.views.R;
import newcanuck.client.views.R.layout;
import newcanuck.client.views.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingActivity extends Activity {

	private Button btnConfirm;
	private EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		editText = (EditText)findViewById(R.id.editTextUserName);
		editText.setText(UserProfile.getProfile(this).getUserName());
		
		btnConfirm = (Button)findViewById(R.id.buttonConfirm);
		btnConfirm.setOnClickListener(new OnClickListener (){
	        	
	        	@Override
	        	public void onClick(View arg0){
	        		String newUserName = editText.getText().toString();
	        		UserProfile.getProfile(SettingActivity.this).setUserName(newUserName);
	        		finish();
	        	}
	        	
	        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

}
