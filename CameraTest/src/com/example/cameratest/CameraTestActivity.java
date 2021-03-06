package com.example.cameratest;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class CameraTestActivity extends Activity implements SurfaceHolder.Callback {
	
	int photoNum = 0;
	SurfaceHolder surfaceHolder;
	SurfaceView svCameraPreview;
	Button btnShoot, btnBack;
	ImageView ivShoot1, ivShoot2, ivShoot3;
	ImageView ivTitle;
	Camera camera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//use fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera_test);
		//set screen orientation
		setRequestedOrientation(0);
		//set up buttons
		btnShoot = (Button)findViewById(R.id.btn_shoot);
		btnBack = (Button)findViewById(R.id.btn_back);
		btnShoot.setOnClickListener(myOnClickListener);
		btnBack.setOnClickListener(myOnClickListener);
		
		ivShoot1 = (ImageView)findViewById(R.id.iv_shoot1);
		ivShoot2 = (ImageView)findViewById(R.id.iv_shoot2);
		ivShoot3 = (ImageView)findViewById(R.id.iv_shoot3);
		ivTitle = (ImageView)findViewById(R.id.iv_title);
		svCameraPreview=(SurfaceView)findViewById(R.id.sv_camera_preview);
		surfaceHolder = svCameraPreview.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		
	}
	
	OnClickListener myOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.btn_shoot && photoNum<3)
				camera.autoFocus(afcb);
			else if(v.getId()==R.id.btn_back) {
				switch(photoNum) {
				case 1:
					ivShoot1.setImageResource(R.drawable.shoot1);
					ivTitle.setBackgroundColor(Color.parseColor("#FF95B9C7"));
					ivTitle.setImageResource(R.drawable.title_step1);
					photoNum--;
					break;
				case 2:
					ivShoot2.setImageResource(R.drawable.shoot2);
					ivTitle.setBackgroundColor(Color.parseColor("#FF3090C7"));
					ivTitle.setImageResource(R.drawable.title_step2);
					photoNum--;
					break;
				case 3:
					ivShoot3.setImageResource(R.drawable.shoot3);
					ivTitle.setBackgroundColor(Color.parseColor("#FF2B60DE"));
					ivTitle.setImageResource(R.drawable.title_step3);
					photoNum--;
					break;
				default:
					break;
				}
				
			}
			
		}
		
	};
	
	PictureCallback jpeg = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			switch(photoNum) {
			case 0:
				Bitmap bmp1Raw = BitmapFactory.decodeByteArray(data,0, data.length);
				Bitmap bmp1 = resizeBitmapToSquare(bmp1Raw);
				ivShoot1.setImageBitmap(bmp1);
				ivTitle.setBackgroundColor(Color.parseColor("#FF3090C7"));
				ivTitle.setImageResource(R.drawable.title_step2);
				photoNum++;
				break;
			case 1:
				Bitmap bmp2Raw = BitmapFactory.decodeByteArray(data,0, data.length);
				Bitmap bmp2 = resizeBitmapToSquare(bmp2Raw);
				ivShoot2.setImageBitmap(bmp2);
				ivTitle.setBackgroundColor(Color.parseColor("#FF2B60DE"));
				ivTitle.setImageResource(R.drawable.title_step3);
				photoNum++;
				break;
			case 2:
				Bitmap bmp3Raw = BitmapFactory.decodeByteArray(data,0, data.length);
				Bitmap bmp3 = resizeBitmapToSquare(bmp3Raw);
				ivShoot3.setImageBitmap(bmp3);
				ivTitle.setBackgroundColor(Color.parseColor("#FF2DFF49"));
				ivTitle.setImageResource(R.drawable.title_finish);
				photoNum++;
				break;
			default:
				break;
			}

			//need start preview, otherwise surfaceView will have a screen lock
			camera.startPreview();
		}
		
	};
	
	private Bitmap resizeBitmapToSquare(Bitmap bitmap) {
		int startHeight, startWidth;
		int edgeLength;
		
		if(bitmap.getWidth()>bitmap.getHeight()) {
			edgeLength = bitmap.getHeight();
			startHeight = 0;
			startWidth = (bitmap.getWidth()-bitmap.getHeight())/2;
		} else {
			edgeLength = bitmap.getWidth();
			startWidth = 0;
			startHeight = (bitmap.getHeight()-bitmap.getWidth())/2;
		}
		return Bitmap.createBitmap(bitmap, startWidth, startHeight, edgeLength, edgeLength);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		try {
//			Camera.Parameters parameters = camera.getParameters();
//			//in order to get square preview size
//			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
//			Camera.Size previewSize = previewSizes.get(0);
//			for(int i=0;i<previewSizes.size();i++) {
//				if(previewSizes.get(i).width==previewSizes.get(i).height)
//					previewSize = previewSizes.get(i);
//			}
//			Log.d("mmpud", "width:" + previewSize.width + ", height:" + previewSize.height);
//			int previewEdgeLength;
//			if (previewSize.width>=previewSize.height) {
//				previewEdgeLength = previewSize.height;
//			} else {
//				previewEdgeLength = previewSize.width;
//			}
//			parameters.setPreviewSize(previewEdgeLength, previewEdgeLength);
//			camera.setParameters(parameters);
//			camera.setPreviewDisplay(surfaceHolder);
//			camera.startPreview();
			Camera.Parameters parameters = camera.getParameters();
			//in order to get square preview size
			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
			Camera.Size previewSize = previewSizes.get(0);
			for(int i=0;i<previewSizes.size();i++) {
				if(previewSizes.get(i).width==previewSizes.get(i).height)
					previewSize = previewSizes.get(i);
			}		
			parameters.setPreviewSize(previewSize.width, previewSize.height);
			camera.setParameters(parameters);
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		System.out.println("surfaceDestroyed");
		camera.stopPreview();
		camera.release();
	}
	
	AutoFocusCallback afcb = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			if(success) {
				camera.takePicture(null, null, jpeg);
			}
		}
	};
	
}
