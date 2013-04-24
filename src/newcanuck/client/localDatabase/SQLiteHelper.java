package newcanuck.client.localDatabase;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "newcanuck.db";
	
	//increase version to call onUpdate. 
	private static final int DATABASE_VERSION = 80;
	
	private static final String DATABASE_CREATE_MISSION = "create table missions (_id integer primary key not null,"
			+ " name text not null,"
			+ " create_date real,"
			+ " ratetimes integer,"
			+ " rating real,"
			+ " latitude real,"
			+ " longitude real,"
			+ " address text,"
			+ " description text,"
			+ " state integer,"
			+ " img_file_name text,"
			+ " add_date integer,"
			+ " complete_date integer"
			+ ");";

	private static final String DATABASE_CREATE_MY_MISSION = "create table my_missions (_id integer primary key not null,"
			+ " name text not null,"
			+ " create_date real,"
			+ " ratetimes integer,"
			+ " rating real,"
			+ " latitude real,"
			+ " longitude real,"
			+ " address text,"
			+ " description text,"
			+ " state integer,"
			+ " img_file_name text,"
			+ " add_date integer,"
			+ " complete_date integer"
			+ ");";

	private static final String DATABASE_CREATE_FEELING = "create table feelings (_id integer primary key autoincrement,"
			+ " mission_id integer,"
			+ " mission_name text,"
			+ " mission_description text,"
			+ " mission_address text,"
			+ " mission_latitude real,"
			+ " mission_longitude real,"
			+ " my_rating real,"
			+ " my_feeling text,"
			+ " create_date integer,"
			+ " my_img_file_name text"
			+ ");";
	
	private static final String DATABASE_CREATE_QUIZ_QUESTION = "create table quiz_questions (_id integer primary key autoincrement,"
			+ " description text,"
			+ " type text,"
			+ " answerA text,"
			+ " answerB text,"
			+ " answerC text,"
			+ " answerD text,"
			+ " correctAnswer text,"
			+ " hint text,"
			+ " suggestion text"
			+ ");";
	
	private static final String DATABASE_CREATE_QUIZ_QUESTION_RECORD = "create table quiz_question_records (_id integer primary key autoincrement,"
			+ " questionId integer,"
			+ " time integer,"
			+ " question text,"
			+ " suggestion text,"
			+ " type text,"
			+ " correctness text"
			+ ");";
	
	private static final String DATABASE_DROP_MISSION = "DROP TABLE IF EXISTS missions";
	private static final String DATABASE_DROP_MY_MISSION = "DROP TABLE IF EXISTS my_missions";
	private static final String DATABASE_DROP_FEELING = "DROP TABLE IF EXISTS feelings";
	private static final String DATABASE_DROP_QUIZ_QUESTION = "DROP TABLE IF EXISTS quiz_questions";
	private static final String DATABASE_DROP_QUIZ_QUESTION_RECORD = "DROP TABLE IF EXISTS quiz_question_records";

	
	//init questions 
	private static final String[] INIT_QUESIONS_INSERT_STATEMENTS 
		= {"INSERT INTO QUIZ_QUESTIONS VALUES (1, 'Question 1', 'Immigration', 'Answer A1', 'Answer B correct1', 'Answer C', 'Answer D', 'B', 'hint 1', 'suggestion 1' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (2, 'Question 2', 'Immigration', 'Answer A2', 'Answer B correct2', 'Answer C', 'Answer D', 'B', 'hint 2', 'suggestion 2' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (3, 'Question 3', 'Immigration', 'Answer A3', 'Answer B correct3', 'Answer C', 'Answer D', 'B', 'hint 3', 'suggestion 3' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (4, 'Question 4', 'Immigration', 'Answer A4', 'Answer B correct4', 'Answer C', 'Answer D', 'B', 'hint 4', 'suggestion 4' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (5, 'Question 5', 'Immigration', 'Answer A5', 'Answer B correct5', 'Answer C', 'Answer D', 'B', 'hint 5', 'suggestion 5' )",
		   
		   "INSERT INTO QUIZ_QUESTIONS VALUES (6, 'What is the meaning of Toonie?', 'Life', 'Canadian 1 dollar coin', 'Canadian 2 dollar coin', 'Canadian 50 cents coin', 'Canadian 25 cents coin', 'B', 'There are 2 colors in the coin.', 'Tonnie refers to Canadian 2 dollar coin in Canada. Loonie refers to 1 dollar coin.' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (7, 'Which date is the national day of Canada?', 'Life', 'July 1, 1867', 'July 2, 1867', 'July 1, 1868', 'July 2, 1868', 'A', 'The first day of a month.', 'Canada Day is the national day of Canada, a federal statutory holiday celebrating the anniversary of the July 1, 1867, enactment of the British North America Act, 1867 (today called the Constitution Act, 1867), which united three colonies into a single country called Canada within the British Empire. Originally called Dominion Day, the name was changed in 1982, the year the Canada Act was passed. Canada Day observances take place throughout Canada as well as by Canadians internationally.' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (8, 'Who is the current Prime Minister of Canada?', 'Life', 'Stephen Harper', 'Julia Gillard', 'David Cameron', 'David Huston', 'A', 'He is a good man.', 'Stephen Joseph Harper (born April 30, 1959), PC, MP, is the twenty-second and current prime minister of Canada and leader of the Conservative Party. Harper became prime minister when his party formed a minority government after the 2006 federal election. He is the first prime minister from the newly reconstituted Conservative Party, following a merger of the Progressive Conservative and Canadian Alliance parties.' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (9, 'Which food is famous in Canada?', 'Life', 'Star Buck Coffee', 'Buger King', 'Red Loster', 'Poutine', 'D', 'Don not eat too much if you don not want to get fat', 'Poutine is a typical Canadian dish (originally from Quebec), made with french fries, topped with brown gravy and curd cheese. Sometimes additional ingredients are added. Poutine is a fast food dish that can now be found across Canada (and is also found in some places in the northern United States).' )",
		   
		   "INSERT INTO QUIZ_QUESTIONS VALUES (11, 'Question 11', 'Fashion', 'Answer A11', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 11', 'suggestion 11' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (12, 'Question 12', 'Fashion', 'Answer A12', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 12', 'suggestion 12' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (13, 'Question 13', 'Fashion', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (14, 'Question 14', 'Fashion', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (15, 'Question 15', 'Fashion', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   
		   "INSERT INTO QUIZ_QUESTIONS VALUES (16, 'Which Province is the last Province to joined Canada?', 'History', 'Newfoundland Labrador', 'Alberta', 'Saskatchewan', 'Prince Edward Island', 'A', 'Answer is more than one word.', 'Newfoundland Labrador is the last province to join Canada. It happened in Mach 31st, 1949. Also, Nunavut, which joined Canada in April 1st, 1999, is the last territory to join Canada. ' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (17, 'Till now, how many Prime Ministers have served for Canada in history?', 'History', '18', '21', '22', '31', 'C', 'More than 20 less than 30.', 'Newfoundland Labrador is the last province to join Canada. It happened in Mach 31st, 1949. Also, Nunavut, which joined Canada in April 1st, 1999, is the last territory to join Canada.' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (18, 'Now how many provinces/territories does Canada have?', 'History', '12', '13', '17', '20', 'B', 'Not very bigger than 10', 'Canada has 10 provinces, 3 territories.  Provinces: Newfoundland and Labrador, Prince Edward, Island, Nova Scotia, New Brunswick, Quebec ,Ontario ,Manitoba ,Saskatchewan ,Alberta ,British Columbia. Territories: Nunavut, North West Territories, Yukon.' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (19, 'Which province in the below is not a province of Canada in 1868?', 'History', 'Ontario', 'Qu¨¦bec', 'Nova Scotia', 'British Columbia', 'D', 'It is not in the west of Canada.', 'In 1867, Canada had only four provinces, and they are Ontario, Qu¨¦bec, Nova Scotia and New Brunswick. They are all located in the east of Canada.' )",
 
		   "INSERT INTO QUIZ_QUESTIONS VALUES (21, 'Question 21', 'Sports', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (22, 'Question 22', 'Sports', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (23, 'Question 23', 'Sports', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (24, 'Question 24', 'Sports', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (25, 'Question 25', 'Sports', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   
		   "INSERT INTO QUIZ_QUESTIONS VALUES (26, 'Question 26', 'Common Sense', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (27, 'Question 27', 'Common Sense', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (28, 'Question 28', 'Common Sense', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (29, 'Question 29', 'Common Sense', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (30, 'Question 30', 'Common Sense', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   
		   "INSERT INTO QUIZ_QUESTIONS VALUES (31, 'Question 31', 'Culture', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (32, 'Question 32', 'Culture', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (33, 'Question 33', 'Culture', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (34, 'Question 34', 'Culture', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (35, 'Question 35', 'Culture', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   
		   "INSERT INTO QUIZ_QUESTIONS VALUES (36, 'Question 36', 'Slangs', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (37, 'Question 37', 'Slangs', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (38, 'Question 38', 'Slangs', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (39, 'Question 39', 'Slangs', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )",
		   "INSERT INTO QUIZ_QUESTIONS VALUES (40, 'Question 40', 'Slangs', 'Answer A13', 'Answer B correct6', 'Answer C', 'Answer D', 'B', 'hint 13', 'suggestion 13' )"};
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_MISSION);
		database.execSQL(DATABASE_CREATE_MY_MISSION);
		database.execSQL(DATABASE_CREATE_FEELING);
		database.execSQL(DATABASE_CREATE_QUIZ_QUESTION);
		database.execSQL(DATABASE_CREATE_QUIZ_QUESTION_RECORD);
		
		for(String questionInsert : INIT_QUESIONS_INSERT_STATEMENTS) {
			database.execSQL(questionInsert);
		}
		//TODO: insert questions
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL(DATABASE_DROP_MY_MISSION);
		database.execSQL(DATABASE_DROP_MISSION);
		database.execSQL(DATABASE_DROP_FEELING);
		database.execSQL(DATABASE_DROP_QUIZ_QUESTION);
		database.execSQL(DATABASE_DROP_QUIZ_QUESTION_RECORD);
		onCreate(database);
	}

}
