package newcanuck.client.tools.CameraManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import newcanuck.client.utility.FilePathManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class CameraManager {

	private static final int PICTURE_SIZE = 750;
	private final String TEMP_FILE_NAME = "tempImg";
	private File imgTempFile;
	private File tempFilesDir;
	private Activity mActivity;
	private Bitmap cameraImg;
	
	public CameraManager(Activity activity) {
		mActivity = activity;
		
		tempFilesDir = new File(FilePathManager.getTempPath());
		if (!tempFilesDir.exists()) {
			tempFilesDir.mkdirs();
		}

		imgTempFile = new File(tempFilesDir, TEMP_FILE_NAME);
	}
	
	//start a camera software to take a picture. 
	//the temp pic will be stored in temp dir.
	public void startCameraActivityForResult(int resultCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgTempFile));
		mActivity.startActivityForResult(intent, resultCode);
	}
	
	//start a camera
	public void startPickingImageFileActivityForResult(int resultCode){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        mActivity.startActivityForResult(intent,resultCode);
	}
	
	//solve the bug of incorrect direction of the pic.
	private int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	//convert the file pic to a smallerImg. Store in the object cameraImg.
	public void generateImageFromFileSystem(String filePath){
		File file = new File(filePath);
		generateImg(file);
	}
	
	//convert the temp pic to a smallerImg. Store in the object cameraImg.
	public void generateImageFromCamera(){
		File file = imgTempFile;
		generateImg(file);
	}

	private void generateImg(File file) {
		if(cameraImg != null){
			cameraImg.recycle();
			cameraImg = null;
		}
			
		Bitmap originalImg = null;
		Uri contentUri = Uri.fromFile(file);

		try {
			originalImg = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), contentUri);

			int b_width = originalImg.getWidth();
			int b_height = originalImg.getHeight();

			// make a smaller image, without rotate
			Bitmap resize = Bitmap.createScaledBitmap(originalImg,
					PICTURE_SIZE, (int) (PICTURE_SIZE * (b_height / (float) b_width)),
					true);
			int width = resize.getWidth();
			int height = resize.getHeight();

			// fix the bug of no auto rotating
			Matrix m = new Matrix();
			int degree = readPictureDegree(contentUri.getPath());
			m.setRotate(degree);

			// create a smaller img, will be saved
			Bitmap smallerImg = Bitmap.createBitmap(resize, 0, 0,
					width, height, m, true);
			
			cameraImg = smallerImg;
			
		} catch (Exception e) {
			Log.d("Error", "Error Message: " + e);
			cameraImg = null;
		}
	}
	
	//store the camera image in the user image file.
	public void storeThePicture(String fileName) throws Exception{
		File dir = new File(FilePathManager.getUserImagePath());
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File file = new File(dir,fileName);
		BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(file));
		cameraImg.compress(Bitmap.CompressFormat.JPEG, 100, bos2);
		bos2.flush();
		bos2.close();
	}
	
	public boolean picExist(){
		return cameraImg != null;
	}
	
	public Bitmap getCameraImage(){
		return cameraImg;
	}
	
	public File getTempFile(){
		return imgTempFile;
	}
}
