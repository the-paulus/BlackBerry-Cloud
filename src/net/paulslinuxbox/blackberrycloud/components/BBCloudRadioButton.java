/**
 * 
 */
package net.paulslinuxbox.blackberrycloud.components;

import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;

/**
 * @author Paulus
 *
 */
public class BBCloudRadioButton extends RadioButtonField {

	/**
	 * 
	 */
	public BBCloudRadioButton() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param label
	 */
	public BBCloudRadioButton(String label, String id) {
		super(label);
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	/**
	 * @param label
	 * @param group
	 * @param selected
	 */
	public BBCloudRadioButton(String label, RadioButtonGroup group,
			boolean selected, String id) {
		super(label, group, selected);
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	/**
	 * @param label
	 * @param group
	 * @param selected
	 * @param style
	 */
	public BBCloudRadioButton(String label, RadioButtonGroup group,
			boolean selected, String id, long style) {
		super(label, group, selected, style);
		// TODO Auto-generated constructor stub
		this.id = id;
	}
	
	public String getID() {
		return this.id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	private String id;
}
