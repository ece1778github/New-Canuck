package newcanuck.client.localDatabase.datasource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import newcanuck.client.localDatabase.SQLiteHelper;
import newcanuck.entity.Mission;
import newcanuck.entity.QuizQuestionRecord;

public class QuizQuestionRecordDatasource {
	final public static String TABLE_NAME = "quiz_question_records";
	final public static String[] ALL_COLUMNS = { "_id", "questionId", "time",
			"question", "suggestion", "type", "correctness" };

	private SQLiteDatabase database = null;
	private SQLiteHelper dbHelper = null;
	
	public QuizQuestionRecordDatasource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public List<QuizQuestionRecord> getQuizQuestionRecords(Long afterTime){
		this.open();
		List<QuizQuestionRecord> records = new ArrayList<QuizQuestionRecord>();
		Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, "time>=?", new String[]{afterTime.toString()},
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			QuizQuestionRecord q = cursorToQuizQuestionRecord(cursor);
			records.add(q);
			cursor.moveToNext();
		}

		cursor.close();
		this.close();
		
		return records;
	}
	
	public List<QuizQuestionRecord> getAllQuizQuestionRecords(){
		this.open();
		List<QuizQuestionRecord> records = new ArrayList<QuizQuestionRecord>();
		Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			QuizQuestionRecord q = cursorToQuizQuestionRecord(cursor);
			records.add(q);
			cursor.moveToNext();
		}

		cursor.close();
		this.close();
		
		return records;
	}
	
	public void insertQuizQuestionRecord(QuizQuestionRecord qr){
		this.open();
		ContentValues values = getContentValuesFromQuizQuestionRecord(qr);
		database.insert(TABLE_NAME, null, values);
		this.close();
	}
	
	public void insertQuizQuestionRecords(List<QuizQuestionRecord> qrs){
		this.open();
		try {
			database.beginTransaction();

			for (QuizQuestionRecord q : qrs) {
				ContentValues values = getContentValuesFromQuizQuestionRecord(q);
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
	
	
	private QuizQuestionRecord cursorToQuizQuestionRecord(Cursor cursor) {
		QuizQuestionRecord q = new QuizQuestionRecord();
		q.setId(cursor.getLong(0));
		q.setQuestionId(cursor.getLong(1));
		q.setTime(cursor.getLong(2));
		q.setQuestion(cursor.getString(3));
		q.setSuggestion(cursor.getString(4));
		q.setType(cursor.getString(5));
		q.setCorrectness(cursor.getString(6));
		return q;
	}
	
	private ContentValues getContentValuesFromQuizQuestionRecord(QuizQuestionRecord qr) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[0], qr.getId());
		values.put(ALL_COLUMNS[1], qr.getQuestionId());
		values.put(ALL_COLUMNS[2], qr.getTime());
		values.put(ALL_COLUMNS[3], qr.getQuestion());
		values.put(ALL_COLUMNS[4], qr.getSuggestion());
		values.put(ALL_COLUMNS[5], qr.getType());
		values.put(ALL_COLUMNS[6], qr.getCorrectness());
		return values;
	}
}

