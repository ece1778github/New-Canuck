package newcanuck.client.views.mission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
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
import android.widget.Toast;




public class AllMissionLoaderAdapter extends BaseAdapter{

	private boolean mBusy = false;
	private LayoutInflater mInflater;
	

	private MyMissionDatabaseDatasource myMissionDatabaseDatasource;
	
	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	
	private ImageLoader mImageLoader;
	private List<Mission> missions;
	private Context mContext;
	
	public AllMissionLoaderAdapter(Context context) {
		mContext = context;
		mImageLoader = new ImageLoader(context);
		this.mInflater = LayoutInflater.from(context);

		myMissionDatabaseDatasource = new MyMissionDatabaseDatasource(context);
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

			convertView = mInflater.inflate(R.layout.activity_all_mission_list_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.txtName);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.location = (TextView) convertView
					.findViewById(R.id.txtAddress);
			holder.time = (TextView) convertView.findViewById(R.id.txtTime);
			holder.rating = (TextView) convertView.findViewById(R.id.txtRating);
			holder.addBtn = (Button) convertView.findViewById(R.id.addBtn);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
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

		// location info
		holder.location.setText("Location: "+mission.getAddress());

		// time info
		Date taken_date = new Date(mission.getCreateDate());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd", Locale.US);
		String date_info = dateFormat.format(taken_date);
		holder.time.setText(date_info);
		
		holder.rating.setText("Rating: "+mission.getRating().toString());
		holder.ratingBar.setRating(mission.getRating().floatValue());

		holder.addBtn.setTag(mission);

		// add button logic
		holder.addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Mission mission = (Mission) v.getTag();
				
				List<Mission> missions = myMissionDatabaseDatasource.getAllMyMissions();
				if(missionIsNotBeingTaken(mission,missions)){
					mission.setAddDate(new Date().getTime());
					myMissionDatabaseDatasource.insertMission(mission);
					//createImageFiles(mission);
					Toast.makeText(mContext, "Add mission sucessfully", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(mContext, "You are taking the mission", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			/*
			private void createImageFiles(Mission mission) {
				
				String url = GlobalParams.URL_USER_IMAGE_SMALL_PREFIX+mission.getImgFileName()+"_s.jpg";
				
				
				//get the image file in cache
				String cacheRootDir = FileManager.getCachePath();
				String cacheFilePath = cacheRootDir + mission.getImgFileName() + "_s.jpg";
				
				Bitmap smallImg = BitmapFactory.decodeFile(cacheFilePath);
				
				//if dir exists, do nothing, else create the dir
				String userImgDir = FileManager.getUserImagePath();
				boolean success = FileHelper.createDirectory(userImgDir);

				try {
					if (success) {
						String userImageRootDir = FileManager.getUserImagePath();
						String imageFilePath = userImageRootDir + mission.getImgFileName() + "_s.jpg";
						File imgFile = new File(imageFilePath);
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imgFile));
						smallImg.compress(Bitmap.CompressFormat.JPEG, 100, bos);
						bos.flush();
						bos.close();
					}
				} catch (Exception e) {
					Log.e("error","fail in creating img");
				}
			}
			*/
			
			private boolean missionIsNotBeingTaken(Mission mission,
					List<Mission> missions) {
				for(Mission m : missions){
					if(m.getId().equals(mission.getId()))
						return false;
				}
				return true;
			}
		});
		
		return convertView;
		
	}

	static class ListItemHolder {
		public TextView name;
		public ImageView img;
		public TextView rating;
		public TextView location;
		public TextView time;
		public Button addBtn;
		public RatingBar ratingBar;
	}
}
