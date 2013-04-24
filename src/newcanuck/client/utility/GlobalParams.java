package newcanuck.client.utility;

public class GlobalParams {
	static public final String URL_DOMAIN = "http://ec2-107-21-233-52.compute-1.amazonaws.com:8080/NewCanuck";
	//static public final String URL_DOMAIN = "http://192.168.137.1:8080/NewCanuck";
	//static public final String URL_DOMAIN = "http://142.1.134.123:8080/NewCanuck";
	
	static public final String URL_USER_IMAGE_SMALL_PREFIX = URL_DOMAIN+"/user_images/s/";
	static public final String URL_USER_IMAGE_LARGE_PREFIX = URL_DOMAIN+"/user_images/l/";
	
	static public final String URL_GET_ALL_MISSIONS = URL_DOMAIN+"/getAllMissions";
	static public final String URL_POST_MISSION = URL_DOMAIN+"/postMission";
	
	static public final String URL_GET_ALL_QUESTIONS = URL_DOMAIN+"/getAllQuizQuestions";
	static public final String URL_POST_QUESTION = URL_DOMAIN+"/postQuizQuestion";
	
	static public final String URL_GET_COMMENTS = URL_DOMAIN+"/getComments";
	static public final String URL_POST_COMMENT = URL_DOMAIN+"/postComment";
	
	static public final String IMG_CACHE_DIR_PATH = "newcanuck/cache/"; 
}
