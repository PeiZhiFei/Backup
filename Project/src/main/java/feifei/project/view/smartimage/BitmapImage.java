package feifei.project.view.smartimage;

import android.content.Context;
import android.graphics.Bitmap;

public class BitmapImage implements SmartImage {
	private Bitmap bitmap;

	public BitmapImage(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public Bitmap getBitmap(Context context) {
		return bitmap;
	}
}