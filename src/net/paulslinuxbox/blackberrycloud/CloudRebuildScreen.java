/**
 * 
 */
package net.paulslinuxbox.blackberrycloud;

import java.io.ByteArrayOutputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.paulslinuxbox.blackberrycloud.components.BBCloudRadioButton;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.xml.jaxp.XMLWriter;

/**
 * Creates the rebuild screen for the user.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class CloudRebuildScreen extends MainScreen implements FieldChangeListener {

	/**
	 * Constructs a new rebuild screen.
	 * 
	 * @param serverId String containing the server ID that is assigned from Rackspace.
	 */
	public CloudRebuildScreen(String serverId) {
		// TODO Auto-generated constructor stub
		this.dispatcher = BlackBerryCloud.getDispatcher();
		this.serverId = serverId;
		setTitle("Black Berry Cloud");
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		infoLabel = new LabelField("Rebuilding from image will wipe your data.");
		imageGroup = new RadioButtonGroup();
		imageManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
		rebuildButton = new ButtonField("Rebuild", ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		rebuildButton.setChangeListener(this);
		
		add(logoField);
		add(new SeparatorField());
		add(infoLabel);
		add(new LabelField("Select Server Image"));
		add(imageManager);
		
		add(rebuildButton);
		
		dispatcher.setCommand(HttpRequestDispatcher.LIST_IMAGES);
		dispatcher.setImageGroup(imageGroup, imageManager);
		dispatcher.setScreen(this);
		dispatcher.run();
	}

	/**
	 * Invoked by a field when a property changes. 
	 */
	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
		field.setDirty(false);
	
		if(field instanceof BBCloudRadioButton){
			BBCloudRadioButton rb = (BBCloudRadioButton) field;
			if(rb.getGroup() == imageGroup)
				imageId = rb.getID();
		}
		
		if(field == rebuildButton) {
			if(this.imageGroup.getSelectedIndex() == -1) 
				Dialog.alert("Server Image is required.");
			else
				rebuildServer();		
		}
	}

	/**
	 * Starts the dispatcher and sends the rebuild request to Rackspace.
	 */
	private void rebuildServer() {
		try {		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLWriter writer = new XMLWriter(baos);
			AttributesImpl attr = new AttributesImpl();
			attr.addAttribute("", "xmlns", "xmlns", "", "http://docs.rackspacecloud.com/servers/api/v1.0");
			attr.addAttribute("", "imageId", "imageId", "TEXT", imageId);
			writer.startDocument();
			writer.startElement("http://docs.rackspacecloud.com/servers/api/v1.0", "rebuild", "rebuild", (Attributes)attr);
	        writer.endElement("http://docs.rackspacecloud.com/servers/api/v1.0", "rebuild", "rebuild");
	        writer.endDocument();
	        dispatcher.setCommand(HttpRequestDispatcher.REBUILD, baos.toString());
	        dispatcher.setServerID(serverId);
	        dispatcher.run();
		} catch(SAXException ex) {
			
		}
	}
	
	/**
	 * Image ID of the server.
	 */
	private String imageId;
	/**
	 * ID of the server that is assigned by Rackspace.
	 */
	private String serverId;
	/**
	 * Field manager that lists the choices a user has to rebuild from.
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
	 * Label that displays basic information.
	 */
	private LabelField infoLabel;
	/**
	 * Button group that displays the image choices.
	 */
	private RadioButtonGroup imageGroup;
	/**
	 * Button that starts the rebuild process.
	 */
	private ButtonField rebuildButton;

}
