package net.paulslinuxbox.blackberrycloud;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpsConnection;
import javax.microedition.lcdui.Graphics;

import net.paulslinuxbox.blackberrycloud.containers.CloudMainScreen;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.TransitionContext;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngineInstance;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * 
 * @author Paulus
 * 
 */
public class CloudLoginScreen extends MainScreen implements FieldChangeListener {

	/**
	 * Constructor for the login screen.
	 */
	public CloudLoginScreen() {
		setTitle("Black Berry Cloud");
		setBackground(BackgroundFactory.createSolidBackground(Color.LIGHTBLUE));
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		HorizontalFieldManager hfMgr = new HorizontalFieldManager(
				Field.FIELD_HCENTER);
		usernameField = new EditField("User Name: ", "");
		apiField = new PasswordEditField("API Key: ", "");
		rememberField = new CheckboxField("Remember Credentials", false);
		loginButton = new ButtonField("Login", ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
		loginButton.setChangeListener(this);
		
		loadData();

		add(logoField);
		add(new SeparatorField());
		add(usernameField);
		add(apiField);
		add(rememberField);
		add(new SeparatorField());
		hfMgr.add(loginButton);
		add(hfMgr);
		persistantData = new Hashtable();
	}

	/**
	 * Places saved user name and API key into their respective fields.
	 */
	private void loadData() {
		persistantObject = PersistentStore.getPersistentObject(KEY);
		if (persistantObject.getContents() == null) {	
			persistantData = new Hashtable();
			persistantObject.setContents(persistantData);
		} else {
			persistantData = (Hashtable) persistantObject.getContents();
		}

		if( persistantData != null ) {
			if (persistantData.containsKey("username")) {
				try {
					usernameField.setText((String) persistantData.get("username"));
				} catch (IllegalArgumentException ex) {
					usernameField.setText("");
				}
			}
	
			if (persistantData.containsKey("apikey")) {
				try {
					apiField.setText((String) persistantData.get("apikey"));
				} catch (IllegalArgumentException ex) {
					apiField.setText("");
				}
			}
	
			if (persistantData.containsKey("remember")) {
				Boolean bdata = (Boolean) persistantData.get("remember");
				rememberField.setChecked(bdata.booleanValue());
			}
		}
	}

	/**
	 * Listens for actions.
	 */
	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
		if (field == loginButton) {
			login();
		}
	}

	/**
	 * Starts the dispatcher to log in.
	 */
	protected void login() {
		
		Thread loginThread = new Thread(new Runnable() { 
			public void run() {
				HttpsConnection conn;
				try {
					conn = (HttpsConnection) Connector.open("https://auth.api.rackspacecloud.com/v1.0");
					conn.setRequestMethod("GET");
					conn.setRequestProperty("X-Auth-User", getUsername());
					conn.setRequestProperty("X-Auth-Key", getAPIKey());
					conn.setRequestProperty("Content-Type", "application/xml");
					
					if (conn.getResponseCode() != 204) {
						requestFailed("Invalid Username and/or Password.");
					} else {
						BlackBerryCloud.getDispatcher().setAPIKey(getAPIKey());
						BlackBerryCloud.getDispatcher().setUsername(getUsername());
						requestSucceeded();
					}
					conn.close();
				} catch (IOException ex) {

				}
			}
		});
		loginThread.start();
	}

	protected boolean onSavePrompt() {
		return true;
	}
	
	protected boolean onSave() {
		return false;
	}

	/*public boolean onClose() {
		usernameField.setDirty(false);
		apiField.setDirty(false);
		return true;
	}*/
	
	/**
	 * Saves the data in the fields.
	 */
	public void save() throws IOException {
		if (rememberField.getChecked() == true) {
			persistantData.put("username", new String(usernameField.getText()));
			persistantData.put("apikey", new String(apiField.getText()));
			persistantData.put("remember", new Boolean(rememberField.getChecked()));
		} else {
			persistantData.put("username", new String(""));
			persistantData.put("apikey", new String(""));
			persistantData.put("remember", new Boolean(rememberField.getChecked()));
		}
		persistantObject.setContents(persistantData);
		persistantObject.commit();
	}

	/**
	 * Pushes the Manager screen onto the window stack.
	 */
	public void requestSucceeded() {
		if(this.rememberField.getChecked()) {
			try {
				save();
			} catch(IOException ioex) {
				Dialog.alert("Unable to save login information.");
			}
		}
		UiApplication.getApplication().invokeLater(new Runnable() {
			public void run() {
				CloudManageScreen cms = new CloudManageScreen(UiApplication.getUiApplication().getActiveScreen());
				UiApplication.getUiApplication().pushScreen(cms);
			}
		});
	}

	/**
	 * Displays error message.
	 * @param message the message to display.
	 */
	public void requestFailed(final String message) {
		UiApplication.getApplication().invokeLater(new Runnable() {
			public void run() {
				Dialog.alert(message);
			}
		});
	}

	/**
	 * Gets the value in the user name field.
	 * @return String Containing the user name.
	 */
	public String getUsername() {
		return this.usernameField.getText();
	}

	/**
	 * Gets the value in the api field.
	 * @return String Containing the API key.
	 */
	public String getAPIKey() {
		return this.apiField.getText();
	}

	/**
	 * Adds additional Menu items to the menu when the Black Berry Button is pushed.
	 */
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Save", 10, 20) {
			public void run() {
				try {
					save();
				} catch(IOException ioe) {
					
				}
			}
		});
		menu.add(new MenuItem("Login", 10, 20) {
			public void run() {
				login();
			}
		});
		menu.add(new MenuItem("About", 10, 20) {
			public void run() {
				CloudAboutScreen about = new CloudAboutScreen();
				UiApplication.getUiApplication().pushScreen(about);
			}
		});
	}

	private Bitmap logoBitmap; // 315x57
	private BitmapField logoField;
	private EditField usernameField;
	private PasswordEditField apiField;
	private CheckboxField rememberField;
	private ButtonField loginButton;
	private Hashtable persistantData;
	private PersistentObject persistantObject;
	private static final long KEY = 0x8c8c2e90b0a25aceL;
}
