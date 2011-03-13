package net.paulslinuxbox.blackberrycloud.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;

public class BBCloudButton extends Field {

	public BBCloudButton(String label, String serverID, Bitmap image, long style) {
		super(style);
		this.label = label;
		this.serverID = serverID;
		this.image = image;
	}
	
	public BBCloudButton(String label, String serverID, long style) {
		super(style);
		this.label = label;
		this.serverID = serverID;
	}

	protected void layout(int width, int height) {
		if(image != null) {
			setExtent(width+3, Math.max(image.getHeight(), getFont().getHeight())+3);
		} else {
			setExtent(width+3, getFont().getHeight()+20);
		}
	}

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
	
	public int getPreferredeight() {
		if( image != null) {
			return Math.max(getFont().getHeight(), image.getHeight()) + 8;
		} else {
			return getFont().getHeight();
		}
	}
	
	public int getPreferredWidth() {
		int width = getFont().getAdvance(label);
		/*if(image != null) {
			width += image.getWidth();
		}*/
		return width;
	}
	
	public boolean isFocusable() {
		return true;
	}
	
	public String getServerID() {
		return this.serverID;
	}
	
	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(0);
		return true;
	}
	
	protected boolean keyChar(char character, int status, int time) {
		if(character == Keypad.KEY_ENTER) {
			fieldChangeNotify(0);
			return true;
		}
		return super.keyChar(character, status, time);
	}

	private String label;
	private Bitmap image;
	private String serverID;
}
