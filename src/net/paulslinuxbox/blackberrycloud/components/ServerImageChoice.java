/**
 * 
 */
package net.paulslinuxbox.blackberrycloud.components;

import net.rim.device.api.i18n.ResourceBundleFamily;
import net.rim.device.api.ui.component.LabelField;

/**
 * Class that overrides the LabelField and adds functionality to the class needed by the application.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class ServerImageChoice extends LabelField {

	/**
	 * Creates a new ServerImageChoice object. 
	 */
	public ServerImageChoice() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a new ServerImageChoice object with specific text and the image id.
	 * 
	 * @param text The text of the choice.
	 * @param id Image id.
	 */
	public ServerImageChoice(Object text, String id) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a new ServerImageChoice object with specific text, image id, and style.
	 * 
	 * @param text The text of the choice.
	 * @param id The image ID.
	 * @param style The style of the choice.
	 */
	public ServerImageChoice(Object text, String id, long style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Overloads the LabelField
	 * 
	 * @param rb
	 * @param key
	 */
	public ServerImageChoice(ResourceBundleFamily rb, int key) {
		super(rb, key);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param offset
	 * @param length
	 * @param style
	 */
	public ServerImageChoice(Object text, int offset, int length, long style) {
		super(text, offset, length, style);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Sets the image ID for the choice.
	 * 
	 * @param id ID of the choice.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the image ID of the choice.
	 * 
	 * @return Image ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Image ID.
	 */
	private String id;

}
