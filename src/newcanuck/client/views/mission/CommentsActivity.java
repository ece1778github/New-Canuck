package newcanuck.client.views.mission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import newcanuck.client.network.datasource.CommentNetworkDatasource;
import newcanuck.client.userProfile.UserProfile;
import newcanuck.client.views.R;
import newcanuck.entity.Comment;
import newcanuck.entity.Mission;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class CommentsActivity extends Activity {

	private CommentNetworkDatasource commentNetworkDatasource;
	
	private ListView listViewComments;
	private ProgressBar progress;
	
	private List<Comment> comments;
	private String missionName;
	private Long missionId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		
		missionName = getIntent().getStringExtra("missionName");
		missionId = getIntent().getLongExtra(("missionId"),0L);
		commentNetworkDatasource = new CommentNetworkDatasource();
				
		initWidgets();
		
		GetCommentsASyncTask getTask = new GetCommentsASyncTask();
		getTask.execute();
	}

	private void initWidgets() {
		setTitle(missionName+" Comments");
		listViewComments = (ListView)findViewById(R.id.listView);
		progress = (ProgressBar)findViewById(R.id.progressBar);
		
		progress.setVisibility(View.VISIBLE);
		listViewComments.setVisibility(View.INVISIBLE);
		
		
	}
	
	public class GetCommentsASyncTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {

		private String result;
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			comments = commentNetworkDatasource.getCommentsFromServer(missionId);
			
			return null;
			
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> out) {
			DataAdapter adapter = new DataAdapter(CommentsActivity.this);
			listViewComments.setAdapter(adapter);
			progress.setVisibility(View.INVISIBLE);
			listViewComments.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_comments, menu);
		return true;
	}
	
	public class DataAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public DataAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if(comments == null)
				return 0;
			return comments.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ListItemHolder holder = null;
			if (convertView == null) {

				holder = new ListItemHolder();

				convertView = mInflater.inflate(R.layout.activity_comments_list_item, null);
				holder.userName = (TextView)convertView.findViewById(R.id.txtUserName);
				holder.comment = (TextView)convertView.findViewById(R.id.txtComment);
				holder.rating = (RatingBar)convertView.findViewById(R.id.ratingBar);
				holder.time = (TextView)convertView.findViewById(R.id.txtTime);
				convertView.setTag(holder);

			} else {
				holder = (ListItemHolder) convertView.getTag();
			}

			// load comment list item data
			Comment comment = comments.get(position);
			holder.userName.setText(comment.getUserName());
			holder.comment.setText(comment.getUserComment());
			holder.rating.setRating(comment.getUserRating().floatValue());
			
			Date taken_date = new Date(comment.getCreateDate());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm", Locale.US);
			String date_info = dateFormat.format(taken_date);
			holder.time.setText(date_info);
			
			return convertView;
		}
	}

	public final class ListItemHolder {
		public RatingBar rating;
		public TextView userName;
		public TextView comment;
		public TextView time;
	}

}
