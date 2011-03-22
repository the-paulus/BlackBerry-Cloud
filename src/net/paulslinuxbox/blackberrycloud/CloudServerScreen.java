package net.paulslinuxbox.blackberrycloud;

import java.io.ByteArrayOutputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.xml.jaxp.XMLWriter;

/**
 * Creates a screen to display information about a specific cloud server.
 * 
 * @author Paul Lyon <pmlyon@gmail.com.
 *
 */
public class CloudServerScreen extends MainScreen implements FieldChangeListener {
	
	
	public CloudServerScreen(String serverID) {
		this.dispatcher = BlackBerryCloud.getDispatcher();
		this.serverID = serverID;
		
		setTitle("Black Berry Cloud");
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		add(logoField);
		add(new SeparatorField());
		vmgr = new VerticalFieldManager(Manager.VERTICAL_SCROLLBAR | Manager.VERTICAL_SCROLL);
		add(vmgr);
		refresh();
	}

	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
		
	}
	
	private void refresh() {
		dispatcher.setCommand(HttpRequestDispatcher.SERVER_DETAILS);
		dispatcher.setServerID(serverID);
		dispatcher.setScreen(this);
		dispatcher.setServerList(vmgr);
		dispatcher.run();
	}
	
	/**
	 * Adds additional Menu items to the menu when the Black Berry Button is
	 * pushed.
	 */
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Hard Reboot", 10, 20) {
			public void run() {
				if(Dialog.ask(Dialog.D_YES_NO, "Are you sure you want to do a hard reboot?") == Dialog.YES) {
					try {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						XMLWriter writer = new XMLWriter(baos);
						AttributesImpl attr = new AttributesImpl();
						attr.addAttribute("", "xmlns", "xmlns", "", "http://docs.rackspacecloud.com/servers/api/v1.0");
						attr.addAttribute("", "type", "type", "TEXT", "HARD");
						writer.startDocument();
						writer.startElement("http://docs.rackspacecloud.com/servers/api/v1.0", "reboot", "reboot", (Attributes)attr);
				        writer.endElement("http://docs.rackspacecloud.com/servers/api/v1.0", "reboot", "reboot");
				        writer.endDocument();
				        dispatcher.setCommand(HttpRequestDispatcher.REBOOT, baos.toString());
				        dispatcher.setServerID(serverID);
				        dispatcher.run();
					} catch(SAXException ex) {
						
					}
				}
				
			}
		});
		menu.add(new MenuItem("Soft Reboot", 10, 20) {
			public void run() {
				if(Dialog.ask(Dialog.D_YES_NO, "Are you sure you want to do a soft reboot?") == Dialog.YES) {
					try {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						XMLWriter writer = new XMLWriter(baos);
						AttributesImpl attr = new AttributesImpl();
						attr.addAttribute("", "xmlns", "xmlns", "", "http://docs.rackspacecloud.com/servers/api/v1.0");
						attr.addAttribute("", "type", "type", "TEXT", "SOFT");
						writer.startDocument();
						writer.startElement("http://docs.rackspacecloud.com/servers/api/v1.0", "reboot", "reboot", (Attributes)attr);
				        writer.endElement("http://docs.rackspacecloud.com/servers/api/v1.0", "reboot", "reboot");
				        writer.endDocument();
				        dispatcher.setCommand(HttpRequestDispatcher.REBOOT, baos.toString());
				        dispatcher.setServerID(serverID);
				        dispatcher.run();
				        Dialog.alert(baos.toString());
					} catch(SAXException ex) {
						
					}
				}
			}
		});
		menu.add(new MenuItem("Backup Schedule", 10, 20) {
			public void run() {
				CloudBackupScreen backup = new CloudBackupScreen(serverID);
				UiApplication.getUiApplication().pushScreen(backup);
			}
		});
		menu.add(new MenuItem("Update Server", 10, 20) {
			public void run() {
				CloudUpdateScreen update = new CloudUpdateScreen(serverID);
				UiApplication.getUiApplication().pushScreen(update);
			}
		});
		menu.add(new MenuItem("Rebuild Server", 10, 20) {
			public void run() {
				CloudRebuildScreen rebuild = new CloudRebuildScreen(serverID);
				UiApplication.getUiApplication().pushScreen(rebuild);
			}
		});
		menu.add(new MenuItem("Resize Server", 10, 20) {
			public void run() {
				CloudResizeScreen resize = new CloudResizeScreen(serverID);
				UiApplication.getUiApplication().pushScreen(resize);
			}
		});
		menu.add(new MenuItem("Confirm Resize", 10, 20) {
			public void run() {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					XMLWriter writer = new XMLWriter(baos);
					AttributesImpl attr = new AttributesImpl();
					attr.addAttribute("", "xmlns", "xmlns", "", "http://docs.rackspacecloud.com/servers/api/v1.0");
					writer.startDocument();
					writer.startElement("http://docs.rackspacecloud.com/servers/api/v1.0", "confirmResize", "confirmResize", (Attributes)attr);
			        writer.endElement("http://docs.rackspacecloud.com/servers/api/v1.0", "confirmResize", "confirmResize");
			        writer.endDocument();
			        dispatcher.setCommand(HttpRequestDispatcher.CONFIRM_RESIZE, baos.toString());
			        dispatcher.run();
				} catch(SAXException ex) {
					
				}
			}
		});
		menu.add(new MenuItem("Revert Server", 10, 20) {
			public void run() {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					XMLWriter writer = new XMLWriter(baos);
					AttributesImpl attr = new AttributesImpl();
					attr.addAttribute("", "xmlns", "xmlns", "", "http://docs.rackspacecloud.com/servers/api/v1.0");
					writer.startDocument();
					writer.startElement("http://docs.rackspacecloud.com/servers/api/v1.0", "revertResize", "revertResize", (Attributes)attr);
			        writer.endElement("http://docs.rackspacecloud.com/servers/api/v1.0", "revertResize", "revertResize");
			        writer.endDocument();
			        dispatcher.setCommand(HttpRequestDispatcher.CONFIRM_RESIZE, baos.toString());
			        dispatcher.run();
				} catch(SAXException ex) {
					
				}
			}
		});
		menu.add(new MenuItem("Delete Server", 10, 20) {
			public void run() {
				if(Dialog.ask(Dialog.D_DELETE) == Dialog.DELETE) {
					dispatcher.setCommand(HttpRequestDispatcher.DELETE_SERVER);
					dispatcher.setServerID(serverID);
					dispatcher.run();
				}
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
				refresh();
			}
		});
	}
	
	private HttpRequestDispatcher dispatcher;
	private String serverID;
	private VerticalFieldManager vmgr;
	private Bitmap logoBitmap;
	private BitmapField logoField;
}
