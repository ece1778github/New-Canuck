package newcanuck.client.views.mission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import newcanuck.client.tools.lazyloaderdemo.ImageLoader;
import newcanuck.client.utility.GlobalParams;
import newcanuck.client.views.R;
import newcanuck.entity.Mission;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyMissionLoaderAdapter extends BaseAdapter{

	private boolean mBusy = false;

	private LayoutInflater mInflater;
	
	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	private ImageLoader mImageLoader;
	private List<Mission> missions;
	
	public MyMissionLoaderAdapter(Context context) {
		mImageLoader = new ImageLoader(context);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public void setMissions(List<Mission> missions){
		this.missions = missions;
	}
	
	public ImageLoader getImageLoader(){
		return mImageLoader;
	}

	@Override
	public int getCount() {
		if(missions != null)
			return missions.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ListItemHolder holder = null;
		if (convertView == null) {

			holder = new ListItemHolder();

			convertView = mInflater.inflate(R.layout.activity_my_mission_list_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.txtName);
			holder.location = (TextView) convertView
					.findViewById(R.id.txtAddress);
			holder.time = (TextView) convertView.findViewById(R.id.txtTime);
			holder.rating = (TextView) convertView.findViewById(R.id.txtRating);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
			holder.state = (Button) convertView.findViewById(R.id.stateBtn);
			convertView.setTag(holder);

		} else {
			holder = (ListItemHolder) convertView.getTag();
		}
		
		//load mission of this position
		Mission mission = missions.get(position);
		
		String url = GlobalParams.URL_USER_IMAGE_SMALL_PREFIX+mission.getImgFileName()+"_s.jpg";

		holder.name.setText(mission.getName());
		
		holder.img.setImageResource(R.drawable.default_img);
		if (!mBusy) {
			mImageLoader.DisplayImage(url, holder.img, false);
		} else {
			mImageLoader.DisplayImage(url, holder.img, false);		
		}
		
		if(mission.getState().equals(1L)){
			holder.state.setBackgroundResource(R.drawable.doing);
		}
		else{
			holder.state.setBackgroundResource(R.drawable.finish);
		}

		// location info
		holder.location.setText(mission.getAddress());

		// time info
		Date taken_date = new Date(mission.getCreateDate());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
		String date_info = dateFormat.format(taken_date);
		holder.time.setText(date_info);
		

		holder.rating.setText("Rating: "+mission.getRating().toString());
		holder.ratingBar.setRating(mission.getRating().floatValue());
		
		return convertView;
	}

	static class ListItemHolder {
		public TextView name;
		public ImageView img;
		public TextView rating;
		public TextView location;
		public TextView time;
		public RatingBar ratingBar;
		public Button state;
	}
}
