/**
 * 
 */
package net.paulslinuxbox.blackberrycloud.containers;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 * @author Paulus
 *
 */
public class CloudMainScreen extends MainScreen {

	/**
	 * 
	 */
	public CloudMainScreen() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public CloudMainScreen(long arg0) {
		super(Manager.NO_HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR);
		// TODO Auto-generated constructor stub
		VerticalFieldManager internalManager = new VerticalFieldManager( Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR ) {
			public void paintBackground( Graphics g ) 
            {
                g.setBackgroundColor( 0x99caf3 );
                g.clear();
            }
            protected void sublayout( int maxWidth, int maxHeight ) 
            {
                super.sublayout( maxWidth, maxHeight );
                XYRect extent = getExtent();
                int width = Math.max( extent.width, Display.getWidth() );
                int height = Math.max( extent.height, Display.getHeight() );
                setExtent( width, height );
            }      
        };
        
        container = new VerticalFieldManager( Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR );
        internalManager.add(container);
        super.add(internalManager);
	}
	
	public void add(Field field) {
		container.add(field);
	}
private VerticalFieldManager container;
}
