package newcanuck.client.localDatabase.datasource;

import java.util.ArrayList;
import java.util.List;

import newcanuck.client.localDatabase.SQLiteHelper;
import newcanuck.entity.Mission;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MyMissionDatabaseDatasource {

	final public static String TABLE_NAME = "my_missions";
	final public static String[] ALL_COLUMNS = { "_id", "name", "create_date",
			"ratetimes", "rating", "latitude", "longitude", "address",
			"description", "state", "img_file_name", "add_date", "complete_date" };

	private SQLiteDatabase database = null;
	private SQLiteHelper dbHelper = null;

	public MyMissionDatabaseDatasource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	private Mission cursorToMission(Cursor cursor) {
		Mission m = new Mission();
		m.setId(cursor.getLong(0));
		m.setName(cursor.getString(1));
		m.setCreateDate(cursor.getLong(2));
		m.setRatetimes(cursor.getLong(3));
		m.setRating(cursor.getDouble(4));
		m.setLatitude(cursor.getDouble(5));
		m.setLongitude(cursor.getDouble(6));
		m.setAddress(cursor.getString(7));
		m.setDescription(cursor.getString(8));
		m.setState(cursor.getLong(9));
		m.setImgFileName(cursor.getString(10));
		m.setAddDate(cursor.getLong(11));
		m.setCompleteDate(cursor.getLong(12));
		return m;
	}

	public List<Mission> getAllMyMissions() {
		this.open();
		List<Mission> missions = new ArrayList<Mission>();

		Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, null, null,
				null, null, "add_date DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Mission m = cursorToMission(cursor);
			missions.add(m);
			cursor.moveToNext();
		}

		cursor.close();
		this.close();
		return missions;
	}

	private ContentValues getContentValuesFromMission(Mission m) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[0], m.getId());
		values.put(ALL_COLUMNS[1], m.getName());
		values.put(ALL_COLUMNS[2], m.getCreateDate());
		values.put(ALL_COLUMNS[3], m.getRatetimes());
		values.put(ALL_COLUMNS[4], m.getRating());
		values.put(ALL_COLUMNS[5], m.getLatitude());
		values.put(ALL_COLUMNS[6], m.getLongitude());
		values.put(ALL_COLUMNS[7], m.getAddress());
		values.put(ALL_COLUMNS[8], m.getDescription());
		values.put(ALL_COLUMNS[9], m.getState());
		values.put(ALL_COLUMNS[10], m.getImgFileName());
		values.put(ALL_COLUMNS[11], m.getAddDate());
		values.put(ALL_COLUMNS[12], m.getCompleteDate());
		return values;
	}
	
	public void insertMission(Mission mission){
		this.open();
		mission.setState(1l);//mark being taken
		ContentValues values = getContentValuesFromMission(mission);
		database.insert(TABLE_NAME, null, values);
		this.close();
	}
	
	public void updateMyMission(Mission mission){
		this.open();
		ContentValues values = getContentValuesFromMission(mission);
		database.update(TABLE_NAME, values, "_id=?", new String[]{mission.getId().toString()});
		this.close();
	}
	
	public void deleteMyMission(Long id){
		this.open();
		database.delete(TABLE_NAME, "_id=?", new String[]{id.toString()});
		this.close();
	}
	
	public Mission getMyMission(Long id){
		this.open();
		Mission mission = null;

		Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, "_id=?", new String[]{id.toString()},
				null, null, null);

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			mission = cursorToMission(cursor);
		}

		cursor.close();
		this.close();
		return mission;
	}
}
