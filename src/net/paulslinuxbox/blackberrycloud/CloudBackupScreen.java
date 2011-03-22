/**
 * 
 */
package net.paulslinuxbox.blackberrycloud;

import java.io.ByteArrayOutputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.xml.jaxp.XMLWriter;

/**
 * @author Paulus
 * 
 */
public class CloudBackupScreen extends MainScreen implements
		FieldChangeListener {

	/**
	 * 
	 */
	public CloudBackupScreen(String serverID) {
		BackupChoice[] daily_choices = new BackupChoice[13];
		daily_choices[0] = new BackupChoice("Disabled", "DISABLED");
		daily_choices[1] = new BackupChoice("0000-0200", "H_0000_0200");
		daily_choices[2] = new BackupChoice("0200-0400", "H_0200_0400");
		daily_choices[3] = new BackupChoice("0400-0600", "H_0400_0600");
		daily_choices[4] = new BackupChoice("0600-0800", "H_0600_0800");
		daily_choices[5] = new BackupChoice("0800-1000", "H_0800_1000");
		daily_choices[6] = new BackupChoice("1000-1200", "H_1000_1200");
		daily_choices[7] = new BackupChoice("1200-1400", "H_1200_1400");
		daily_choices[8] = new BackupChoice("1400-1600", "H_1400_1600");
		daily_choices[9] = new BackupChoice("1600-1800", "H_1600_1800");
		daily_choices[10] = new BackupChoice("1800-2000", "H_1800_2000");
		daily_choices[11] = new BackupChoice("2000-2200", "H_2000_2200");
		daily_choices[12] = new BackupChoice("2200-0000", "H_2200_0000");

		BackupChoice[] weekly_choices = new BackupChoice[8];
		weekly_choices[0] = new BackupChoice("Disabled", "DISABLED");
		weekly_choices[1] = new BackupChoice("Monday", "MONDAY");
		weekly_choices[2] = new BackupChoice("Tuesday", "TUESDAY");
		weekly_choices[3] = new BackupChoice("Wednesday", "WEDNESDAY");
		weekly_choices[4] = new BackupChoice("Thursday", "THURSDAY");
		weekly_choices[5] = new BackupChoice("Friday", "FRIDAY");
		weekly_choices[6] = new BackupChoice("Saturday", "SATURDAY");
		weekly_choices[7] = new BackupChoice("Sunday", "SUNDAY");

		this.dispatcher = BlackBerryCloud.getDispatcher();
		this.serverID = serverID;

		setTitle("Black Berry Cloud");
		logoBitmap = Bitmap.getBitmapResource("racklogo.PNG");
		logoField = new BitmapField(logoBitmap, Field.FIELD_HCENTER);
		add(logoField);
		add(new SeparatorField());

		vmgr = new VerticalFieldManager(Manager.VERTICAL_SCROLL
				| Manager.VERTICAL_SCROLLBAR);
		daily = new ObjectChoiceField("Daily: ", daily_choices);
		weekly = new ObjectChoiceField("Weekly: ", weekly_choices);
		savebtn = new ButtonField("Save", ButtonField.CONSUME_CLICK
				| ButtonField.NEVER_DIRTY);
		savebtn.setChangeListener(this);
		vmgr.add(new LabelField("Backups Schedule:"));
		vmgr.add(daily);
		vmgr.add(weekly);
		vmgr.add(savebtn);
		add(vmgr);
	}

	/**
	 * @param style
	 */
	public CloudBackupScreen(long style) {
		super(style);
		// TODO Auto-generated constructor stub
	}

	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
		if (field.isDirty())
			field.setDirty(false);
		else if (field == savebtn) {

			
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					XMLWriter writer = new XMLWriter(baos);
					AttributesImpl attr = new AttributesImpl();
					attr.addAttribute("", "xmlns", "xmlns", "", "http://docs.rackspacecloud.com/servers/api/v1.0");
					if ((daily.getChoice(daily.getSelectedIndex()).toString().compareTo("Disabled") != 0) || (weekly.getChoice(weekly.getSelectedIndex()).toString().compareTo("Disabled") != 0)) 
						attr.addAttribute("", "enabled", "enabled", "TEXT", "true");
					if ((daily.getChoice(daily.getSelectedIndex()).toString().compareTo("Disabled") != 0)) {
						BackupChoice choice = (BackupChoice) daily.getChoice(daily.getSelectedIndex());
						attr.addAttribute("", "daily", "daily", "TEXT", choice.getValue());
					}
					if(weekly.getChoice(weekly.getSelectedIndex()).toString().compareTo("Disabled") != 0) {
						BackupChoice choice = (BackupChoice) weekly.getChoice(weekly.getSelectedIndex());
						attr.addAttribute("", "weekly", "weekly", "TEXT", choice.getValue());
					}
					writer.startDocument();
					writer.startElement("http://docs.rackspacecloud.com/servers/api/v1.0","backupSchedule", "backupSchedule", (Attributes) attr);
					writer.endElement("http://docs.rackspacecloud.com/servers/api/v1.0","backupSchedule", "backupSchedule");
					writer.endDocument();
					dispatcher.setCommand(HttpRequestDispatcher.SCHEDULE_BACKUP, baos
							.toString());
					dispatcher.setServerID(serverID);
					dispatcher.setScreen(this);
					dispatcher.run();
				} catch (SAXException ex) {

				}
			}
	}

	private class BackupChoice {
		public BackupChoice(String label, String value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public String getValue() {
			return value;
		}

		public String toString() {
			return label;
		}

		private String value;
		private String label;
	}

	private ButtonField savebtn;
	private HttpRequestDispatcher dispatcher;
	private VerticalFieldManager vmgr;
	private ObjectChoiceField daily;
	private ObjectChoiceField weekly;
	private HorizontalFieldManager dhmgr;
	private String serverID;
	private Bitmap logoBitmap;
	private BitmapField logoField;
}
