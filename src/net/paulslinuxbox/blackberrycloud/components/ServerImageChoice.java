/**
 * 
 */
package net.paulslinuxbox.blackberrycloud.components;

import net.rim.device.api.i18n.ResourceBundleFamily;
import net.rim.device.api.ui.component.LabelField;

/**
 * @author Paulus
 *
 */
public class ServerImageChoice extends LabelField {

	/**
	 * 
	 */
	public ServerImageChoice() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public ServerImageChoice(Object text, String id) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param style
	 */
	public ServerImageChoice(Object text, String id, long style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	/**
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
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	private String id;

}
