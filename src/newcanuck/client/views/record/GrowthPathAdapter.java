package newcanuck.client.views.record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import newcanuck.client.localDatabase.datasource.FeelingDatabaseDatasource;
import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
import newcanuck.client.utility.FilePathManager;
import newcanuck.client.utility.GlobalParams;
import newcanuck.client.views.R;
import newcanuck.entity.Feeling;
import newcanuck.entity.Mission;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class GrowthPathAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Feeling> feelings;
	private List<Mission> missions;
	private MyMissionDatabaseDatasource myMissionDatabaseDatasource;

	public GrowthPathAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		missions = new LinkedList<Mission>();
		myMissionDatabaseDatasource = new MyMissionDatabaseDatasource(context);
	}

	public void setFeelings(List<Feeling> feelings) {
		this.feelings = feelings;
		setMissions();
	}

	@Override
	public int getCount() {
		if (feelings != null)
			return feelings.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemHolder holder = null;
		if (convertView == null) {

			holder = new ListItemHolder();

			convertView = mInflater.inflate(
					R.layout.activity_growth_path_list_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.txtName);
			holder.time = (TextView) convertView.findViewById(R.id.txtTime);
			holder.feelings = (TextView) convertView
					.findViewById(R.id.txtRating);
			convertView.setTag(holder);

		} else {
			holder = (ListItemHolder) convertView.getTag();
		}

		// load mission of this position
		Feeling feeling = feelings.get(position);
		Mission mission = missions.get(position);

		holder.name.setText(feeling.getMissionName());

		String filePath = FilePathManager.getCachePath()+mission.getImgFileName()+"_s.jpg"; 
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		holder.img.setImageBitmap(bitmap);

		// time info
		Date taken_date = new Date(feeling.getCreateDate());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd",
				Locale.US);
		String date_info = dateFormat.format(taken_date);
		holder.time.setText(date_info);

		holder.feelings.setText("Feeling: " + feeling.getMyFeeling().toString());

		return convertView;
	}

	public List<Mission> getMissions() {
		return missions;
	}

	public void setMissions() {
		for (Feeling feeling :feelings){
			missions.add(myMissionDatabaseDatasource.getMyMission(feeling.getMissionId()));
		}
	}

	static class ListItemHolder {
		public TextView name;
		public ImageView img;
		public TextView feelings;
		public TextView time;
	}

}
