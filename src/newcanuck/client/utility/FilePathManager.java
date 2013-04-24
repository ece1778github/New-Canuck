package newcanuck.client.utility;

public class FilePathManager {

	public static String getCachePath() {
		return CommonUtil.getRootFilePath() + "newcanuck/cache/";
	}

	public static String getUserImagePath() {
		return CommonUtil.getRootFilePath() + "newcanuck/user_images/";
	}

	public static String getTempPath() {
		return CommonUtil.getRootFilePath() + "newcanuck/temp/";
	}
}

