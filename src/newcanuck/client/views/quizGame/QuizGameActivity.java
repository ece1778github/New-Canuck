package newcanuck.client.views.quizGame;

import java.util.LinkedList;
import java.util.Random;

import newcanuck.client.localDatabase.datasource.QuizQuestionRecordDatasource;
import newcanuck.client.quizGameController.QuizGameController;
import newcanuck.client.userProfile.UserProfile;
import newcanuck.client.views.R;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class QuizGameActivity extends Activity {

	protected static final int QUIZ_REPORT = 13213;
	private QuizGameController controller;

	// widgets
	private ProgressBar timeBar;

	private Button buttonAnswerA;
	private Button buttonAnswerB;
	private Button buttonAnswerC;
	private Button buttonAnswerD;

	private Button button5050;
	private Button buttonSkip;
	private Button buttonHint;

	private TextView questionType;
	private TextView questionTitle;
	private TextView gameCount;
	private TextView questionDescription;
	private TextView score;

	private int questionCount = 1;
	
	private CountDownTimer countDownTimer = new CountDownTimer(10000,100) {
		
        @Override
		public void onTick(long millisUntilFinished) {
        	
        	int count = (int) ((10000 - millisUntilFinished)/100);
			if (count < timeBar.getMax()) {
				timeBar.setProgress(count);
			}
		}

        @Override
        public void onFinish() {
        	controller.timesUp();
        	if(controller.isCompleted()){
        		this.cancel();
				completeGame();
        	}
        	else{
        		updateUI();
        	}
        }		
    };

    private void completeGame() {
    	
    	QuizQuestionRecordDatasource datasource = new QuizQuestionRecordDatasource(this);
    	datasource.insertQuizQuestionRecords(controller.getAnswerCorrectnessRecords());
    	
    	Intent intent = new Intent(QuizGameActivity.this, QuizReportActivity.class);
    	intent.putExtra("startTime", controller.getStartTime());
    	
    	intent.putExtra("score", controller.getScore());
    	int totalScores = UserProfile.getProfile(this).getTotolScores();
    	totalScores += controller.getScore();
    	UserProfile.getProfile(this).setTotolScores(totalScores);
    	
    	intent.putExtra("rightCount", controller.getRightCount());
    	intent.putExtra("wrongCount", controller.getWrongCount());
    	intent.putExtra("questionCount", controller.getNumOfQuestions());
    	
		startActivityForResult(intent, QUIZ_REPORT);
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_game);

		// init controller
		controller = new QuizGameController(this);

		// loadWidgets
		loadWidgets();
	}

	private void loadWidgets() {
		timeBar = (ProgressBar) findViewById(R.id.timeBar);
		timeBar.setMax(100);
		timeBar.setProgress(0);

		questionType = (TextView) findViewById(R.id.questionType);
		questionTitle = (TextView) findViewById(R.id.questionTitle);
		gameCount = (TextView) findViewById(R.id.questionsInfo);
		questionDescription = (TextView) findViewById(R.id.questionDescription);
		score = (TextView) findViewById(R.id.textViewScore);

		buttonAnswerA = (Button) findViewById(R.id.buttonAnswerA);
		buttonAnswerB = (Button) findViewById(R.id.buttonAnswerB);
		buttonAnswerC = (Button) findViewById(R.id.buttonAnswerC);
		buttonAnswerD = (Button) findViewById(R.id.buttonAnswerD);

		OnClickListener answerListener = getAnswerListener();

		buttonAnswerA.setOnClickListener(answerListener);
		buttonAnswerB.setOnClickListener(answerListener);
		buttonAnswerC.setOnClickListener(answerListener);
		buttonAnswerD.setOnClickListener(answerListener);

		button5050 = (Button) findViewById(R.id.button5050);
		buttonSkip = (Button) findViewById(R.id.buttonSkip);
		buttonHint = (Button) findViewById(R.id.buttonHint);

		OnClickListener propListener = getPropListener();

		button5050.setOnClickListener(propListener);
		buttonSkip.setOnClickListener(propListener);
		buttonHint.setOnClickListener(propListener);
		

		updateUI();
	}

	private OnClickListener getPropListener() {
		OnClickListener propListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.equals(button5050)) {
					if (controller.fiftyFiftyUsable()) {
						controller.useFiftyFifty();

						Random r = new Random();
						LinkedList<Button> buttons = new LinkedList<Button>();
						
						if(!controller.getCurrentQuestion().getCorrectAnswer().equals("A"))
							buttons.add(buttonAnswerA);
						
						if(!controller.getCurrentQuestion().getCorrectAnswer().equals("B"))
							buttons.add(buttonAnswerB);
						
						if(!controller.getCurrentQuestion().getCorrectAnswer().equals("C"))
							buttons.add(buttonAnswerC);
						
						if(!controller.getCurrentQuestion().getCorrectAnswer().equals("D"))
							buttons.add(buttonAnswerD);
						
						int location = r.nextInt(2);
						buttons.get(location).setVisibility(View.INVISIBLE);
						buttons.remove(location);
						buttons.get(r.nextInt(1)).setVisibility(View.INVISIBLE);

						button5050.setVisibility(View.INVISIBLE);
					}
				} else if (v.equals(buttonSkip)) {
					if (controller.skipUsable()) {
						controller.useSkip();
						buttonSkip.setVisibility(View.INVISIBLE);
						if (controller.isCompleted()) {
							countDownTimer.cancel();
							completeGame();
						} else {
							updateUI();
						}
					}

				} else if (v.equals(buttonHint)) {
					if (controller.hintUsable()) {
						controller.useHint();
						Toast.makeText(QuizGameActivity.this,
								controller.getCurrentQuestion().getHint(),
								Toast.LENGTH_SHORT).show();
						buttonHint.setVisibility(View.INVISIBLE);
					}
				}

			}
		};
		return propListener;
	}

	private OnClickListener getAnswerListener() {
		OnClickListener answerListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.equals(buttonAnswerA)) {
					controller.doNextQuestion("A");
				} else if (v.equals(buttonAnswerB)) {
					controller.doNextQuestion("B");
				} else if (v.equals(buttonAnswerC)) {
					controller.doNextQuestion("C");
				} else if (v.equals(buttonAnswerD)) {
					controller.doNextQuestion("D");
				}

				if (controller.isCompleted()) {
					countDownTimer.cancel();
					completeGame();
				} else {
					updateUI();
				}
			}

		};
		return answerListener;
	}

	private void updateUI() {
		buttonAnswerA.setVisibility(View.VISIBLE);
		buttonAnswerB.setVisibility(View.VISIBLE);
		buttonAnswerC.setVisibility(View.VISIBLE);
		buttonAnswerD.setVisibility(View.VISIBLE);

		questionType.setText(controller.getCurrentQuestion().getType());
		questionDescription.setText(controller.getCurrentQuestion()
				.getDescription());
		gameCount.setText(controller.getRightCount() + "/"
				+ controller.getWrongCount());
		questionTitle.setText("Q" + questionCount++);
		
		score.setText(Integer.toString(controller.getScore()));

		buttonAnswerA.setText(controller.getCurrentQuestion().getAnswerA());
		buttonAnswerB.setText(controller.getCurrentQuestion().getAnswerB());
		buttonAnswerC.setText(controller.getCurrentQuestion().getAnswerC());
		buttonAnswerD.setText(controller.getCurrentQuestion().getAnswerD());
	
		timeBar.setProgress(0);
		countDownTimer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quiz_game, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			countDownTimer.cancel();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIZ_REPORT
				&& resultCode == QuizReportActivity.QUIZ_REPORT_RESULT) {
			countDownTimer.cancel();
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		countDownTimer.cancel();
		super.onStop();
	}
	
}
