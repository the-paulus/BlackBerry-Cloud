package net.paulslinuxbox.blackberrycloud;

import java.io.ByteArrayOutputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.paulslinuxbox.blackberrycloud.components.BBCloudRadioButton;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.xml.jaxp.XMLWriter;
/**
 * Class for creating a screen which allows users to create cloud servers.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class CloudCreateServerScreen extends MainScreen implements FieldChangeListener {
	
	/**
	 * Constructor
	 */
	public CloudCreateServerScreen() {
		this.dispatcher = BlackBerryCloud.getDispatcher();
		setTitle("Black Berry Cloud");
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		serverNameField = new EditField("Server name: ", "");
		flavorGroup = new RadioButtonGroup();
		imageGroup = new RadioButtonGroup();
		flavorManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
		imageManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
		createButton = new ButtonField("Create", ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		createButton.setChangeListener(this);
		
		add(logoField);
		add(new SeparatorField());
		add(serverNameField);
		add(new LabelField("Select Server Image"));
		add(imageManager);
		add(new LabelField("Select Server Specs"));
		add(flavorManager);
		
		dispatcher.setCommand(HttpRequestDispatcher.LIST_FLAVORS);
		dispatcher.setFlavorGroup(flavorGroup, flavorManager);
		dispatcher.setScreen(this);
		dispatcher.run();
		
		dispatcher.setCommand(HttpRequestDispatcher.LIST_IMAGES);
		dispatcher.setImageGroup(imageGroup, imageManager);
		dispatcher.setScreen(this);
		dispatcher.run();
		
		add(createButton);
	}

	/**
	 * Invoked by a field when a property changes.
	 * 
	 * @param field The field that changed.
	 * @param context Information specifying the origin of the change.
	 */
	public void fieldChanged(Field field, int context) {
		
		field.setDirty(false);
		
		if(field instanceof BBCloudRadioButton){
			BBCloudRadioButton rb = (BBCloudRadioButton) field;
			if(rb.getGroup() == flavorGroup)
				flavorId = rb.getID();
			else if(rb.getGroup() == imageGroup)
				imageId = rb.getID();
		}
		
		if(field == createButton) {
			createServer();
		}
	}
	
	/**
	 * Executes the Request Dispatcher that sends out the request to Rackspace indicating 
	 * that a new cloud server should be created. 
	 */
	private void createServer() {
		if(serverNameField.getText().compareTo("") == 0) {
			Dialog.alert("Server name is required.");
		} else if(this.imageGroup.getSelectedIndex() == -1) {
			Dialog.alert("Server Image is required.");
		}else {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				XMLWriter writer = new XMLWriter(baos);
				AttributesImpl attr = new AttributesImpl();
				attr.addAttribute("", "xmlns", "xmlns", "", "http://docs.rackspacecloud.com/servers/api/v1.0");
				attr.addAttribute("", "name", "name", "TEXT", serverNameField.getText());
				attr.addAttribute("", "imageId", "imageId", "TEXT", imageId);
				attr.addAttribute("", "flavorId", "flavorId", "TEXT", flavorId);
				writer.startDocument();
				writer.startElement("http://docs.rackspacecloud.com/servers/api/v1.0", "server", "server", (Attributes)attr);
		        writer.endElement("http://docs.rackspacecloud.com/servers/api/v1.0", "server", "server");
		        writer.endDocument();
		        dispatcher.setCommand(HttpRequestDispatcher.CREATE_SERVER, baos.toString());
		        dispatcher.setScreen(this);
		        dispatcher.run();
			} catch(SAXException ex) {
				
			}
		}
	}
	
	/**
	 * Adds additional Menu items to the menu when the Black Berry Button is
	 * pushed.
	 */
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Create", 10, 20) {
			public void run() {
				createServer();
			}
		});
		menu.add(new MenuItem("Cancel", 10, 20) {
			public void run() {
				// TODO: add cancel/close code here.
			}
		});
	}
	
	/**
	 * Invoked when the screen should prompt to save its contents. 
	 */
	protected boolean onSavePrompt() {
		return true;
	}
	
	/**
	 * Invoked when the screen should save its contents.
	 */
	protected boolean onSave() {
		return false;
	}
	
	/**
	 * Flavor ID of the cloud server. This is the size of the server (eg: 256MB, 512MB, etc)
	 */
	private String flavorId;
	/**
	 * Image ID of the cloud server. This is the distribution of Linux or version of Windows.
	 */
	private String imageId;
	/**
	 * Field manager that contains all the flavor options.
	 */
	private VerticalFieldManager flavorManager;
	/**
	 * Field manager that contains all the image options.
	 */
	private VerticalFieldManager imageManager;
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
	/**
	 * Field that allows a user to enter the name of the server.
	 */
	private EditField serverNameField;
	/**
	 * Button group that lists server size choices.
	 */
	private RadioButtonGroup flavorGroup;
	/**
	 * Button group that displays image choices.
	 */
	private RadioButtonGroup imageGroup;
	/**
	 * The button field used to signal to the dispatcher that a server needs to be created.
	 */
	private ButtonField createButton;
}
