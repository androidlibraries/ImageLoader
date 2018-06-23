package com.jiangjiesheng.imageloader.entity;

import java.io.Serializable;

public class PhotoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String url;// 注意要加上http:// 默认在数据源实例化之前添加http://
	private String thumbnailUrl;// 网络资源服务器缩略图 ,注意要加上http:// 默认在数据源实例化之前添加http://
	private String originalPath;// 本地路径,注意要加上file://
	private String providerUri;// 内容提供者,注意要加上content://
	private String assetsPath;// assets资源,注意要加上assets://
	private String drawablePath;// 内容提供者,注意要加上drawable://

	private boolean isChecked;

	public PhotoModel(String url, String thumbnailUrl) {
		super();
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
	}

	public PhotoModel(String url, String thumbnailUrl, boolean isChecked) {
		super();
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
		this.isChecked = isChecked;
	}

	public PhotoModel(String originalPath, boolean isChecked) {
		super();
		this.originalPath = originalPath;
		this.isChecked = isChecked;
	}

	public PhotoModel(String originalPath) {
		this.originalPath = originalPath;
	}

	public PhotoModel() {
	}

	public String getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getProviderUri() {
		return providerUri;
	}

	public void setProviderUri(String providerUri) {
		this.providerUri = providerUri;
	}

	public String getAssetsPath() {
		return assetsPath;
	}

	public void setAssetsPath(String assetsPath) {
		this.assetsPath = assetsPath;
	}

	public String getDrawablePath() {
		return drawablePath;
	}

	public void setDrawablePath(String drawablePath) {
		this.drawablePath = drawablePath;
	}

	@Override
	public String toString() {
		return "PhotoModel [url=" + url + ", thumbnailUrl=" + thumbnailUrl
				+ ", originalPath=" + originalPath + ", providerUri="
				+ providerUri + ", assetsPath=" + assetsPath
				+ ", drawablePath=" + drawablePath + ", isChecked=" + isChecked
				+ "]";
	}

}
