package newcanuck.client.network.datasource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import newcanuck.client.network.HttpModule;
import newcanuck.client.utility.GlobalParams;
import newcanuck.entity.Comment;

public class CommentNetworkDatasource {
	public String postComment(Comment comment){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("missionId", comment.getMissionId());
		map.put("userName", comment.getUserName());
		map.put("comment", comment.getUserComment());
		map.put("rating", comment.getUserRating());
		map.put("createDate", comment.getCreateDate()); 
		return HttpModule.postJsonDataToServer(GlobalParams.URL_POST_COMMENT,map);
	}
	
	public List<Comment> getCommentsFromServer(Long missionId) {
		final String url = GlobalParams.URL_GET_COMMENTS;
		
		try {
			List<Comment> comments = new LinkedList<Comment>();
			JSONArray jsonArray = HttpModule.getJsonDataFromServer(url+"?missionId="+missionId);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				
				Comment comment = getCommentFromJson(json);
				comments.add(comment);
			}
			
			return comments;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Comment getCommentFromJson(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		Comment comment = new Comment();
		comment.setCreateDate(json.getLong("createDate"));
		comment.setMissionId(json.getLong("missionId"));
		comment.setId(json.getLong("id"));
		comment.setUserComment(json.getString("userComment"));
		comment.setUserName(json.getString("userName"));
		comment.setUserRating(json.getDouble("userRating"));
		
		return comment;
	}
}
