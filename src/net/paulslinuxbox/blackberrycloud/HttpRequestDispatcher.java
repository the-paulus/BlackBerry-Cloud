package net.paulslinuxbox.blackberrycloud;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.*; 
import org.xml.sax.SAXException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpsConnection;

import net.paulslinuxbox.blackberrycloud.components.BBCloudButton;
import net.paulslinuxbox.blackberrycloud.components.BBCloudRadioButton;
import net.paulslinuxbox.blackberrycloud.components.ServerImageChoice;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.system.WLANInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;
import net.rim.device.api.xml.parsers.ParserConfigurationException;

/**
 * Dispatcher class that is used by the application to manage cloud servers at Rackspace.
 * 
 * @author Paulus
 *
 */
public class HttpRequestDispatcher implements Runnable {

	/**
	 * Private constructor. 
	 */
	private HttpRequestDispatcher() { }
	
	/**
	 * Returns a single instance of the HttpRequestDispatcher.
	 * 
	 * @return {@link HttpRequestDispatcher} Single instance of the HttpRequestDispatcher
	 */
	static public HttpRequestDispatcher getInstance() {
		if(requestInstance == null) {
			synchronized(HttpRequestDispatcher.class) {
				if(requestInstance == null) {
					requestInstance = new HttpRequestDispatcher();
				}
			}
		}
		return requestInstance;
	}
	
	/**
	 * Sets the user name for authentication.
	 * 
	 * @param username Username to provide upon authentication.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Sets the api for authentication.
	 * 
	 * @param apikey API key to provide upon authentication.
	 */
	public void setAPIKey(String apikey) {
		this.apikey = apikey;
	}

	/**
	 * Gets the active internet connection.
	 * @return
	 */
	private ServiceRecord getWAP2ServiceRecord() {
		ServiceBook sb = ServiceBook.getSB();
		ServiceRecord[] records = sb.getRecords();
		
		for(int i=0; i <records.length; i++) {
			String cid = records[i].getCid().toLowerCase();
			String uid = records[i].getUid().toLowerCase();
			
			if(cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") == -1 && uid.indexOf("mms") == -1) {
				return records[i];
			}
		}
		return null;
	}
	
	/**
	 * Code that is to be run in another thread.
	 */
	public void run() {
		String connectionParameters = "";
		HttpsConnection conn;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		ByteArrayOutputStream baos;
		InputStream responseData;
		byte[] buffer;
		int bytesRead;
		
		if(!this.authenticated) {
			authenticate(this.username, this.apikey);
		}
		
		// Checks to see what connection the phone is using (e.g. WiFi or Data Network)
		if(WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED) {
			connectionParameters = ";interface=wifi";
		} else {
			int coverageStatus = CoverageInfo.getCoverageStatus();
			ServiceRecord record = getWAP2ServiceRecord();
			if(record != null && (coverageStatus & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT) {
				connectionParameters = ";deviceside=true;ConnectionUID=" + record.getUid();
			} else if((coverageStatus & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS) {
				connectionParameters = ";deviceside=false";
			} else if((coverageStatus & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT) {
				connectionParameters = ";deviceside=true";
			}
		}
		
		switch(command) {
		case GET_LIMITS:
			try {
				db = dbf.newDocumentBuilder(); 
				conn = (HttpsConnection) Connector.open(this.management_url + "/limits.xml");
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-Auth-Token", this.token);
                if (conn.getResponseCode() == 200 || conn.getResponseCode() == 203) {              	
                    parseLimits(db.parse(conn.openInputStream()));
                }
                conn.close();
			} catch(IOException ioex) {
				
			}catch(ParserConfigurationException pex) {
				
			} catch(SAXException saex) {
				
			}
			break;		
			case LIST_SERVERS:
				try {
					db = dbf.newDocumentBuilder();
	                conn = (HttpsConnection) Connector.open(this.management_url + "/servers/detail.xml");
	                conn.setRequestMethod("GET");
	                conn.setRequestProperty("X-Auth-Token", this.token);
	                if (conn.getResponseCode() == 200 || conn.getResponseCode() == 203) {              	
	                    parseServers(db.parse(conn.openInputStream()));
	                } else {
	                	parseFault(db.parse(conn.openInputStream()));
	                }
	                conn.close();
				} catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				}
				break;
			case LIST_FLAVORS:
				try {
					db = dbf.newDocumentBuilder();
					conn = (HttpsConnection) Connector.open(this.management_url + "/flavors/detail.xml");
					conn.setRequestMethod("GET");
					conn.setRequestProperty("X-Auth-Token", this.token);
					if(conn.getResponseCode() == 200 || conn.getResponseCode() == 203) {
						praseFlavors(db.parse(conn.openInputStream()));
					}
					conn.close();
				}  catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				}
				break;
			case LIST_IMAGES:
				try {
					db = dbf.newDocumentBuilder();
					conn = (HttpsConnection) Connector.open(this.management_url + "/images.xml");
					conn.setRequestMethod("GET");
					conn.setRequestProperty("X-Auth-Token", this.token);
					if(conn.getResponseCode() == 200 || conn.getResponseCode() == 203) {
						parseImages(db.parse(conn.openInputStream()));
					}
					conn.close();
				}  catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				}
				break;
			case SERVER_DETAILS:
				try {
					db = dbf.newDocumentBuilder();
	                conn = (HttpsConnection) Connector.open(this.management_url + "/servers/" + this.serverID + ".xml");
	                conn.setRequestMethod("GET");
	                conn.setRequestProperty("X-Auth-Token", this.token);
	                if (conn.getResponseCode() == 200 || conn.getResponseCode() == 203) {              	
	                    parseServerInfo(db.parse(conn.openInputStream()));
	                }
	                conn.close();
				} catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				}
				break;
			case CREATE_SERVER:
				try {
					db = dbf.newDocumentBuilder();
	                conn = (HttpsConnection) Connector.open(this.management_url + "/servers.xml" + connectionParameters, Connector.READ_WRITE);
	                conn.setRequestMethod("POST");
	                conn.setRequestProperty("Content-Type","text/xml");
	                conn.setRequestProperty("X-Auth-Token", this.token);
	                conn.setRequestProperty("Content-Length", Integer.toString(this.post_xml.length()));
	                OutputStreamWriter os = new OutputStreamWriter(conn.openOutputStream());
                	os.write(this.post_xml);
	                os.close();
	                if (conn.getResponseCode() == 202) {
	                    parseCreateServer(db.parse(conn.openInputStream()));
	                } else {
	                	parseFault(db.parse(conn.openInputStream()));
	                }
	                conn.close();
				} catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					Dialog.alert(saex.getMessage());
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				} finally {
					
				}
				break;
			case DELETE_SERVER:
				try {
					db = dbf.newDocumentBuilder();
	                conn = (HttpsConnection) Connector.open(this.management_url + "/servers/" + this.serverID + ".xml");
	                conn.setRequestMethod("DELETE");
	                conn.setRequestProperty("X-Auth-Token", this.token);
	                if (conn.getResponseCode() == 202) {    
	                    Dialog.inform("Server Deleted");
	                } else {
	                	parseFault(db.parse(conn.openInputStream()));
	                }
	                conn.close();
				} catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				}
				break;
			case UPDATE_SERVER:
				try {
					db = dbf.newDocumentBuilder();
	                conn = (HttpsConnection) Connector.open(this.management_url + "/servers/" + this.serverID + ".xml", Connector.READ_WRITE);
	                conn.setRequestMethod("PUT");
	                conn.setRequestProperty("Content-Type","text/xml");
	                conn.setRequestProperty("X-Auth-Token", this.token);
	                conn.setRequestProperty("Content-Length", Integer.toString(this.post_xml.length()));
	                OutputStreamWriter os = new OutputStreamWriter(conn.openOutputStream());
                	os.write(this.post_xml);
	                os.flush();
	                os.close();
	                if (conn.getResponseCode() == 204) {
	                	Dialog.inform("Server updated successfully");
	                	UiApplication.getUiApplication().popScreen(this.screen);
	                } else {
	                	parseFault(db.parse(conn.openInputStream()));
	                }
	                conn.close();
				} catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				}
				break;
			case SCHEDULE_BACKUP:
				try {
					db = dbf.newDocumentBuilder();
	                conn = (HttpsConnection) Connector.open(this.management_url + "/servers/" + this.serverID + "/backup_schedule", Connector.READ_WRITE);
	                conn.setRequestMethod("PUT");
	                conn.setRequestProperty("Content-Type","text/xml");
	                conn.setRequestProperty("X-Auth-Token", this.token);
	                conn.setRequestProperty("Content-Length", Integer.toString(this.post_xml.length()));
	                OutputStreamWriter os = new OutputStreamWriter(conn.openOutputStream());
                	os.write(this.post_xml);
	                os.flush();
	                os.close();
	                if (conn.getResponseCode() == 204) {
	                	Dialog.inform("Successfully Scheduled.");
	                	UiApplication.getUiApplication().popScreen(this.screen);
	                } else {
	                	parseFault(db.parse(conn.openInputStream()));
	                }
	                conn.close();
				} catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				}
				break;
			case CREATE_IMAGE:
				break;
			case REBOOT:
			case REBUILD:
			case CONFIRM_RESIZE:
			case REVERT_RESIZE:
				try {
					db = dbf.newDocumentBuilder();
					String url = this.management_url + "/servers/" + this.serverID + "/action.json";
	                conn = (HttpsConnection) Connector.open(url, Connector.READ_WRITE);
	                conn.setRequestMethod("POST");
	                conn.setRequestProperty("Accept", "application/xml");
	                conn.setRequestProperty("Content-Type", "application/xml");
	                conn.setRequestProperty("X-Auth-Token", this.token);
	                conn.setRequestProperty("Content-Length", Integer.toString(this.post_xml.length()));
	                OutputStreamWriter os = new OutputStreamWriter(conn.openOutputStream());
					os.write(this.post_xml);
					os.flush();
					os.close();
	                if (conn.getResponseCode() == 202) {    
	                    // There is nothing to parse as this API call does not have a response.
	                	UiApplication.getUiApplication().popScreen(this.screen);
	                } else {
	                	parseFault(db.parse(conn.openInputStream()));
	                }
	                conn.close();
				} catch(IOException ioex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, ioex.getMessage().getBytes());
				} catch(ParserConfigurationException pex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, pex.getMessage().getBytes());
				} catch(SAXException saex) {
					EventLogger.logEvent(BlackBerryCloud.GUID, saex.getMessage().getBytes());
				}
				break;
			case AUTHENTICATE:
				if(screen instanceof CloudLoginScreen) {
					authenticate(username, apikey);
				}
				break;
		}
	}
	
	/**
	 * Provides a username and API key to the Rackspace Cloud API for verification.
	 * 
	 * @param username String Username to provide.
	 * @param apikey String API key to provide.
	 */
	private void authenticate(String username, String apikey) {
		HttpsConnection conn;
		try {
			conn = (HttpsConnection) Connector.open(API_URL);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("X-Auth-User", username);
			conn.setRequestProperty("X-Auth-Key", apikey);
			conn.setRequestProperty("Content-Type", "application/xml");
			
			if(screen instanceof CloudLoginScreen) {
				CloudLoginScreen s = (CloudLoginScreen) screen;
				if (conn.getResponseCode() != 204) {
					s.requestFailed("Invalid Username and/or Password.");
				} else {
					s.requestSucceeded();
				}
			} else {
				if(conn.getResponseCode() != 204) {
					Dialog.alert("Error authenticating.");
				}
			}
			
			this.management_url = conn.getHeaderField("X-Server-Management-Url");
			this.storage_url = conn.getHeaderField("X-Storage-Url");
			this.cdn_url = conn.getHeaderField("X-CDN-Management-Url");
			this.token = conn.getHeaderField("X-Auth-Token");
			
			conn.close();
			this.authenticated = true;
		} catch (IOException ex) {

		}
	}
	
	/**
	 * Checks to see if we are authenticated or not.
	 * 
	 * @return Indicates if the request dispatcher is authenticated.
	 */
	public boolean isAuthenticated() {
		return this.authenticated;
	}
	
	/**
	 * Parses the XML response received from Rackspace.
	 * 
	 * @param xml_dom {@link Document} XML response to be parsed.
	 */
	private void parseCreateServer(Document xml_dom) {
		NamedNodeMap nmap = xml_dom.getElementsByTagName("server").item(0).getAttributes();
        Dialog.alert("Building " + nmap.getNamedItem("name").getNodeValue() + "\nPassword: " + nmap.getNamedItem("adminPass").getNodeValue());
        UiApplication.getUiApplication().popScreen(this.screen);
	}
	
	/**
	 * Parses the response provided when updating the server's name and/or password.
	 * 
	 * @param xml_dom {@link Document} that contains the XML from the server.
	 */
	private void parseUpdateServer(Document xml_dom) {
		NamedNodeMap nmap = xml_dom.getElementsByTagName("server").item(0).getAttributes();
        Dialog.alert("Server has been updated: Name: " + nmap.getNamedItem("name").getTextContent() + "\nPassword: " + nmap.getNamedItem("adminPass").getTextContent()); 
	}
	
	/**
	 * Displays the error received from RS in a dialog.
	 * 
	 * @param xml_dom Document object containing the response message.
	 */
	private void parseFault(Document xml_dom) {
		Dialog.alert(xml_dom.getElementsByTagName("message").item(0).getChildNodes().item(0).getNodeValue());
	}
	
	/**
	 * Parses the flavors that Rackspace returns.
	 * 
	 * @param xml_dom Document containing the list of flavors in XML format.
	 */
	private void praseFlavors(Document xml_dom) {
		NodeList flavors = xml_dom.getElementsByTagName("flavor");
		Node node;
		NamedNodeMap nmap;
		String label;
		
		for(int i=0; i<flavors.getLength(); i++) {
			node = flavors.item(i);
			nmap = node.getAttributes();
			label = nmap.getNamedItem("name").getNodeValue() + " (" + nmap.getNamedItem("disk").getNodeValue() + "GB)";
			BBCloudRadioButton flavor = new BBCloudRadioButton(label, nmap.getNamedItem("id").getNodeValue());
			flavor.setChangeListener((FieldChangeListener) this.screen);
			flavorGroup.add(flavor);
			flavorMgr.add(flavor);
		}
	}
	
	/**
	 * Prases the Images that Rackspace returns.
	 * 
	 * @param xml_dom Document containing the list of images in XML format.
	 */
	private void parseImages(Document xml_dom) {
		NodeList images = xml_dom.getElementsByTagName("image");
		Node node;
		NamedNodeMap nmap;
		
		for(int i=0; i<images.getLength(); i++) {
			node = images.item(i);
			nmap = node.getAttributes();
			
			BBCloudRadioButton image = new BBCloudRadioButton(nmap.getNamedItem("name").getNodeValue(), nmap.getNamedItem("id").getNodeValue());
			image.setChangeListener((FieldChangeListener)this.screen);
			imageGroup.add(image);
			imageMgr.add(image);
		}
	}
	
	/**
	 * Parses the XML request received when getting server information.
	 * 
	 * @param xml_dom Document containing XML data for a particular server.
	 */
	private void parseServerInfo(Document xml_dom) {
		Element el = xml_dom.getDocumentElement();
        NodeList nodeChildList = null;
        Element tmp_el;

            NamedNodeMap nmap = el.getAttributes();
            if(list != null && list instanceof VerticalFieldManager) {
            	
            	list.add(new LabelField("Image ID: " + nmap.getNamedItem("imageId").getNodeValue()));
            	list.add(new LabelField("Flavor ID: " + nmap.getNamedItem("flavorId").getNodeValue()));
            	list.add(new LabelField("Server Name: " + nmap.getNamedItem("name").getNodeValue()));
            	list.add(new LabelField("Status: " + nmap.getNamedItem("status").getNodeValue() + "(" + nmap.getNamedItem("progress").getNodeValue() + "%)"));
            	list.add(new SeparatorField());
            	list.add(new LabelField("Public IP Addresses", Field.FIELD_HCENTER));
            	
            	nodeChildList = el.getElementsByTagName("public");
                tmp_el = (Element) nodeChildList.item(0);
                nodeChildList = tmp_el.getElementsByTagName("ip");
            
                for (int j = 0; j < nodeChildList.getLength(); j++) {
                    NamedNodeMap j_nmap = nodeChildList.item(j).getAttributes();
                    for (int k = 0; k < j_nmap.getLength(); k++) {
                        list.add(new LabelField(j_nmap.item(k).getNodeValue()));
                    }
                }
                
                list.add(new SeparatorField());
            	list.add(new LabelField("Private IP Addresses", Field.FIELD_HCENTER));
            	
            	nodeChildList = el.getElementsByTagName("private");
                tmp_el = (Element) nodeChildList.item(0);
                nodeChildList = tmp_el.getElementsByTagName("ip");

                for (int j = 0; j < nodeChildList.getLength(); j++) {
                    NamedNodeMap j_nmap = nodeChildList.item(j).getAttributes();
                    for (int k = 0; k < j_nmap.getLength(); k++) {
                        list.add(new LabelField(j_nmap.item(k).getNodeValue()));
                    }
                }
            }
	}
	
	/**
	 * Parses the XML response which contains a list of servers under the account.
	 * 
	 * @param xml_dom Document containing an XML server list.
	 */
	private void parseServers(Document xml_dom) {
        Element rootEl = xml_dom.getDocumentElement();
        NodeList servers = rootEl.getElementsByTagName("server");        
        Bitmap img;

        // Iterate through all the server tags
        for (int i = 0; i < servers.getLength(); i++) {
            Node node = servers.item(i);
            NamedNodeMap nmap = node.getAttributes();

            if( list instanceof VerticalFieldManager ) {
            	String imgID = nmap.getNamedItem("imageId").getNodeValue();
            	if(imgID == "71" || imgID == "53") img = Bitmap.getBitmapResource("fedora-logo.png");
            	else if(imgID == "4") img = Bitmap.getBitmapResource("debian-logo.png");
            	else if(imgID == "19") img = Bitmap.getBitmapResource("gentoo-logo.png");
            	else if(imgID == "14362" || imgID == "10" || imgID == "69" || imgID == "49") img = Bitmap.getBitmapResource("ubuntu-logo.png");
            	else if(imgID == "62" || imgID == "14") img = Bitmap.getBitmapResource("redhat-logo.png");
            	else if(imgID == "40" || imgID == "41") img = Bitmap.getBitmapResource("oracle-logo.gif");
            	else if(imgID == "51" || imgID == "187811") img = Bitmap.getBitmapResource("centos-logo.gif");
            	else if(imgID == "55") img = Bitmap.getBitmapResource("arch-logo.gif");
            	else img = null;
            	
            	BBCloudButton tmp_btn = new BBCloudButton(nmap.getNamedItem("name").getNodeValue(), nmap.getNamedItem("id").getNodeValue(), img, ButtonField.CONSUME_CLICK );
            	tmp_btn.setChangeListener((FieldChangeListener) screen);
            	list.add(tmp_btn);
            }
        }
    }
	
	/**
	 * Parses the limits XML response.
	 * 
	 * @param xml_dom Document containing account limits in XML format.
	 */
	private void parseLimits(Document xml_dom) {
		Element rootEl = xml_dom.getDocumentElement();
        NodeList rate = rootEl.getElementsByTagName("rate");     
        NodeList absolute = rootEl.getElementsByTagName("absolute");
        NodeList rate_limits;
        NodeList absolute_limits;
        Node node;
        NamedNodeMap nmap;
        Element el;
        
        el = (Element) rate.item(0);
        rate_limits = el.getElementsByTagName("limit");
        
        rateMgr.add(new LabelField("Verb"));
    	rateMgr.add(new LabelField("Value"));
    	rateMgr.add(new LabelField("Remaining"));
    	rateMgr.add(new LabelField("Unit"));
        
        for(int i=0; i < rate_limits.getLength(); i++) {
        	node = rate_limits.item(i);
        	nmap = node.getAttributes();
        	try {
	        	rateMgr.add(new LabelField(nmap.getNamedItem("verb").getNodeValue()));
	        	rateMgr.add(new LabelField(nmap.getNamedItem("value").getNodeValue()));
	        	rateMgr.add(new LabelField(nmap.getNamedItem("remaining").getNodeValue()));
	        	rateMgr.add(new LabelField(nmap.getNamedItem("unit").getNodeValue()));
        	} catch(IllegalStateException iex) {
            	Dialog.alert("Over the Limit!");
            }
        }
        
        el = (Element) absolute.item(0);
        absolute_limits = el.getElementsByTagName("limit");
        
        for(int i=0; i < absolute_limits.getLength(); i++) {
        	node = absolute_limits.item(i);
        	nmap = node.getAttributes();
        	try {
	        	absoluteMgr.add(new LabelField(nmap.getNamedItem("name").getNodeValue()), Manager.FIELD_LEFT);
	        	absoluteMgr.add(new LabelField(nmap.getNamedItem("value").getNodeValue()), Manager.FIELD_LEFT);
        	} catch(IllegalStateException iex) {
        		Dialog.alert("Over the Limit!");
        	} catch(NullPointerException n) {
        		if(absoluteMgr == null) Dialog.alert("Manager is null");
        		if(nmap.getNamedItem("maxTotalRAMSize").getNodeValue() == null) Dialog.alert("a");
        	}
        }
        
	}
	
	/**
	 * Sets the list fields for the limits.
	 * 
	 * @param rate Field that contains the rate limits.
	 * @param absolute Field that contains the absolute limits.
	 */
	public void setLimitLists(GridFieldManager rate, GridFieldManager absolute) {
		try {
			rateMgr.deleteAll();
			absoluteMgr.deleteAll();
		} catch(NullPointerException nex) {
			
		} finally {
			this.rateMgr = rate;
			this.absoluteMgr = absolute;
		}
	}
	
	/**
	 * Sets the command to execute when the thread starts.
	 * 
	 * @param cmd Command to execute.
	 * @param post_xml XML data to send along with the command.
	 */
	public void setCommand(int cmd, String post_xml) {
		this.setCommand(cmd);
		this.post_xml = post_xml;
	}
	
	/**
	 * Sets the command to execute when the thread starts.
	 * 
	 * @param cmd Command to execute.
	 */
	public void setCommand(int cmd) {
		this.command = cmd;
	}
	
	/**
	 * Sets the screen to display results on.
	 * 
	 * @param screen The screen to display the response on.
	 */
	public void setScreen(MainScreen screen) {
		this.screen = screen;
	}
	
	/**
	 * Sets the field manager for where the list of servers associated with the account will be displayed.
	 * 
	 * @param fmgr FieldManager that will have the servers listed on.
	 */
	public void setServerList(VerticalFieldManager fmgr) {
		fmgr.deleteAll();
		this.list = fmgr;
	}
	
	/**
	 * Sets the server ID to run operations against.
	 * 
	 * @param serverID ID of the server to run commands against.
	 */
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	
	/**
	 * Sets image group and field manager when getting image listings.
	 * 
	 * @param imageGroup Button Group that contains a list of available images.
	 * @param imageMgr The FieldManager that contains the ButtonGroup.
	 */
	public void setImageGroup(RadioButtonGroup imageGroup, VerticalFieldManager imageMgr) {
		this.imageGroup = imageGroup;
		this.imageMgr = imageMgr;
	}
	
	/**
	 * Sets the flavor group and field manager when getting flavor listings.
	 * 
	 * @param flavorGroup Button Group that contains a list of available flavors.
	 * @param flavorMgr The FieldManager that contains the ButtonGroup
	 */
	public void setFlavorGroup(RadioButtonGroup flavorGroup, VerticalFieldManager flavorMgr) {
		this.flavorGroup = flavorGroup;
		this.flavorMgr = flavorMgr;
	}
	
	/**
	 * String containing the XML to post to Rackspace.
	 */
	private String post_xml;
	/**
	 * FieldManager that contains the ButtonGroup choices for flavors.
	 */
	private VerticalFieldManager flavorMgr;
	/**
	 * FieldManager that contains the ButtonGroup choices for images.
	 */
	private VerticalFieldManager imageMgr;
	/**
	 * ButtonGroup that contains flavor choices.
	 */
	private RadioButtonGroup flavorGroup;
	/**
	 * ButtonGroup that contains image choices.
	 */
	private RadioButtonGroup imageGroup;
	/**
	 * FieldManager that holds the rate limit information.
	 */
	private GridFieldManager rateMgr;
	/**
	 * FieldManager that contains the absolute limit information.
	 */
	private GridFieldManager absoluteMgr;
	/**
	 * Server ID to perform actions against.
	 */
	private String serverID;
	/**
	 * 
	 */
	private VerticalFieldManager list;
	/**
	 * Request instance.
	 */
	static private HttpRequestDispatcher requestInstance = new HttpRequestDispatcher();
	/**
	 * The account user name.
	 */
	private String username = "";
    /**
     * The account API key.
     */
	private String apikey = "";
    /**
     * API URL to run commands against.
     */
	private final String API_URL = "https://auth.api.rackspacecloud.com/v1.0";
    /**
     * The management URL to run server requests against. This is provided by Rackspace after being successfully authenticated.
     */
	private String management_url;
    /**
     * The storage URL to perform cloud file request against. This is provided by Rackspace after being successfully authenticated.
     */
	private String storage_url;
    /**
     * Content Delivery URL. This is provided by Rackspace after being successfully authenticated.
     */
	private String cdn_url;
    /**
     * Token given to the sessoin after successful authentication.
     */
	private String token;
    /**
     * Determines if authentication is needed or not.
     */
	private boolean authenticated = false;
    /**
     * Contains the XML response.
     */
	private Document doc;
	/**
	 * Screen Object to perform updates on.
	 */
	private MainScreen screen;
	/**
	 * Command that is to be executed.
	 */
	private int command;
	/**
	 * Authentication command.
	 */
	public static final int AUTHENTICATE = -1;
	/**
	 * List servers command.
	 */
	public static final int LIST_SERVERS = 1;
	/**
	 * List servers with details command. 
	 */
	public static final int SERVER_DETAILS = 2;
	/**
	 * Get limits command.
	 */
	public static final int GET_LIMITS = 3;
	/**
	 * List flavors command.
	 */
	public static final int LIST_FLAVORS = 4;
	/**
	 * List images command.
	 */
	public static final int LIST_IMAGES = 5;
	/**
	 * Create server command.
	 */
	public static final int CREATE_SERVER = 6;
	/**
	 * Delete server command.
	 */
	public static final int DELETE_SERVER = 7;
	/**
	 * Update server command.
	 */
	public static final int UPDATE_SERVER = 8;
	/**
	 * Reboot command.
	 */
	public static final int REBOOT = 9;
	/**
	 * Rebuild command.
	 */
	public static final int REBUILD = 10;
	/**
	 * Confirm resize command.
	 */
	public static final int CONFIRM_RESIZE = 11;
	/**
	 * Revert resize command.
	 */
	public static final int REVERT_RESIZE = 12;
	/**
	 * Resize server command.
	 */
	public static final int RESIZE_SERVER = 13;
	/**
	 * Schedule backup command.
	 */
	public static final int SCHEDULE_BACKUP = 14;
	/**
	 * Create image command.
	 */
	public static final int CREATE_IMAGE = 15;
}
