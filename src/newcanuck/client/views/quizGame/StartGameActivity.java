package newcanuck.client.views.quizGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import newcanuck.client.localDatabase.datasource.QuizGameDatasource;
import newcanuck.client.network.datasource.QuizQuestionNetworkDatasource;
import newcanuck.client.userProfile.UserProfile;
import newcanuck.client.views.HomeActivity;
import newcanuck.client.views.R;
import newcanuck.client.views.R.layout;
import newcanuck.client.views.R.menu;
import newcanuck.client.views.mission.FeelingActivity;
import newcanuck.entity.Comment;
import newcanuck.entity.QuizQuestion;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class StartGameActivity extends Activity {

	protected static final int UPLOAD_QUESTION = 0;
	//widgets
	private ArrayList<CheckBox> checkBoxGroupOfQuestionTypes;
	private Button btnStartGame;
	private TextView textViewNumberOfQuestions;
	private SeekBar seekBarNumberOfQuestions;
	private int numberOfQuestions;
	private final int MAX_NUM_QUESTIONS = 40;
	private Button btnUpdateQuesions;
	private View btnUploadQuestion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_game);
		
		initWidgets();
	}

	private void initWidgets() {
		initBtnStartGame();
		initUpdateQuestionsButton();
		initUploadQuestionButton();
		initCheckBoxGroupOfQuestionTypes();
		initNumberOfQuestions();
	}

	private void initUploadQuestionButton() {
		btnUploadQuestion = (Button)findViewById(R.id.buttonUploadQuesion);
		btnUploadQuestion.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(StartGameActivity.this, UploadQuestionActivity.class), UPLOAD_QUESTION);
			}
        });
	}

	
	ProgressDialog dialog;
	private void initUpdateQuestionsButton() {
		btnUpdateQuesions = (Button)findViewById(R.id.buttonUpdateQuesions);
		btnUpdateQuesions.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				StartGameActivity.this.setTitle("Loading Questions...");

				dialog = ProgressDialog.show(StartGameActivity.this, "Loading Questions...", 
		                "Loading. Please wait...", true);
				new GetQuizQuestionsASyncTask().execute();
			}
        });
		
	}
	

	public class GetQuizQuestionsASyncTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {

		String result;
		
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			QuizQuestionNetworkDatasource quizQuestionNetworkDatasource = new QuizQuestionNetworkDatasource();
			List<QuizQuestion> quizQuesions = quizQuestionNetworkDatasource.getAllQuestionssFromServer();
			if(quizQuesions == null || quizQuesions.size()==0) {
				result = "Internet not available.";
			}
			else {
				QuizGameDatasource quizQuestionDatasource = new QuizGameDatasource(StartGameActivity.this);
				quizQuestionDatasource.updateAllQuetions(quizQuesions);
				
				result = "Update quiz quesitions successfully!";
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> out) {
			//update widget
			Toast.makeText(StartGameActivity.this, (CharSequence) result, Toast.LENGTH_SHORT).show();

			dialog.cancel();
			StartGameActivity.this.setTitle("Start Game");
		}
	}

	private void initNumberOfQuestions() {
		textViewNumberOfQuestions = (TextView)findViewById(R.id.textViewNumOfQuestions);
		seekBarNumberOfQuestions = (SeekBar)findViewById(R.id.seekBarNumOfQuestions);
		
		UserProfile userProfile = UserProfile.getProfile(this);
		seekBarNumberOfQuestions.setMax(MAX_NUM_QUESTIONS);
		
		numberOfQuestions = userProfile.getNumOfQuestions();
		seekBarNumberOfQuestions.setProgress(numberOfQuestions);
		textViewNumberOfQuestions.setText("Number Of Questions: "+numberOfQuestions);
		
		seekBarNumberOfQuestions.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
				numberOfQuestions = progress;
				textViewNumberOfQuestions.setText("Number Of Questions: "+numberOfQuestions);
			}

			@Override
			public void onStartTrackingTouch(SeekBar s) {
				return;
			}

			@Override
			public void onStopTrackingTouch(SeekBar s) {
				return;
			}
			
		});
	}

	private void initCheckBoxGroupOfQuestionTypes() {
		checkBoxGroupOfQuestionTypes = new ArrayList<CheckBox>();
		UserProfile userProfile = UserProfile.getProfile(this); 
		
		String[] user_types = userProfile.getQuestionTypes();
		String[] all_types = userProfile.getAllTypes();
		
		LinearLayout leftGroup = (LinearLayout)findViewById(R.id.leftCheckBoxGroup);
		LinearLayout rightGroup = (LinearLayout)findViewById(R.id.rightCheckBoxGroup);
		
		for(int i=0; i<all_types.length; i++){
			CheckBox checkBox = new CheckBox(this);
			checkBox.setText(all_types[i]);
			
			boolean leftOrRight = (i%2 == 0);
			if(leftOrRight) {
				leftGroup.addView(checkBox);
			}
			else {
				rightGroup.addView(checkBox);
			}
			
			checkBox.setChecked(false);
			for(int j=0; j<user_types.length; j++) {
				if(all_types[i].equals(user_types[j])) {
					checkBox.setChecked(true);
				}
			}
			
			checkBoxGroupOfQuestionTypes.add(checkBox);
		}
	}

	private void initBtnStartGame() {
		btnStartGame = (Button)findViewById(R.id.buttonStart);
		btnStartGame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				storeQuestionTypesSetting();
				startActivityForResult(new Intent(StartGameActivity.this, QuizGameActivity.class),0);
			}

			private void storeQuestionTypesSetting() {
				UserProfile userProfile = UserProfile.getProfile(StartGameActivity.this); 
				
				ArrayList<String> questionTypes = new ArrayList<String>();
				for(int i=0; i<checkBoxGroupOfQuestionTypes.size(); i++) {
					CheckBox c = checkBoxGroupOfQuestionTypes.get(i);
					if(c.isChecked()) {
						questionTypes.add(c.getText().toString());
					}
				}
				
				Object[] arrObjectTypes = questionTypes.toArray();
				String[] arrStrTypes = Arrays.copyOf(arrObjectTypes, arrObjectTypes.length, String[].class);
				userProfile.setQuestionTypes(arrStrTypes);
				
				userProfile.setNumOfQuestions(numberOfQuestions);
			}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start_game, menu);
		return true;
	}

}
