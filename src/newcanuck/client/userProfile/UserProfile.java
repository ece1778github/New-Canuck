package newcanuck.client.userProfile;

import android.content.Context;
import android.content.SharedPreferences;

public class UserProfile {
	
	private static UserProfile profile;
	
	//TODO: now every thing is hard coded...
	private final static String[] DEFAULT_TYPES = {"Sports","Culture","History","Fashion","Life"};
	private final static String[] ALL_TYPES = {"Imigration", "Slangs", "Sports", "Culture", "History", "Fashion", "Life",  "Common Sense"};
	private final static int DEFAULT_NUM_QUESTIONS = 20;
	
	private Context context;

	static public UserProfile getProfile(Context context){
		if(profile == null){
			profile = new UserProfile();
		}
		
		profile.context = context;
		return profile;
	}
	
	private UserProfile(){
		
	}
	
	public String getUserName() {
		SharedPreferences setting = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
		String userName = setting.getString("user_name", "Default Name");
		return userName;
	}

	public void setUserName(String userName) {
		SharedPreferences setting = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		setting.edit().putString("user_name", userName).commit();
	}

	public String[] getQuestionTypes() {
		SharedPreferences setting = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
		String strTypes = setting.getString("questions_types", "");
		if(strTypes.equals("")){
			return DEFAULT_TYPES;
		}
		else{
			return strTypes.split("-");
		}
	}

	public void setQuestionTypes(String[] questionTypes) {
		SharedPreferences setting = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
		StringBuffer types = new StringBuffer();
		
		if(questionTypes.length>0){
			types.append(questionTypes[0]);
			for(int i=1; i<questionTypes.length; i++){
				types.append("-").append(questionTypes[i]);
			}
		}
		SharedPreferences.Editor editor = setting.edit();
		editor.putString("questions_types", types.toString());
		editor.apply();
		
	}

	public int getNumOfQuestions() {
		SharedPreferences setting = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		return setting.getInt("num_of_questions", DEFAULT_NUM_QUESTIONS);
		
	}

	public void setNumOfQuestions(int numOfQuestions) {
		SharedPreferences setting = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		setting.edit().putInt("num_of_questions", numOfQuestions).commit();
	}

	public int getTotolScores() {
		SharedPreferences setting = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		return setting.getInt("total_score", 0);
	}

	public void setTotolScores(int totolScores) {
		SharedPreferences setting = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		setting.edit().putInt("total_score", totolScores).commit();
	}
	
	public int getTotolCompletedMissions() {
		SharedPreferences setting = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		return setting.getInt("total_missions", 0);
	}

	public void setTotolCompletedMissions(int totalMissions) {
		SharedPreferences setting = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		setting.edit().putInt("total_missions", totalMissions).commit();
	}

	public String[] getAllTypes() {
		return ALL_TYPES;
	}
}
