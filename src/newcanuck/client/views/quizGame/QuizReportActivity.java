package newcanuck.client.views.quizGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newcanuck.client.localDatabase.datasource.QuizQuestionRecordDatasource;
import newcanuck.client.views.R;
import newcanuck.entity.QuizQuestionRecord;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class QuizReportActivity extends Activity {
	public static int QUIZ_REPORT_RESULT = 2315;
	
	private QuizQuestionRecordDatasource quizQuestionRecordDatasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_report);
		
		Intent intent = getIntent();
		Long time = intent.getLongExtra("startTime", 0);
		int score = intent.getIntExtra("score", 0);
		int questionCount = intent.getIntExtra("questionCount", 0);
		int rightCount = intent.getIntExtra("rightCount", 0);
		int wrongCount = intent.getIntExtra("wrongCount", 0);
	
		quizQuestionRecordDatasource = new QuizQuestionRecordDatasource(this);
		List<QuizQuestionRecord> records = quizQuestionRecordDatasource.getQuizQuestionRecords(time);
		
		Map<String, List<QuizQuestionRecord>> mapTypeRecords = new HashMap<String, List<QuizQuestionRecord>>();
		Map<String, Integer> mapTypeMistakes = new HashMap<String, Integer>();
		for(QuizQuestionRecord r : records) {
			String type = r.getType();
			if(!mapTypeRecords.keySet().contains(type)) {
				mapTypeRecords.put(type, new ArrayList<QuizQuestionRecord>());
				mapTypeMistakes.put(type, 0);
			}
			
			mapTypeRecords.get(type).add(r);
			if(r.getCorrectness().equals("false")){
				mapTypeMistakes.put(type, mapTypeMistakes.get(type)+1);
			}
		}		
		
		//data in the result block
		int accuracy = (int)(rightCount*100.0f/(rightCount+wrongCount));
		List<String> mistakesListText = new ArrayList<String>();
		
		for(String type : mapTypeMistakes.keySet()){
			Integer mistakes = mapTypeMistakes.get(type);
			mistakesListText.add( mistakes + " mistakes in "+type+" section.");
		}
		
		String mistakesReport = "Accuracy: " + accuracy + "%\n";
		for(String txt : mistakesListText){
			mistakesReport += txt + "\n";
		}
		
		TextView textViewMistakesReport = (TextView)findViewById(R.id.resultReport);
		textViewMistakesReport.setText(mistakesReport);
		
		//data in the Suggestion block : Information you may want to know.
		String suggestionsReport = "";
		for(String type: mapTypeRecords.keySet()) {
			suggestionsReport += "+" + type + "\n\n";
			int i=1;
			for(QuizQuestionRecord record : mapTypeRecords.get(type)) {
				if(record.getCorrectness().equals("true"))
					continue;
				
				suggestionsReport += i+". "+record.getSuggestion()+"\n\n";
				i++;
			}
			suggestionsReport+="\n";
		}
		

		TextView textViewSuggestionsReport = (TextView)findViewById(R.id.suggestionsReport);
		textViewSuggestionsReport.setText(suggestionsReport);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quiz_report, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			returnToQuiz();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void returnToQuiz(){
		setResult(QUIZ_REPORT_RESULT);
		finish();
	}

}
