package newcanuck.client.views.mission;

import java.util.Date;
import java.util.List;
import java.util.Map;

import newcanuck.client.network.datasource.CommentNetworkDatasource;
import newcanuck.client.userProfile.UserProfile;
import newcanuck.client.utility.FilePathManager;
import newcanuck.client.views.R;
import newcanuck.entity.Comment;
import newcanuck.entity.Feeling;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FeelingActivity extends Activity {

	private Feeling feeling;
	
	private CommentNetworkDatasource commentNetworkDatasource;
	
	private TextView textViewMissionName;
	private TextView textViewAddress;
	private TextView textViewMyRating;
	private TextView textViewFeeling;
	private ImageView imageView;
	private RatingBar ratingBar;
	
	private Button btnShareFeeling;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feeling_main);
		
		commentNetworkDatasource = new CommentNetworkDatasource();
		
		textViewMissionName = (TextView)findViewById(R.id.textViewMissionName);
		textViewFeeling = (TextView)findViewById(R.id.textViewFeeling);
		textViewAddress = (TextView)findViewById(R.id.textViewAddress);
		textViewMyRating = (TextView)findViewById(R.id.textViewMyRating);
		ratingBar = (RatingBar)findViewById(R.id.ratingBar);

		feeling = (Feeling) getIntent().getSerializableExtra("feeling");
		textViewMissionName.setText(feeling.getMissionName());
		textViewFeeling.setText(feeling.getMyFeeling());
		textViewMyRating.setText(feeling.getMyRating().toString());
		textViewAddress.setText(feeling.getMissionAddress());
		ratingBar.setRating(feeling.getMyRating().floatValue());
		
		RelativeLayout imageFram = (RelativeLayout) this.findViewById(R.id.imageFram);
		imageView = new ImageView(this);
		String filePath = FilePathManager.getUserImagePath()+feeling.getMyImgFileName(); 
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		imageView.setImageBitmap(bitmap);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 10, 10, 0);
		imageFram.addView(imageView);
		imageView.setLayoutParams(lp);
		imageView.requestLayout();
		
		btnShareFeeling = (Button)findViewById(R.id.buttonShareToServer);
		btnShareFeeling.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				FeelingActivity.this.setTitle("Sending My Feeling...");
				
				PostCommentASyncTask task = new PostCommentASyncTask();
				task.execute();
			}
			
		});
	}
	
	public class PostCommentASyncTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {

		private String result;
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			Comment comment = new Comment();
			comment.setMissionId(feeling.getMissionId());
			comment.setUserComment(feeling.getMyFeeling());
			comment.setUserName(UserProfile.getProfile(FeelingActivity.this).getUserName());
			comment.setUserRating(feeling.getMyRating());
			comment.setCreateDate(new Date().getTime());
			result = commentNetworkDatasource.postComment(comment);
			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> out) {
			//update widget
			Toast.makeText(FeelingActivity.this, (CharSequence) result, Toast.LENGTH_SHORT).show();

			FeelingActivity.this.setTitle("My Feeling");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feeling_main, menu);
		return true;
	}

}
