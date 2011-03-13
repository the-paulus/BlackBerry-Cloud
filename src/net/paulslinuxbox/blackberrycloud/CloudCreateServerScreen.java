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

public class CloudCreateServerScreen extends MainScreen implements FieldChangeListener {
	
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
	
	protected boolean onSavePrompt() {
		return true;
	}
	
	protected boolean onSave() {
		return false;
	}
	
	private String flavorId;
	private String imageId;
	private VerticalFieldManager flavorManager;
	private VerticalFieldManager imageManager;
	private HttpRequestDispatcher dispatcher;
	private BitmapField logoField;
	private Bitmap logoBitmap;
	private EditField serverNameField;
	private RadioButtonGroup flavorGroup;
	private RadioButtonGroup imageGroup;
	private ButtonField createButton;
}
