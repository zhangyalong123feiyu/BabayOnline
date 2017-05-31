package bibi.com.babayonlie.bean;

import java.io.Serializable;
import java.util.List;

public class WonderfulPicture implements Serializable {

	private String url;
	private int width;
	private int height;

	@Override
	public String toString() {
		return "WonderfulPicture [url=" + url + ", width=" + width + ", height=" + height + "]";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
