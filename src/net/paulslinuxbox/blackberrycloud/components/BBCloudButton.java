package net.paulslinuxbox.blackberrycloud.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;

/**
 * Specialized button used in the BlackBerry Cloud application.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 *
 */
public class BBCloudButton extends Field {

	/**
	 * Creates a new BBCloudButton by assigning it a label, particular server, image, and style.
	 * @param label Text to display on the button.
	 * @param serverID The associated server.
	 * @param image What image to display.
	 * @param style The style of the field.
	 */
	public BBCloudButton(String label, String serverID, Bitmap image, long style) {
		super(style);
		this.label = label;
		this.serverID = serverID;
		this.image = image;
	}
	
	/**
	 * Creates a new BBCloudButton and assigns it a label, to a particular server, and style.
	 * @param label Text to display on th ebutton.
	 * @param serverID The associated server.
	 * @param style The style of the field.
	 */
	public BBCloudButton(String label, String serverID, long style) {
		super(style);
		this.label = label;
		this.serverID = serverID;
	}

	/**
	 * Lays out field contents.
	 */
	protected void layout(int width, int height) {
		if(image != null) {
			setExtent(width+3, Math.max(image.getHeight(), getFont().getHeight())+3);
		} else {
			setExtent(width+3, getFont().getHeight()+20);
		}
	}

	/**
	 * Invoked by the framework to redraw a portion of this field.
	 */
	protected void paint(Graphics graphics) {
		int backgroundColor = 0;
		int foregroundColor = 0;
		
		graphics.clear();
		
		if(isFocus()) {
			backgroundColor = Color.LIGHTBLUE;
			foregroundColor = Color.BLACK;
		} else {
			backgroundColor = Color.LIGHTGRAY;
			foregroundColor = Color.BLACK;
		}
		
		if( image != null ) {
			int textY = (getHeight() - getFont().getHeight()) / 2;
			int imageY = (getHeight() - image.getHeight()) / 2;
			graphics.setColor(backgroundColor);
			graphics.fillRoundRect(3, 3, getWidth()-6, getHeight(), 12, 12);
			graphics.drawBitmap(0, imageY, image.getWidth(), image.getHeight(), image, 0, 0);
			graphics.setColor(foregroundColor);
			graphics.drawText(label, image.getWidth(), textY);
		} else {
			graphics.setColor(backgroundColor);
			graphics.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 12, 12);
			graphics.setColor(foregroundColor);
			graphics.drawText(label, (getWidth() - getFont().getBounds(label))/2, (getHeight() - getFont().getHeight())/2);
		}
	}
	
	/**
	 * Retrieves this field's preferred height. Your implementation of getPreferredHeight() should return the height of your custom field if it has any amount of space available. 
	 * 
	 * @return Preferred height for this field in pixels.
	 */
	public int getPreferredeight() {
		if( image != null) {
			return Math.max(getFont().getHeight(), image.getHeight()) + 8;
		} else {
			return getFont().getHeight();
		}
	}
	
	/**
	 * Retrieves this field's preferred width. Your implementation of getPreferredWidth() should return the width of your custom field if it has any amount of space available. 
	 * 
	 * @return Preferred width for this field in pixels.
	 */
	public int getPreferredWidth() {
		int width = getFont().getAdvance(label);
		/*if(image != null) {
			width += image.getWidth();
		}*/
		return width;
	}
	
	/**
	 * Determines if this field accepts the focus.
	 */
	public boolean isFocusable() {
		return true;
	}
	
	/**
	 * Gets the associated server ID.
	 * @return Associated server ID.
	 */
	public String getServerID() {
		return this.serverID;
	}
	
	/**
	 * Invoked when the navigational action is selected. 
	 */
	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(0);
		return true;
	}
	
	/**
	 * Handles character generation events. 
	 */
	protected boolean keyChar(char character, int status, int time) {
		if(character == Keypad.KEY_ENTER) {
			fieldChangeNotify(0);
			return true;
		}
		return super.keyChar(character, status, time);
	}

	/**
	 * Text to dipslay on the button.
	 */
	private String label;
	/**
	 * Image to display on the button.
	 */
	private Bitmap image;
	/**
	 * Server ID associated with the button.
	 */
	private String serverID;
}
