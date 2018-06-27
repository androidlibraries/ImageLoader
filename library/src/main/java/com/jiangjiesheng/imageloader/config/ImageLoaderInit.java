package com.jiangjiesheng.imageloader.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jiangjiesheng.imageloader.R;
import com.nostra13.universalimageloader.cache.disc.impl.BaseDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 通用工具类
 * 
 * @author chenww
 * 
 */
public class ImageLoaderInit {

	/**
	 * 方法说明：初始化ImageLoader 废弃的方法点击进入其中复制出来新的方法
	 * http://www.mincoder.com/article/3800.shtml
	 * http://blog.csdn.net/lidec/article/details/50472177?locationNum=8
	 * 
	 * @author Aotu-JS ,E-mail:js@jiangjiesheng.com
	 * @version 创建时间：2017年1月11日 上午10:02:46
	 * 
	 * @param ctx
	 * @param customCachePath
	 *            设置相对于内存卡根路径即可，不需要设置Environment.getExternalStorageDirectory(),
	 *            (storage/1499-CDD9/)之类的路径
	 */
	public static void initImageLoader(Context ctx, String customCachePath,
			boolean isKeepOriginalImageName) {

		final File cacheDir = StorageUtils
				.getOwnCacheDirectory(
						ctx,
						(customCachePath != null && !customCachePath.isEmpty()) ? customCachePath
								: "imageloader/Cache");// 自定义路径
		if (ctx != null) {
			DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder() //
					.considerExifParams(true) // 调整图片方向
					.resetViewBeforeLoading(true) // 载入之前重置ImageView
					.showImageOnLoading(R.drawable.ic_picture_loading) // 载入时图片设置为黑色
					.showImageOnFail(R.drawable.ic_picture_loadfailed) // 加载失败时显示的图片
					.cacheInMemory(false)// 161121 JS 新增调用 OOM问题
					.cacheOnDisk(true)// 161121 JS 新增调用 OOM问题 20170402标记：
										// 直接读本地文件最好设置false,读取网络图片，最好设置为true
					.delayBeforeLoading(0) // 载入之前的延迟时间

					// 以下是20170402补充
					// .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
					// .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//
					// 以上是20170402补充
					.build();
			ImageLoaderConfiguration config;
			if (isKeepOriginalImageName) {
				config = new ImageLoaderConfiguration.Builder(ctx)
						.defaultDisplayImageOptions(defaultDisplayImageOptions)
						.memoryCacheExtraOptions(480, 800).threadPoolSize(5)

						// 以下为补充 20170402补充
						.diskCacheSize(10 * 1024 * 1024)
						// 10 * 1024 * 1024
						// 设置无效
						.diskCacheFileCount(30)
						// 设置无效
						.diskCache(// 自定义缓存路径并保持原文件名
								new BaseDiscCache(new File(cacheDir
										.getAbsolutePath()), null,
										new FileNameGenerator() {

											@Override
											public String generate(
													String imageUri) {
												return getImageName(imageUri);
											}
										}) {
								})
						// 有效
						// UnlimitedDiskCache 不限制缓存大小（默认）
						// TotalSizeLimitedDiskCache (设置总缓存大小，超过时删除最久之前的缓存)
						// FileCountLimitedDiskCache
						// (设置总缓存文件数量，当到达警戒值时，删除最久之前的缓存。如果文件的大小都一样的时候，可以使用该模式)
						// LimitedAgeDiskCache (不限制缓存大小，但是设置缓存时间，到期后删除)
						// 以上diskCache*这三个参数会互相覆盖，只使用一个
						.imageDownloader(
								new BaseImageDownloader(ctx, 5 * 1000,
										30 * 1000)) // 设置有效
						.writeDebugLogs() // Remove for release app
						// 以上为补充
						.build();
			} else {
				config = new ImageLoaderConfiguration.Builder(ctx)
						.defaultDisplayImageOptions(defaultDisplayImageOptions)
						.memoryCacheExtraOptions(480, 800)
						.threadPoolSize(5)

						// 以下为补充 20170402补充
						.diskCacheSize(10 * 1024 * 1024)
						// 10 * 1024 * 1024
						// 设置无效
						.diskCacheFileCount(30)
						.diskCache(new UnlimitedDiscCache(cacheDir))
						// 以上diskCache*这三个参数会互相覆盖，只使用一个
						.imageDownloader(
								new BaseImageDownloader(ctx, 5 * 1000,
										30 * 1000)) // 设置有效
						.writeDebugLogs() // Remove for release app
						// 以上为补充
						.build();
			}

			ImageLoader.getInstance().init(config);
		}

	}

	// 未使用到
	public static boolean writeToFile(String targetFilePath,
			InputStream inputStream) throws IOException {
		Log.e("test", "writeToFile targetFilePath=" + targetFilePath);
		if (targetFilePath == null || targetFilePath.isEmpty()) {
			return false;
		}

		File f = new File(targetFilePath);
		if (!f.exists()) {
			f.createNewFile();
		}
		if (f == null || !f.canWrite()) {
			return false;
		}
		int byteCount = 0;

		byte[] bytes = new byte[inputStream.available()];

		FileOutputStream outputStream = new FileOutputStream(f);
		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);
		}
		if (inputStream != null)
			inputStream.close();
		if (outputStream != null)
			outputStream.close();
		return true;
	}

	public static void onClearMemoryClick(Context ctx, View view) {
		Toast.makeText(ctx, "清除内存缓存成功", Toast.LENGTH_SHORT).show();
		ImageLoader.getInstance().clearMemoryCache(); // 清除内存缓存
	}

	public static void onClearDiskClick(Context ctx, View view) {
		// Toast.makeText(ctx, "清除本地缓存成功", Toast.LENGTH_SHORT).show();
		ImageLoader.getInstance().clearDiskCache(); // 清除本地缓存
	}

	public static String getImageName(String imageUri) {
		int lastIndex = imageUri.lastIndexOf('/');
		if (!imageUri.contains("."))
			imageUri = imageUri + ".png";
		return imageUri.substring(lastIndex + 1);
	}
}
