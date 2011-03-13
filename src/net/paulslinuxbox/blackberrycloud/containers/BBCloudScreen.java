/**
 * 
 */
package net.paulslinuxbox.blackberrycloud.containers;

import net.paulslinuxbox.blackberrycloud.BlackBerryCloud;
import net.paulslinuxbox.blackberrycloud.HttpRequestDispatcher;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * @author Paulus
 *
 */
public class BBCloudScreen extends MainScreen {

	/**
	 * 
	 */
	public BBCloudScreen() {
		// TODO Auto-generated constructor stub
		dispatcher = BlackBerryCloud.getDispatcher();
		setTitle("Black Berry Cloud");
		setBackground(BackgroundFactory.createSolidBackground(Color.LIGHTBLUE));
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		
		add(logoField);
		add(new SeparatorField());
		
	}

	/**
	 * @param arg0
	 */
	public BBCloudScreen(long arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	private Bitmap logoBitmap; // 315x57
	private BitmapField logoField;
	protected HttpRequestDispatcher dispatcher;
}
