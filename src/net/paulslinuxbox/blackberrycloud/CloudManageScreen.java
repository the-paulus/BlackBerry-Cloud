package net.paulslinuxbox.blackberrycloud;

import java.util.Hashtable;

import net.paulslinuxbox.blackberrycloud.components.BBCloudButton;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 * Creates the screen where the user can select a server from a list.
 * 
 * @author Paulus
 * 
 */
public class CloudManageScreen extends MainScreen implements FieldChangeListener {

	/**
	 * Constructs a new Manage screen.
	 * @param old A reference to the last screen.
	 */
	public CloudManageScreen(Screen old) {
		// TODO Auto-generated constructor stub
		
		this.dispatcher = BlackBerryCloud.getDispatcher();
		
		setTitle("Black Berry Cloud");
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		HorizontalFieldManager hfMgr = new HorizontalFieldManager(
				Field.FIELD_HCENTER);
		add(logoField);
		add(new SeparatorField());
		vmgr = new VerticalFieldManager(Manager.VERTICAL_SCROLLBAR | Manager.VERTICAL_SCROLL);
		add(vmgr);
		
		loadServers();
	}
	
	/**
	 * Populates the screen with a list of servers associated with the curren account.
	 */
	private void loadServers() {
		dispatcher.setCommand(HttpRequestDispatcher.LIST_SERVERS);
		dispatcher.setScreen(this);
		dispatcher.setServerList(vmgr);
		dispatcher.run();
	}
	
	/**
	 * Invoked by a field when a property changes. 
	 */
	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
		if(field instanceof BBCloudButton) {
			field.setDirty(false);
			BBCloudButton btnServer = (BBCloudButton) field;
			UiApplication.getUiApplication().pushScreen(new CloudServerScreen(btnServer.getServerID()));
		}
	}

	/**
	 * Adds additional Menu items to the menu when the Black Berry Button is
	 * pushed.
	 */
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		/*menu.add(new MenuItem("View Server", 10, 20) {
			public void run() {
				
			}
		});*/
		menu.add(new MenuItem("Create Server", 10, 20) {
			public void run() {
				CloudCreateServerScreen newServer = new CloudCreateServerScreen();
				UiApplication.getUiApplication().pushScreen(newServer);
			}
		});
		menu.add(new MenuItem("View Limits", 10, 20) {
			public void run() {
				CloudLimitsScreen limits = new CloudLimitsScreen();
				UiApplication.getUiApplication().pushScreen(limits);
			}
		});
		menu.add(new MenuItem("About", 10, 20) {
			public void run() {
				CloudAboutScreen about = new CloudAboutScreen();
				UiApplication.getUiApplication().pushScreen(about);
			}
		});
		menu.add(new MenuItem("Refresh", 10, 20) {
			public void run() {
				loadServers();
			}
		});
	}
	
	/**
	 * Sets the Hashtable in which the list of servers will be stored.
	 * @param serverlist contains all the servers associated with the account.
	 */
	public void setServerList(Hashtable serverlist) {
		this.servers = serverlist;
	}
	
	/**
	 * Gets the user name.
	 * @return the currently logged in user name.
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Gets the API key.
	 * @return The API key.
	 */
	public String getAPIKey() {
		return this.apikey;
	}
	
	
	/**
	 * Field that displays the logo.
	 */
	private BitmapField logoField;
	/**
	 * Image containing the Rackspace logo.
	 */
	private Bitmap logoBitmap;
	/**
	 * Field manager that holds the list of servers.
	 */
	private VerticalFieldManager vmgr;
	/**
	 * Data list of servers.
	 */
	private Hashtable servers = null;
	/**
	 * Current user name.
	 */
	private String username;
	/**
	 * Current Password.
	 */
	private String apikey;
	/**
	 * Dispatcher object used to make requests.
	 */
	private HttpRequestDispatcher dispatcher;
}
