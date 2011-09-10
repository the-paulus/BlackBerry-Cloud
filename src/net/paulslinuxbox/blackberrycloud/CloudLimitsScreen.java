package net.paulslinuxbox.blackberrycloud;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
/**
 * Limit screen class that creates a new window for a user to view their accounts limits.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class CloudLimitsScreen extends MainScreen {

	/**
	 * Constructor
	 */
	public CloudLimitsScreen() {
		dispatcher = BlackBerryCloud.getDispatcher();
		setTitle("Black Berry Cloud");
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		rateMgr = new GridFieldManager(6, 4, Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
		absoluteMgr = new GridFieldManager(4, 2, Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
		LabelField rateLabel = new LabelField("Rate Limits");
		LabelField absoluteLabel = new LabelField("Absolute Limts");
		
		add(logoField);
		add(new SeparatorField());
		add(rateLabel);
		add(rateMgr);
		rateMgr.add(new LabelField("Verb", Field.FIELD_HCENTER));
		rateMgr.add(new LabelField("Value", Field.FIELD_HCENTER));
		rateMgr.add(new LabelField("Reaming", Field.FIELD_HCENTER));
		rateMgr.add(new LabelField("Unit", Field.FIELD_HCENTER));
		add(absoluteLabel);
		add(absoluteMgr);
		
		refresh();
	}
	
	/**
	 * Gets the limits and updates the display to reflect any changes.
	 */
	private void refresh() {
		dispatcher.setCommand(HttpRequestDispatcher.GET_LIMITS);
		dispatcher.setScreen(this);
		dispatcher.setLimitLists(rateMgr, absoluteMgr);
		dispatcher.run();
	}
	
	/**
	 * Override makeMenu function. Adds additional menu items to the applications menu. 
	 */
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Refresh", 10, 20) {
			public void run() {
				refresh();
			}
		});
		menu.add(new MenuItem("About", 10, 20) {
			public void run() {
				CloudAboutScreen about = new CloudAboutScreen();
				UiApplication.getUiApplication().pushScreen(about);
			}
		});
	}
	
	/**
	 * Field manager for displaying the rate limits.
	 */
	private GridFieldManager rateMgr;
	/**
	 * Field manager for displaying absolute limits.
	 */
	private GridFieldManager absoluteMgr;
	/**
	 * Dispatcher object used to make requests.
	 */
	private HttpRequestDispatcher dispatcher;
	/**
	 * Field that displays the logo.
	 */
	private BitmapField logoField;
	/**
	 * Image containing the Rackspace logo.
	 */
	private Bitmap logoBitmap;
}
