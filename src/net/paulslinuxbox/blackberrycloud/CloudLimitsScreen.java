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

public class CloudLimitsScreen extends MainScreen {

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
	
	private void refresh() {
		dispatcher.setCommand(HttpRequestDispatcher.GET_LIMITS);
		dispatcher.setScreen(this);
		dispatcher.setLimitLists(rateMgr, absoluteMgr);
		dispatcher.run();
	}
	
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
	
	private GridFieldManager rateMgr;
	private GridFieldManager absoluteMgr;
	private HttpRequestDispatcher dispatcher;
	private BitmapField logoField;
	private Bitmap logoBitmap;
}
