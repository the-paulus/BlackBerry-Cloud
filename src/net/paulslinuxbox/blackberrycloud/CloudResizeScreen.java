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
 * Creates a resize screen for the user.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class CloudResizeScreen extends MainScreen implements
		FieldChangeListener {

	/**
	 * Constructor. Creates a new resize screen.
	 * 
	 * @param serverId Server ID as a String.
	 */
	public CloudResizeScreen(String serverId) {
		this.dispatcher = BlackBerryCloud.getDispatcher();
		this.serverId = serverId;
		setTitle("Black Berry Cloud");
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		infoLabel = new LabelField("Resizing a server to anything larger than 4GB will cause data loss.");
		flavorGroup = new RadioButtonGroup();
		flavorManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
		resizeButton = new ButtonField("Resize", ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		resizeButton.setChangeListener(this);
		
		add(logoField);
		add(new SeparatorField());
		add(infoLabel);
		add(new LabelField("Select Server Image"));
		add(flavorManager);
		
		add(resizeButton);
		
		dispatcher.setCommand(HttpRequestDispatcher.LIST_FLAVORS);
		dispatcher.setFlavorGroup(flavorGroup, flavorManager);
		dispatcher.setScreen(this);
		dispatcher.run();
	}

	/**
	 * @param style
	 */
	public CloudResizeScreen(long style) {
		super(style);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Invoked by a field when a property changes. 
	 */
	public void fieldChanged(Field field, int context) {
		field.setDirty(false);
		
		if(field instanceof BBCloudRadioButton)	{
			BBCloudRadioButton rb = (BBCloudRadioButton) field;
			if(rb.getGroup() == flavorGroup)
				flavorId = rb.getID();
		}
		
		if(field == resizeButton) {
			if(this.flavorGroup.getSelectedIndex() == -1)
				Dialog.alert("You must select a size.");
			else 
				resizeServer();
		}
	}
	
	/**
	 * Starts the dispatcher and sends a request to Rackspace.
	 */
	private void resizeServer() {
		try {		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLWriter writer = new XMLWriter(baos);
			AttributesImpl attr = new AttributesImpl();
			attr.addAttribute("", "xmlns", "xmlns", "", "http://docs.rackspacecloud.com/servers/api/v1.0");
			attr.addAttribute("", "flavorId", "flavorId", "TEXT", flavorId);
			writer.startDocument();
			writer.startElement("http://docs.rackspacecloud.com/servers/api/v1.0", "resize", "resize", (Attributes)attr);
	        writer.endElement("http://docs.rackspacecloud.com/servers/api/v1.0", "resize", "resize");
	        writer.endDocument();
	        dispatcher.setCommand(HttpRequestDispatcher.REBUILD, baos.toString());
	        dispatcher.setServerID(serverId);
	        dispatcher.run();
		} catch(SAXException ex) {
			
		}
	}
	
	/**
	 * Flavor (server size) that the user has selected.
	 */
	private String flavorId;
	/**
	 * The server ID.
	 */
	private String serverId;
	/**
	 * Displays the radio button group that shows the flavor choices to the user.
	 */
	private VerticalFieldManager flavorManager;
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
	 * Label explaining to the user what to do.
	 */
	private LabelField infoLabel;
	/**
	 * Button group that displays the choice of server sizes or flavors.
	 */
	private RadioButtonGroup flavorGroup;
	/**
	 * Button that starts the resize operation.
	 */
	private ButtonField resizeButton;
}
