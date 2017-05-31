package bibi.com.babayonlie.utils;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * 
 * API 11֮ǰ�� android.text.ClipboardManager
 * 
 * API 11֮�� android.content.ClipboardManager
 * 
 * @author LiYiMing
 * 
 */
public class ClipboardUtils {
	/**
	 * ʵ���ı����ƹ���
	 * 
	 * @param content
	 */
	public static void copy(String content, Context context) {
		// �õ������������
		ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setText(content.trim());
	}

	/**
	 * ʵ��ճ������
	 * 
	 * @param context
	 * @return
	 */
	public static String paste(Context context) {
		// �õ������������
		ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		return clipboardManager.getText().toString().trim();
	}
}
