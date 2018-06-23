package com.jiangjiesheng.imageloader.ui;

import java.io.Console;
import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jiangjiesheng.imageloader.R;
import com.jiangjiesheng.imageloader.config.ImageLoaderInit;
import com.jiangjiesheng.imageloader.entity.PhotoModel;
import com.jiangjiesheng.imageloader.libs.GestureImageView;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 类说明 ：自定义图片显示控件
 * 
 * @author 江节胜 E-mail:dev@jiangjiesheng.com
 * @version 创建时间：2017年4月2日 下午6:44:31
 * 
 */
public class PhotoPreview extends LinearLayout implements OnClickListener {

	private String tag = "PhotoPreview";
	private ProgressBar pbLoading;
	private GestureImageView ivContent;
	private OnClickListener l;
	private Context ctx;

	public PhotoPreview(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(
				R.layout.imageloader_gestureimageview, this, true);
		this.ctx = context;
		pbLoading = (ProgressBar) findViewById(R.id.imageloader_pb_loading_vpp);
		ivContent = (GestureImageView) findViewById(R.id.imageloader_iv_content_vpp);
		ivContent.setOnClickListener(this);
	}

	public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
		this(context);
	}

	public PhotoPreview(Context context, AttributeSet attrs) {
		this(context);
	}

	// 参数
	// String imageUri = "http://site.com/image.png"; // from Web
	// String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
	// String imageUri = "content://media/external/audio/albumart/13"; // from
	// content provider
	// String imageUri = "assets://image.png"; // from assets
	// String imageUri = "drawable://" + R.drawable.image; // from drawables
	// (only images, non-9patch)
	/**
	 * 方法说明：支持保存到本地缓存支持自定义缓存路径,支持缓存文件保持原名
	 * 
	 * @author 江节胜 ,E-mail:dev@jiangjiesheng.com
	 * @version 创建时间：2017年4月2日 下午11:46:23
	 * 
	 * @param photoModel
	 */
	public void loadImagefromWeb(PhotoModel photoModel) {
		if (photoModel.getUrl() != null && !photoModel.getUrl().isEmpty()) {
			String imgPath = isImageExist(photoModel.getUrl());
			Log.e(tag, "已存在的图片：" + imgPath);
			if (imgPath != null && !imgPath.isEmpty()) {
				loadImagefromSDCard(new PhotoModel(imgPath));
			} else {
				loadImage(photoModel.getUrl());
			}
		} else {
			showToast(getContext(), errorMsg);
		}
	}

	public void loadImagefromSDCard(PhotoModel photoModel) {
		if (photoModel.getOriginalPath() != null
				&& !photoModel.getOriginalPath().isEmpty()) {
			loadImage("file://" + photoModel.getOriginalPath());
		} else {
			showToast(getContext(), errorMsg);
		}
	}

	public void loadImagefromContentProvider(PhotoModel photoModel) {
		if (photoModel.getProviderUri() != null
				&& !photoModel.getProviderUri().isEmpty()) {
			if (!photoModel.getProviderUri().startsWith("content://")) {
				loadImage("content://" + photoModel.getProviderUri());
			} else {
				loadImage(photoModel.getProviderUri());
			}
		} else {
			showToast(getContext(), errorMsg);
		}
	}

	public void loadImagefromAssets(PhotoModel photoModel) {
		if (photoModel.getAssetsPath() != null
				&& !photoModel.getAssetsPath().isEmpty()) {
			if (!photoModel.getAssetsPath().startsWith("assets://")) {
				loadImage("assets://" + photoModel.getAssetsPath());
			} else {
				loadImage(photoModel.getAssetsPath());
			}
		} else {
			showToast(getContext(), errorMsg);
		}
	}

	public void loadImagefromDrawable(PhotoModel photoModel) {
		if (photoModel.getDrawablePath() != null
				&& !photoModel.getDrawablePath().isEmpty()) {
			if (!photoModel.getDrawablePath().startsWith("drawable://")) {
				loadImage("drawable://" + photoModel.getDrawablePath());
			} else {
				loadImage(photoModel.getDrawablePath());
			}
		} else {
			showToast(getContext(), errorMsg);
		}
	}

	/**
	 * 方法说明： Android-Universal-Image-Loader 图片加载开源库
	 * 首先必须CommonUtils.initImageLoader，然后
	 * ImageLoader.getInstance().loadImage()。(多种方法可供调用)
	 * 
	 * @author 江节胜 ,E-mail:dev@jiangjiesheng.com
	 * @version 创建时间：2017年4月2日 下午1:57:46
	 * 
	 * @param path
	 */
	private void loadImage(String path) { // Android-Universal-Image-Loader 用法 ：
											// http://blog.csdn.net/vipzjyno1/article/details/23206387
		/**
		 * 如果这里还报错，直接返回
		 */
		if (!ImageLoader.getInstance().isInited()) {// 防止空指针 设置 默认路径
			ImageLoaderInit.initImageLoader(ctx, null, false);
		}

		ImageLoader.getInstance().loadImage(path,
				new SimpleImageLoadingListener() { // 可以使用ImageLoadingListener
					// 监听 (更详细)
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						ivContent.setImageBitmap(loadedImage);
						pbLoading.setVisibility(View.GONE);
						Log.e("PhotoPreview", "onLoadingComplete--imageUri:"
								+ imageUri);// imageUri 是参数
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						ivContent.setImageDrawable(getResources().getDrawable(
								R.drawable.ic_picture_loadfailed));
						pbLoading.setVisibility(View.GONE);
					}
				});
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}
 
	/**
	 * 方法说明：专用于设置缓存时保存原图片名称时不能优先读取缓存文件
	 * @author 江节胜 ,E-mail:dev@jiangjiesheng.com
	 * @version 创建时间：2017-4-5 下午1:05:02 
	 *
	 * @param path
	 * @return
	 */
	private String isImageExist(String path) {
		DiskCache mDiskCache = ImageLoader.getInstance().getDiskCache();
		File folderPath = mDiskCache.getDirectory();
		String imageName = ImageLoaderInit.getImageName(path);
		Log.e("PhotoPreview", "isImageExist--imageName:" + imageName);
		String imagePath = folderPath.getAbsolutePath() + File.separator
				+ imageName;
		Log.e("PhotoPreview", "isImageExist--imagePath:" + imagePath);
		File f = new File(imagePath);
		if (f != null && f.exists() && f.canRead()) {
			return imagePath;
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imageloader_iv_content_vpp && l != null)
			l.onClick(ivContent);
	};

	private String errorMsg = "图片资源类型有误";
	private Toast mToast;

	private void showToast(Context ctx, String msg) {
		if (ctx == null) {
			return;
		}
		if (mToast == null)
			mToast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
		else {
			mToast.setText(msg);
		}
		mToast.show();
	}
}
