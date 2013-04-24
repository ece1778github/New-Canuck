package newcanuck.client.views.quizGame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newcanuck.client.localDatabase.datasource.QuizGameDatasource;
import newcanuck.client.network.datasource.QuizQuestionNetworkDatasource;
import newcanuck.client.userProfile.UserProfile;
import newcanuck.client.views.R;
import newcanuck.client.views.quizGame.StartGameActivity.GetQuizQuestionsASyncTask;
import newcanuck.entity.QuizQuestion;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UploadQuestionActivity extends Activity {

	private Spinner spinnerType;
	private Spinner spinnerCorrectAnswer;
	private Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_question);
		
		spinnerType = (Spinner)findViewById(R.id.spinnerType);
		spinnerCorrectAnswer = (Spinner)findViewById(R.id.spinnerCorrectAnswer);
		
		String types [] = UserProfile.getProfile(this).getAllTypes();
		List<String> typesList = Arrays.asList(types);
		ArrayAdapter<String> typesDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typesList);
		typesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(typesDataAdapter);
		
		String answers [] = {"A","B","C","D"};
		List<String> answersList = Arrays.asList(answers);
		ArrayAdapter<String> answersDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, answersList);
		answersDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCorrectAnswer.setAdapter(answersDataAdapter);
		
		btnSubmit = (Button)findViewById(R.id.submitQuestion);
		btnSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Toast.makeText(UploadQuestionActivity.this, String.valueOf(spinnerType.getSelectedItem()), Toast.LENGTH_SHORT).show();
				dialog = ProgressDialog.show(UploadQuestionActivity.this, "Posting your question...", 
		                "Loading. Please wait...", true);
				new PostQuizQuestionsASyncTask().execute();
			}
			
		});
	}

	ProgressDialog dialog;
	public class PostQuizQuestionsASyncTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {

		String result;
		
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			QuizQuestionNetworkDatasource quizQuestionNetworkDatasource = new QuizQuestionNetworkDatasource();
			
			QuizQuestion question = new QuizQuestion();
			
			EditText v = (EditText)findViewById(R.id.editTextQuestionDescription);
			question.setDescription(v.getText().toString());
			
			question.setType(spinnerType.getSelectedItem().toString());
			
			v = (EditText)findViewById(R.id.editTextAnswerA);
			question.setAnswerA(v.getText().toString());
			
			v = (EditText)findViewById(R.id.editTextAnswerB);
			question.setAnswerB(v.getText().toString());
			
			v = (EditText)findViewById(R.id.editTextAnswerC);
			question.setAnswerC(v.getText().toString());
			
			v = (EditText)findViewById(R.id.editTextAnswerD);
			question.setAnswerD(v.getText().toString());
			
			question.setCorrectAnswer(spinnerCorrectAnswer.getSelectedItem().toString());
			
			v = (EditText)findViewById(R.id.editTextSuggestion);
			question.setSuggestion(v.getText().toString());

			v = (EditText)findViewById(R.id.editTextHint);
			question.setHint(v.getText().toString());
			
			result = quizQuestionNetworkDatasource.postQuizQuestion(question);
			
			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> out) {
			//update widget
			Toast.makeText(UploadQuestionActivity.this, (CharSequence) result, Toast.LENGTH_SHORT).show();

			dialog.cancel();
			UploadQuestionActivity.this.setTitle("Start Game");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload_question, menu);
		return true;
	}

	public Spinner getSpinnerCorrectAnswer() {
		return spinnerCorrectAnswer;
	}

	public void setSpinnerCorrectAnswer(Spinner spinnerCorrectAnswer) {
		this.spinnerCorrectAnswer = spinnerCorrectAnswer;
	}

}
