package com.echoii.bean.cloudfilelist;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Thumbnails implements Serializable
{
	private static final long serialVersionUID = 1L;
    /** 图片组件 */
    private ImageView imageView;

    /** 图片地址 */
    private String url;

    /** 缩略图 */
    private Bitmap bitmap;

    public Thumbnails()
    {

    }

    public Thumbnails(ImageView imageView, String url, Bitmap bitmap)
    {
        this.imageView = imageView;
        this.url = url;
        this.bitmap = bitmap;
    }

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
