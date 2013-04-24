package newcanuck.client.quizGameController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import newcanuck.client.localDatabase.datasource.QuizGameDatasource;
import newcanuck.client.userProfile.UserProfile;
import newcanuck.entity.QuizQuestion;
import newcanuck.entity.QuizQuestionRecord;

public class QuizGameController {

	private int currentQuestion;
	private List<QuizQuestion> questions;
	private List<QuizQuestionRecord> answerCorrectnessRecords;
	private int score;
	private int rightCount;
	private int wrongCount;
	private Long startTime;

	private boolean skip; // whether skip is used
	private boolean hint; // whether hint is used
	private boolean fiftyFifty; // whether 50-50 is used
	private int numOfQuestions;

	private UserProfile profile;

	public QuizGameController(Context context) {
		answerCorrectnessRecords = new ArrayList<QuizQuestionRecord>();
		
		currentQuestion = 0;
		score = 0;
		skip = true;
		hint = true;
		fiftyFifty = true;

		rightCount = 0;
		wrongCount = 0;
		
		profile = UserProfile.getProfile(context);
		String[] types = profile.getQuestionTypes();

		numOfQuestions = profile.getNumOfQuestions();
		
		startTime = new Date().getTime();

		// TODO: revised to add some parameter
		QuizGameDatasource quizGameDatasource = new QuizGameDatasource(context);
		questions = quizGameDatasource.getRandomQuestions(types,numOfQuestions);
		numOfQuestions = questions.size();
		
	}

	// TODO: will be deleted
	private List<QuizQuestion> getTestQuestions() {
		// TODO Auto-generated method stub
		List<QuizQuestion> questionsList = new ArrayList<QuizQuestion>();
		String[] questionTypes = profile.getQuestionTypes();
		for (int i = 0; i < numOfQuestions; i++) {
			QuizQuestion quizQuestion = new QuizQuestion();
			quizQuestion.setAnswerA("answer A");
			quizQuestion.setAnswerB("answer B");
			quizQuestion.setAnswerC("answer C");
			quizQuestion.setAnswerD("answer D");

			quizQuestion.setCorrectAnswer("A");

			quizQuestion.setDescription("sample question " + i);
			quizQuestion.setHint("sample hint " + i);
			quizQuestion.setSuggestion("sample suggestion " + i);
			quizQuestion.setType(questionTypes[i % questionTypes.length]);

			questionsList.add(quizQuestion);
		}

		return questionsList;
	}

	public QuizQuestion getCurrentQuestion() {
		return questions.get(currentQuestion);
	}

	/*
	 * pseudocode
	 * 
	 * 
	 * doNextQuestion(answer); if(isCompleted){ change to report activtiy and
	 * show result. } else{ update UI(getCurrentQuestion) }
	 */

	public boolean isCompleted() {
		return currentQuestion == questions.size();
	}

	public boolean doNextQuestion(String answer) {
		boolean isCorrect;
		String correctness;
		
		if (answer.equals(questions.get(currentQuestion).getCorrectAnswer())) {
			isCorrect = true;
			correctness = "true";
			rightCount++;
			score += 20;
		} else {
			isCorrect = false;
			correctness = "false";
			wrongCount++;
		}
		
		addQuestionRecord(correctness);
		
		
		currentQuestion++;
		return isCorrect;
	}

	private void addQuestionRecord(String correctness) {
		QuizQuestionRecord qr = new QuizQuestionRecord();
		qr.setCorrectness(correctness);
		qr.setQuestion(questions.get(currentQuestion).getDescription());
		qr.setQuestionId(questions.get(currentQuestion).getId());
		qr.setSuggestion(questions.get(currentQuestion).getSuggestion());
		qr.setType(questions.get(currentQuestion).getType());
		qr.setTime(new Date().getTime());
		answerCorrectnessRecords.add(qr);
	}

	public void timesUp() {
		wrongCount++;
		addQuestionRecord("false");
		currentQuestion++;
	}

	public boolean skipUsable() {
		return skip;
	}

	public void useSkip() {
		score += 20;
		addQuestionRecord("false");
		currentQuestion++;
		rightCount++;
		skip = false;
	}

	public boolean hintUsable() {
		return hint;
	}

	public String useHint() {
		hint = false;
		return questions.get(currentQuestion).getHint();
	}

	public boolean fiftyFiftyUsable() {
		return fiftyFifty;
	}

	public void useFiftyFifty() {
		fiftyFifty = false;
	}

	public int getScore() {
		return score;
	}

	public int getNumOfQuestions() {
		return numOfQuestions;
	}

	public int getRightCount() {
		return rightCount;
	}

	public int getWrongCount() {
		return wrongCount;
	}
	
	public List<QuizQuestionRecord> getAnswerCorrectnessRecords(){
		return answerCorrectnessRecords;
	}

	public Long getStartTime() {
		return startTime;
	}
}
