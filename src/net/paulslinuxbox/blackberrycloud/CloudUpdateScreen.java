/**
 * 
 */
package net.paulslinuxbox.blackberrycloud;

import java.io.ByteArrayOutputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.paulslinuxbox.blackberrycloud.containers.BBCloudScreen;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.xml.jaxp.XMLWriter;

/**
 * Creates a screen to allow a user to update the server name and password.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 * 
 */
public class CloudUpdateScreen extends BBCloudScreen implements
		FieldChangeListener {

	/**
	 * Creates a new Update Screen
	 * 
	 * @param serverId
	 *            ID of the cloud server.
	 */
	public CloudUpdateScreen(String serverId) {

		serverNameField = new EditField("Server Name: ", "");
		adminPassField = new PasswordEditField("Password: ", "");
		updateBtn = new ButtonField("Update", ButtonField.CONSUME_CLICK);
		VerticalFieldManager vMgr = new VerticalFieldManager(
				Field.FIELD_HCENTER);

		updateBtn.setChangeListener(this);

		add(serverNameField);
		add(adminPassField);
		vMgr.add(updateBtn);
		add(vMgr);
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
	 * Invoked by a field when a property changes.
	 */
	public void fieldChanged(Field field, int context) {
		field.setDirty(false);
		if (field == updateBtn) {
			if (serverNameField.getText() == ""
					&& adminPassField.getText() == "") {
				Dialog.alert("Server name or password are required.");
			} else {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					XMLWriter writer = new XMLWriter(baos);
					AttributesImpl attr = new AttributesImpl();
					attr.addAttribute("", "xmlns", "xmlns", "",
							"http://docs.rackspacecloud.com/servers/api/v1.0");
					attr.addAttribute("", "name", "name", "TEXT",
							serverNameField.getText());
					attr.addAttribute("", "adminPass", "adminPass", "TEXT",
							adminPassField.getText());
					writer.startDocument();
					writer.startElement(
							"http://docs.rackspacecloud.com/servers/api/v1.0",
							"server", "server", (Attributes) attr);
					writer.endElement(
							"http://docs.rackspacecloud.com/servers/api/v1.0",
							"server", "server");
					writer.endDocument();
					dispatcher.setCommand(HttpRequestDispatcher.UPDATE_SERVER,
							baos.toString());
					dispatcher.setScreen(this);
					dispatcher.run();
				} catch (SAXException ex) {
				}
			}
		}
	}

	/**
	 * New name of the server.
	 */
	private EditField serverNameField;
	/**
	 * The new admin password of the server.
	 */
	private PasswordEditField adminPassField;
	/**
	 * Executes the update.
	 */
	private ButtonField updateBtn;

}
