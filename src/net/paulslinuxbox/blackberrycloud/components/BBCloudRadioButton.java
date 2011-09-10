
package net.paulslinuxbox.blackberrycloud.components;

import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;

/**
 * Overrides the RadioButtonField and adds functionality specific to the application.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class BBCloudRadioButton extends RadioButtonField {

	/**
	 * Creates a new BBCloudRadioButton
	 */
	public BBCloudRadioButton() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a new BBCloudRadioButton with a label and a server ID.
	 * 
	 * @param label Label of the radio button.
	 * @param id ID of the server.
	 */
	public BBCloudRadioButton(String label, String id) {
		super(label);
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	/**
	 * Creates a new BBCloudRadioButton with a label, button group, if it's select, or server ID
	 * 
	 * @param label Text to display on the button.
	 * @param group The group the button belongs to.
	 * @param selected Indicates whether or not the button is selected or not.
	 * @param id Server ID.
	 */
	public BBCloudRadioButton(String label, RadioButtonGroup group,
			boolean selected, String id) {
		super(label, group, selected);
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	/**
	 * Creates a new BBCloudRadioButton with a label, group, whether it's selected, server ID, and style.
	 * 
	 * @param label Text to display on the button.
	 * @param group The group the button belongs to.
	 * @param selected Indicates whether or not the button is selected or not.
	 * @param id Server ID.
	 * @param style The style of the button.
	 */
	public BBCloudRadioButton(String label, RadioButtonGroup group,
			boolean selected, String id, long style) {
		super(label, group, selected, style);
		// TODO Auto-generated constructor stub
		this.id = id;
	}
	
	/**
	 * Gets the Server ID
	 * 
	 * @return The ID of the server..
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * Sets the Server ID.
	 * 
	 * @param id ID to set.
	 */
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	 * The Server ID the button is associated with.
	 */
	private String id;
}
