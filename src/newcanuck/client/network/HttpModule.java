package newcanuck.client.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

public class HttpModule {

	private final static String EXCEPTION_MSG = "Connection failed. Please check your internet.";

	public static JSONArray getJsonDataFromServer(String url) throws Exception {

		String out = getRawDataFromServer(url);
		return new JSONArray(out);
	}

	public static List<Map<String, Object>> getDataListFromServer(String url)
			throws Exception {

		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();

		JSONArray jsonArray = getJsonDataFromServer(url);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			Map<String, Object> map = new HashMap<String, Object>();

			Iterator<?> itr = json.keys();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				map.put(key, json.get(key));
			}
			list.add(map);
		}
		return list;
	}

	public static String postJsonDataToServer(String url,
			Map<String, Object> map) {

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : map.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, map.get(key)
					.toString()));
		}

		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // time
																		// out
																		// for
																		// response
		HttpConnectionParams.setSoTimeout(httpParams, 60000); // time out for
																// finishing
																// getting all
																// data

		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(url);

		String out = null;

		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					out = EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
			return "Internet inavailable or no data";
		}

		return out;
	}

	public static String postJsonDataToServerWithBitmap(String url,
			Map<String, Object> map, Bitmap bitmap) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); // compress to
																	// which
																	// format
																	// you want.
		String image_str = Base64.encodeToString(stream.toByteArray(),
				Base64.DEFAULT);

		try {
			stream.close();
		} catch (IOException e) {
			Log.v("HttpModule ERROR", e.getMessage());
		}

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("image", image_str));
		image_str = null;

		for (String key : map.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, map.get(key)
					.toString()));
		}

		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // time
																		// out
																		// for
																		// response
		HttpConnectionParams.setSoTimeout(httpParams, 60000); // time out for
																// finishing
																// getting all
																// data

		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(url);

		String out = null;

		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					out = EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
			return "Internet inavailable or no data";
		}

		return out;
	}

	public static String getRawDataFromServer(String url) throws Exception {

		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // time
																		// out
																		// for
																		// response
		HttpConnectionParams.setSoTimeout(httpParams, 60000); // time out for
																// finishing
																// getting all
																// data

		HttpClient client = new DefaultHttpClient(httpParams);
		HttpGet request;

		try {
			request = new HttpGet(new URI(url));
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity);
					return out;
				}
			}
			throw new Exception(EXCEPTION_MSG);

		} catch (Exception e) {
			throw new Exception(EXCEPTION_MSG);
		}
	}

}