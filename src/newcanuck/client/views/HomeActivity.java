package newcanuck.client.views;

import newcanuck.client.views.mission.MissionActivity;
import newcanuck.client.views.quizGame.StartGameActivity;
import newcanuck.client.views.record.RecordActivity;
import newcanuck.client.views.setting.SettingActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {

	//buttons
	Button btnTourMission = null;
	Button btnQuizGame = null;
	Button btnRecord = null;
	Button btnSetting = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        //load widgets
        btnTourMission = (Button)findViewById(R.id.btnTourMission);
        btnTourMission.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(HomeActivity.this, MissionActivity.class),0);
			}
        	
        });
        
        btnQuizGame = (Button)findViewById(R.id.btnQuizGame);
        btnQuizGame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(HomeActivity.this, StartGameActivity.class),1);
			}
        	
        });
        
        btnRecord =(Button)findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new OnClickListener (){
        	
        	@Override
        	public void onClick(View arg0){
        		startActivityForResult(new Intent(HomeActivity.this, RecordActivity.class),2);
        	}
        	
        });
        
        btnSetting = (Button)findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new OnClickListener (){
        	
        	@Override
        	public void onClick(View arg0){
        		startActivityForResult(new Intent(HomeActivity.this, SettingActivity.class),2);
        	}
        	
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
}
