package com.jiangjiesheng.imageloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jiangjiesheng.imageloader.config.ImageLoaderInit;
import com.jiangjiesheng.imageloader.entity.PhotoModel;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 类说明 ： 如果出现Id冲突 Clean ALL---------------------------------------
 * 配置见--CommonUtils.java;调用见--PhotoPreview.java;加载图片来源见--
 * BasePhotoPreviewActivity.java 后期定制可以从PhotoPreviewActivity.java 开始 运行本demo
 * project.properties 取消库 。 http://www.mincoder.com/article/3800.shtml
 * http://blog.csdn.net/lidec/article/details/50472177?locationNum=8
 * 
 * @author 江节胜 E-mail:dev@jiangjiesheng.com
 * @version 创建时间：2017年4月2日 下午2:18:10
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageloader_activity_main_demo);

	}

	@Override
	protected void onResume() {
		super.onResume();
		String URL1 = "http://www.jiangjiesheng.com/dev/resource/images/fullstackdeveloper/fullStackDeveloper-1.png";
		String URL2 = "http://www.jiangjiesheng.com/dev/resource/images/fullstackdeveloper/fullStackDeveloper-2.png";
		String URL3 = "http://www.jiangjiesheng.com/dev/resource/images/fullstackdeveloper/fullStackDeveloper-3.png";
		String URL4 = "http://www.jiangjiesheng.com/dev/resource/images/fullstackdeveloper/fullStackDeveloper-4.png";
		String URL5 = "http://www.jiangjiesheng.com/dev/resource/images/fullstackdeveloper/fullStackDeveloper-5.png";
		String URL6 = "http://pic124.nipic.com/file/20170321/12173849_104100807038_2.jpg";
		String URL7 = "http://pic125.nipic.com/file/20170320/12340304_212624455034_2.jpg";

		List<PhotoModel> list = new ArrayList<PhotoModel>();
		list.add(new PhotoModel(URL1, null));
		list.add(new PhotoModel(URL2, null));
		list.add(new PhotoModel(URL3, null));
		list.add(new PhotoModel(URL4, null));
		list.add(new PhotoModel(URL5, null));
		list.add(new PhotoModel(URL6, null));
		list.add(new PhotoModel(URL7, null));
		Bundle b = new Bundle();
		b.putSerializable("photos", (Serializable) list);
		b.putInt("position", 2);
		//if (!ImageLoader.getInstance().isInited()) { //这里就不要判断了 这里初始化的配置可能和其他页面初始化的冲突
			ImageLoaderInit.initImageLoader(this, "imageloader/Cache3", false);// 这里设置独立路径
		//}
		launchActivity(this, PhotoPreviewActivity.class, b);

		// 如果需要从右向左的转场平滑动画 BasePhotoPreviewActivity.java中注释掉
		// overridePendingTransition(R.anim.activity_alpha_action_in, 0); //
		// 最好同时注释掉intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		// 不要转场动画则最好加上以上两项

	}

	/**
	 * 开启activity(带参数)
	 */
	public static void launchActivity(Context context, Class<?> activity,
			Bundle bundle) {
		Intent intent = new Intent(context, activity);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivity(intent);
	}
}
