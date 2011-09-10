package net.paulslinuxbox.blackberrycloud;

import net.paulslinuxbox.blackberrycloud.containers.BBCloudScreen;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

/**
 * Creates a simple screen to tell the user about the application.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class CloudAboutScreen extends BBCloudScreen {

	/**
	 * Creates the new screen. 
	 */
	public CloudAboutScreen() {
		
		label = new LabelField("Black Berry Cloud\n\nVersion: 1.0.0\n\nWritten By Paul Lyon (pmlyon@gmail.com)");
		
		add(label);
	}
	/**
	 * Label contain the about screen text.
	 */
	private LabelField label;
}
