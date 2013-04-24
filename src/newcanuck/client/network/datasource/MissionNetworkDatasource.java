package newcanuck.client.network.datasource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import newcanuck.client.network.HttpModule;
import newcanuck.client.utility.GlobalParams;
import newcanuck.entity.Mission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class MissionNetworkDatasource {
	
	public List<Mission> getAllMissionsFromServer() {
		final String url = GlobalParams.URL_GET_ALL_MISSIONS;
		
		try {
			List<Mission> missions = new LinkedList<Mission>();
			JSONArray jsonArray = HttpModule.getJsonDataFromServer(url);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				
				Mission mission = getMissionFromJson(json);
				missions.add(mission);
			}
			
			return missions;
			
		} catch (Exception e) {
			return null;
		}
	}

	public String postMission(Mission mission, Bitmap pic){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", mission.getName());
		map.put("description", mission.getDescription());
		map.put("create_date", mission.getCreateDate());
		map.put("address", mission.getAddress());
		map.put("latitude", mission.getLatitude());
		map.put("longitude", mission.getLongitude());
		
		return HttpModule.postJsonDataToServerWithBitmap(GlobalParams.URL_POST_MISSION,map,pic);
	}
	
	private Mission getMissionFromJson(JSONObject json) throws JSONException {
		
		Mission mission = new Mission();
		mission.setId(json.getLong("id"));
		mission.setName(json.getString("name"));
		mission.setCreateDate(json.getLong("createDate"));
		mission.setRatetimes(json.getLong("ratetimes"));
		mission.setRating(json.getDouble("rating"));
		mission.setLatitude(json.getDouble("latitude"));
		mission.setLongitude(json.getDouble("longitude"));
		mission.setAddress(json.getString("address"));
		mission.setDescription(json.getString("description"));
		mission.setState(0l);
		mission.setImgFileName(json.getString("imgFileName"));
		
		return mission;
	}
}
