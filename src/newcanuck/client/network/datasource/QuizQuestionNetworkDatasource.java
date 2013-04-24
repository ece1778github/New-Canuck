package newcanuck.client.network.datasource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import newcanuck.client.network.HttpModule;
import newcanuck.client.utility.GlobalParams;
import newcanuck.entity.QuizQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizQuestionNetworkDatasource {

	public List<QuizQuestion> getAllQuestionssFromServer() {
		final String url = GlobalParams.URL_GET_ALL_QUESTIONS;
		
		try {
			List<QuizQuestion> questions = new LinkedList<QuizQuestion>();
			JSONArray jsonArray = HttpModule.getJsonDataFromServer(url);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				
				QuizQuestion question = getQuizQuestionFromJson(json);
				questions.add(question);
			}
			
			return questions;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public String postQuizQuestion(QuizQuestion question) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("description", question.getDescription());
		map.put("type", question.getType());
		map.put("answerA", question.getAnswerA());
		map.put("answerB", question.getAnswerB());
		map.put("answerC", question.getAnswerC());
		map.put("answerD", question.getAnswerD());
		map.put("correctAnswer", question.getCorrectAnswer());
		map.put("hint", question.getHint());
		map.put("suggestion", question.getSuggestion());
		return HttpModule.postJsonDataToServer(GlobalParams.URL_POST_QUESTION,map);
	}

	private QuizQuestion getQuizQuestionFromJson(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		QuizQuestion question = new QuizQuestion();
		question.setId(json.getLong("id"));
		question.setDescription(json.getString("description"));
		question.setType(json.getString("type"));
		question.setAnswerA(json.getString("answerA"));
		question.setAnswerB(json.getString("answerB"));
		question.setAnswerC(json.getString("answerC"));
		question.setAnswerD(json.getString("answerD"));
		question.setCorrectAnswer(json.getString("correctAnswer"));
		question.setHint(json.getString("hint"));
		question.setSuggestion(json.getString("suggestion"));
		return question;
	}
}
