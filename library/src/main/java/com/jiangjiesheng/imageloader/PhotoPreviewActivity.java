package com.jiangjiesheng.imageloader;

import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

import com.jiangjiesheng.imageloader.config.ImageResource;
import com.jiangjiesheng.imageloader.entity.PhotoModel;
import com.jiangjiesheng.imageloader.ui.BasePhotoPreviewActivity;

/**
 * 类说明 ：demo样板文件,正式使用时都继承BasePhotoPreviewActivity.java
 * @author 江节胜 E-mail:dev@jiangjiesheng.com 
 * @version 创建时间：2017年4月3日 上午12:04:43 
 *
 */
public class PhotoPreviewActivity extends BasePhotoPreviewActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init(getIntent().getExtras());
		
		setIsShowBottomMenu(true);//设置底部菜单默认不显示
		setImageResource(ImageResource.FromWeb);//设置读取图片的类型
	}

	@SuppressWarnings("unchecked")
	protected void init(Bundle extras) {
		if (extras == null)
			return;

		if (extras.containsKey("photos")) { // 预览图片
			photos = (List<PhotoModel>) extras.getSerializable("photos");
			current = extras.getInt("position", 0);
			updatePercent();
			bindData();
		}
	}

	// 测试
	@Override
	protected void doShowMenu(boolean isUp) {
		super.doShowMenu(isUp);
//		Toast.makeText(getApplicationContext(),
//				"菜单" + (isUp == false ? "显示" : "关闭"), 0).show();
	}

	@Override
	protected void clickShare(int position) {
		super.clickShare(position);
		Toast.makeText(getApplicationContext(), "点击分享" + position, 0).show();
	}

	@Override
	protected void clickDelete(int position) {
		super.clickDelete(position);
		Toast.makeText(getApplicationContext(), "点击删除" + position, 0).show();
	}

}
