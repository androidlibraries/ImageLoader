package com.jiangjiesheng.imageloader.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiangjiesheng.imageloader.R;
import com.jiangjiesheng.imageloader.entity.PhotoModel;
import com.jiangjiesheng.imageloader.libs.AnimationUtil;

/**
 * 类说明 ：可以用于定制页面的父类
 */
public class BasePhotoPreviewActivity extends Activity implements
		OnPageChangeListener, OnClickListener {

	private ViewPager mViewPager;
	private RelativeLayout layoutTop, layoutbottom;
	private ImageButton btnBack;
	private TextView tvPercent, tvshare, tvdelete;
	protected List<PhotoModel> photos;
	protected int current;
	private boolean isShowBottomMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.imageloader_activity_photopreview);
		layoutTop = (RelativeLayout) findViewById(R.id.imageloader_layout_top_app);
		btnBack = (ImageButton) findViewById(R.id.imageloader_btn_back_app);
		tvPercent = (TextView) findViewById(R.id.imageloader_tv_percent_app);
		mViewPager = (ViewPager) findViewById(R.id.imageloader_vp_base_app);

		// 测试
		layoutbottom = (RelativeLayout) findViewById(R.id.imageloader_layout_bottom_app);
		tvshare = (TextView) findViewById(R.id.imageloader_tv_bottom_share);
		tvshare.setOnClickListener(this);
		tvdelete = (TextView) findViewById(R.id.imageloader_tv_bottom_delete);
		tvdelete.setOnClickListener(this);

		btnBack.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);

		// overridePendingTransition(R.anim.activity_alpha_action_in, 0); //
		// 渐入效果

	}

	/** 绑定数据，更新界面 */
	protected void bindData() {
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(current);
	}

	private PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public int getCount() {
			if (photos == null) {
				return 0;
			} else {
				return photos.size();
			}
		}

		@Override
		public View instantiateItem(final ViewGroup container,
				final int position) {
			PhotoPreview photoPreview = new PhotoPreview(
					getApplicationContext());
			((ViewPager) container).addView(photoPreview);
			int currentImageResource = ImageResource.CurrentImageResource;
			if (currentImageResource == ImageResource.FromWeb) {
				photoPreview.loadImagefromWeb(photos.get(position));// 这里暂时设置从网络读取
			} else if (currentImageResource == ImageResource.FromSDCard) {
				photoPreview.loadImagefromSDCard(photos.get(position));
			} else if (currentImageResource == ImageResource.FromContentProvider) {
				photoPreview.loadImagefromContentProvider(photos.get(position));
			} else if (currentImageResource == ImageResource.FromAssets) {
				photoPreview.loadImagefromAssets(photos.get(position));
			} else if (currentImageResource == ImageResource.FromDrawable) {
				photoPreview.loadImagefromDrawable(photos.get(position));
			} else {
				photoPreview.loadImagefromWeb(photos.get(position));// 这里暂时设置从网络读取
			}
			photoPreview.setOnClickListener(photoItemClickListener);
			return photoPreview;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	};
	protected boolean isUp;

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.imageloader_btn_back_app) {// 不要使用switch
			setResult(RESULT_OK);//关键 这里决定返回状态
			finish();
		} else if (v.getId() == R.id.imageloader_tv_bottom_share) {
			clickShare(current);
		} else if (v.getId() == R.id.imageloader_tv_bottom_delete) {
			clickDelete(current);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		current = arg0;
		updatePercent();
	}

	protected void updatePercent() {
		tvPercent.setText((current + 1) + "/" + photos.size());
	}

	/** 图片点击事件回调 */
	private OnClickListener photoItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isUp) {
				new AnimationUtil(getApplicationContext(), R.anim.translate_up)
						.setInterpolator(new LinearInterpolator())
						.setFillAfter(true).startAnimation(layoutTop);
				isUp = true;
				// 测试
				if (isShowBottomMenu)
					new AnimationUtil(getApplicationContext(),
							R.anim.translate_down)
							.setInterpolator(new LinearInterpolator())
							.setFillAfter(true).startAnimation(layoutbottom);
			} else {
				new AnimationUtil(getApplicationContext(),
						R.anim.translate_down_current)
						.setInterpolator(new LinearInterpolator())
						.setFillAfter(true).startAnimation(layoutTop);
				isUp = false;
				// 测试
				if (isShowBottomMenu)
					new AnimationUtil(getApplicationContext(),
							R.anim.translate_up_current)
							.setInterpolator(new LinearInterpolator())
							.setFillAfter(true).startAnimation(layoutbottom);
			}
			// 测试
			doShowMenu(isUp);
		}
	};

	/**
	 * 方法说明：设置底部菜单是否显示
	 * 
	 * @author 江节胜 ,E-mail:dev@jiangjiesheng.com
	 * @param isShow
	 */
	protected void setIsShowBottomMenu(boolean isShow) {
		isShowBottomMenu = isShow;
		if (layoutbottom != null)
			layoutbottom.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
	}

	/**
	 * 方法说明：设置图片加载模式
	 * 
	 * @author 江节胜 ,E-mail:dev@jiangjiesheng.com
	 */
	protected void setImageResource(int ImageResourceInt) {
		ImageResource.CurrentImageResource = ImageResourceInt;
	}

	// 测试
	protected void doShowMenu(boolean isUp) {
	}

	// 测试
	protected void clickShare(int position) {
	}

	// 测试
	protected void clickDelete(int position) {
	}
}

class ImageResource {
	public static int CurrentImageResource = -1;
	public static int FromWeb = 0;
	public static int FromSDCard = 1;
	public static int FromContentProvider = 2;
	public static int FromAssets = 3;
	public static int FromDrawable = 4;
}