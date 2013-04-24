package newcanuck.client.tools.lazyloaderdemo.cache;
import newcanuck.client.utility.FilePathManager;


import android.content.Context;

public class FileCache extends AbstractFileCache{

	public FileCache(Context context) {
		super(context);
	
	}


	@Override
	public String getSavePath(String url) {
		String[] strs = url.split("/");
		String fileName = strs[strs.length-1];
		return getCacheDir() + fileName;
	}

	@Override
	public String getCacheDir() {
		
		return FilePathManager.getCachePath();
	}

}
