package newcanuck.client.localDatabase.datasource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import newcanuck.client.localDatabase.SQLiteHelper;
import newcanuck.entity.Mission;
import newcanuck.entity.QuizQuestion;

public class QuizGameDatasource {

	final public static String TABLE_NAME = "quiz_questions";
	final public static String[] ALL_COLUMNS = { "_id", "description", "type",
			"answerA", "answerB", "answerC", "answerD", "correctAnswer",
			"hint", "suggestion" };

	private SQLiteDatabase database = null;
	private SQLiteHelper dbHelper = null;

	public QuizGameDatasource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	private QuizQuestion cursorToQuizQuesion(Cursor cursor) {
		QuizQuestion q = new QuizQuestion();
		q.setId(cursor.getLong(0));
		q.setDescription(cursor.getString(1));
		q.setType(cursor.getString(2));
		q.setAnswerA(cursor.getString(3));
		q.setAnswerB(cursor.getString(4));
		q.setAnswerC(cursor.getString(5));
		q.setAnswerD(cursor.getString(6));
		q.setCorrectAnswer(cursor.getString(7));
		q.setHint(cursor.getString(8));
		q.setSuggestion(cursor.getString(9));

		return q;
	}

	public List<QuizQuestion> getAllQuizQuestions() {
		this.open();
		List<QuizQuestion> questions = new ArrayList<QuizQuestion>();

		Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			QuizQuestion q = cursorToQuizQuesion(cursor);
			questions.add(q);
			cursor.moveToNext();
		}

		cursor.close();
		this.close();
		return questions;
	}

	public void updateAllQuetions(List<QuizQuestion> tempQuestions) {
		this.open();
		try {
			database.beginTransaction();
			database.execSQL("delete from " + TABLE_NAME + ";");

			for (QuizQuestion q : tempQuestions) {
				ContentValues values = getContentValuesFromQuestion(q);
				database.insert(TABLE_NAME, null, values);
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}

	private ContentValues getContentValuesFromQuestion(QuizQuestion q) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[0], q.getId());
		values.put(ALL_COLUMNS[1], q.getDescription());
		values.put(ALL_COLUMNS[2], q.getType());
		values.put(ALL_COLUMNS[3], q.getAnswerA());
		values.put(ALL_COLUMNS[4], q.getAnswerB());
		values.put(ALL_COLUMNS[5], q.getAnswerC());
		values.put(ALL_COLUMNS[6], q.getAnswerD());
		values.put(ALL_COLUMNS[7], q.getCorrectAnswer());
		values.put(ALL_COLUMNS[8], q.getHint());
		values.put(ALL_COLUMNS[9], q.getSuggestion());
		return values;
	}

	public void updateMission(QuizQuestion quesions) {
		this.open();
		ContentValues values = getContentValuesFromQuestion(quesions);
		database.update(TABLE_NAME, values, "_id=?", new String[] { quesions
				.getId().toString() });
		this.close();
	}

	public List<QuizQuestion> getRandomQuestions(String[] types,
			int numberOfQuestions) {
		// you should give an number that can be divided by number of types or
		// you can get numbers of questions less than that
		this.open();
		int averageLength = numberOfQuestions / types.length;
		QuizQuestion question;
		List<QuizQuestion> questions = new LinkedList<QuizQuestion>();
		Cursor cursor = null;

		try {
			for (int i = 0; i < types.length; i++) {
				if (i == types.length - 1) {
					averageLength = numberOfQuestions - (types.length - 1)
							* averageLength;
				}

				cursor = database.query(TABLE_NAME, ALL_COLUMNS, "type = ?",
						new String[] { types[i] }, null, null, null);

				int randomArray[] = getExtentRandomNumbers(cursor.getCount(),
						averageLength);

				for (int j = 0; j < randomArray.length; j++) {

					cursor.moveToPosition(randomArray[j]);
					question = cursorToQuizQuesion(cursor);
					questions.add(question);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			cursor.close();
			this.close();
		}

		return questions;
	}

	private int getExtentRandomNumber(int extent) {
		int number = (int) (Math.random() * extent);
		return number;
	}

	private int[] getExtentRandomNumbers(int extent, int number) {

			if (extent < number) number = extent;
			List<Integer> extentArray = new LinkedList<Integer>();
			int[] rs = new int[number];
			for (int i = 0; i <extent; i++ ){
				extentArray.add(i);
			}
			for (int i =0; i< number; i++){
				int position = getExtentRandomNumber(extent*2)%extentArray.size();
				rs[i] = extentArray.get(position);
				extentArray.remove(position);
			}
			return rs;
	}
}
