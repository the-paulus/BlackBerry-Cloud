

package net.paulslinuxbox.blackberrycloud;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;

/**
 * Main Blackberry Application class which starts.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class BlackBerryCloud extends UiApplication {

	/**
	 * Creates a new BlackBerryCloud application.
	 */
	public BlackBerryCloud() {
		CloudLoginScreen scr = new CloudLoginScreen();
		dispatcher = HttpRequestDispatcher.getInstance();
		pushScreen(scr);
	}
	
	/**
	 * Main function of the application.
	 * @param args Program parameters.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventLogger.register(GUID, APP_NAME, EventLogger.VIEWER_EXCEPTION);
		UiApplication app = new BlackBerryCloud();
		app.enterEventDispatcher();
	}
	
	/**
	 * Retreives the Request Dispatcher for use with interfacing with Rackspace's services.
	 * @return HttpRequestDispatcher object.
	 */
	public static HttpRequestDispatcher getDispatcher() {
		return dispatcher;
	}
	
	/**
	 * GUID of the application used for persistant data.
	 */
	public static final long GUID = 0xea60b485a022cf4bL;
	/**
	 * Application name.
	 */
	public static final String APP_NAME = "BBCloud";
	/**
	 * Dispatcher object used to make requests.
	 */
	private static HttpRequestDispatcher dispatcher;
}
