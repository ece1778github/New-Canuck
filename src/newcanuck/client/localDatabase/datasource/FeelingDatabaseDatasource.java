package newcanuck.client.localDatabase.datasource;

import java.util.ArrayList;
import java.util.List;

import newcanuck.client.localDatabase.SQLiteHelper;
import newcanuck.entity.Feeling;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FeelingDatabaseDatasource {

	private static final String TABLE_NAME = "feelings";
	private SQLiteDatabase database = null;
	private SQLiteHelper dbHelper = null;
	
	final public static String[] ALL_COLUMNS = { "_id", "mission_id", "mission_name",
			"mission_description", "mission_address", "mission_latitude", "mission_longitude", "my_rating",
			"my_feeling", "create_date", "my_img_file_name"};
	
	public FeelingDatabaseDatasource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void insertFeeling(Feeling feeling) {
		this.open();
		ContentValues values = getContentValuesFromFeeling(feeling);
		database.insert(TABLE_NAME, null, values);
		this.close();
	}

	public Feeling getFeeling(Long id){
		Feeling feeling = null;
		this.open();
		Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, "_id=?", new String[]{id.toString()},
				null, null, null);

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			feeling = cursorToFeeling(cursor);
		}

		cursor.close();
		this.close();
		return feeling;
	}
	
	public Feeling getFeelingByMission(Long missionId){
		this.open();
		Feeling feeling = null;
		Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, "mission_id=?", new String[]{missionId.toString()},
				null, null, null);

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			feeling = cursorToFeeling(cursor);
		}

		cursor.close();
		this.close();
		return feeling;
	}
	
	public List<Feeling> getAllFeelings() {
		this.open();
		List<Feeling> feelings = new ArrayList<Feeling>();

		Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, null, null,	null, null, "create_date");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Feeling f = cursorToFeeling(cursor);
			feelings.add(f);
			cursor.moveToNext();
		}

		cursor.close();
		this.close();
		return feelings;
	}
	
	
	private ContentValues getContentValuesFromFeeling(Feeling feeling) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], feeling.getMissionId());
		values.put(ALL_COLUMNS[2], feeling.getMissionName());
		values.put(ALL_COLUMNS[3], feeling.getMissionDescription());
		values.put(ALL_COLUMNS[4], feeling.getMissionAddress());
		values.put(ALL_COLUMNS[5], feeling.getMissionLatitude());
		values.put(ALL_COLUMNS[6], feeling.getMissionLongitude());
		values.put(ALL_COLUMNS[7], feeling.getMyRating());
		values.put(ALL_COLUMNS[8], feeling.getMyFeeling());
		values.put(ALL_COLUMNS[9], feeling.getCreateDate());
		values.put(ALL_COLUMNS[10], feeling.getMyImgFileName());
		
		return values;
	}
	

	private Feeling cursorToFeeling(Cursor cursor) {
		Feeling f = new Feeling();
		f.setId(cursor.getLong(0));
		f.setMissionId(cursor.getLong(1));
		f.setMissionName(cursor.getString(2));
		f.setMissionDescription(cursor.getString(3));
		f.setMissionAddress(cursor.getString(4));
		f.setMissionLatitude(cursor.getDouble(5));
		f.setMissionLongitude(cursor.getDouble(6));
		f.setMyRating(cursor.getDouble(7));
		f.setMyFeeling(cursor.getString(8));
		f.setCreateDate(cursor.getLong(9));
		f.setMyImgFileName(cursor.getString(10));
		return f;
	}

	
}
