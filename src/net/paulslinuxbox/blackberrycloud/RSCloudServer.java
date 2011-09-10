/**
 * 
 */
package net.paulslinuxbox.blackberrycloud;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Cloud Server object. Each RSCloudServer object is a separate cloud server under the current user's account.
 * 
 * @author Paul Lyon <pmlyon@gmail.com>
 */
public class RSCloudServer {

	/**
	 * Creates a new RSCloudServer object based on a series of parameters.
	 * @param server_id The ID of the server.
	 * @param server_name Name of the server.
	 * @param image_id Image that the server uses.
	 * @param flavor_id Flavor of the server.
	 * @param status Status of the server. This could be complete, building, resizing, etc.
	 * @param progress Progress of the resize or build.
	 * @param host_id ID of the host.
	 * @param metadate Any metadata associated with the server.
	 * @param public_ips List of public IPs.
	 * @param private_ips List of private IPs.
	 */
   public RSCloudServer(Integer server_id, String server_name, Integer image_id, Integer flavor_id, String status, Integer progress, String host_id, Hashtable metadate, String[] public_ips, String[] private_ips) {

   }

   /**
    * Creates a black RSCloudServer.
    */
   public RSCloudServer() {

   }

   /**
    * 
    */
   protected void update() {

   }
   
   /**
    * Gets the ID of the server.
    * 
    * @return the serverid ID of the server.
    */
   public String getServerid() {
       return serverid;
   }

   /**
    * Sets the ID of the server.
    * 
    * @param serverid the server ID to set
    */
   public void setServerid(String serverid) {
       this.serverid = serverid;
   }

   /**
    * Gets the name of the server.
    * 
    * @return the servername Name of the server.
    */
   public String getServername() {
       return servername;
   }

   /**
    * Sets the name of the server.
    * 
    * @param servername the servername to set
    */
   public void setServername(String servername) {
       this.servername = servername;
   }

   /**
    * Gets the image ID.
    * 
    * @return the imageid
    */
   public String getImageid() {
       return imageid;
   }

   /**
    * Sets the image ID.
    * 
    * @param imageid the imageid to set
    */
   public void setImageid(String imageid) {
       this.imageid = imageid;
   }

   /**
    * Gets the flavor ID.
    * 
    * @return the flavorid
    */
   public String getFlavorid() {
       return flavorid;
   }

   /**
    * Sets the flavor ID.
    * 
    * @param flavorid the flavorid to set
    */
   public void setFlavorid(String flavorid) {
       this.flavorid = flavorid;
   }

   /**
    * Gets the status of the server.
    * 
    * @return the status
    */
   public String getStatus() {
       return status;
   }

   /**
    * Sets the status of the server.
    * 
    * @param status the status to set
    */
   public void setStatus(String status) {
       this.status = status;
   }

   /**
    * Gets the progress of the build or resize.
    * 
    * @return the progress
    */
   public String getProgress() {
       return progress;
   }

   /**
    * Sets the progress of the build or resize.
    * 
    * @param progress the progress to set
    */
   public void setProgress(String progress) {
       this.progress = progress;
   }

   /**
    * Gets the Host ID.
    * 
    * @return the hostid
    */
   public String getHostid() {
       return hostid;
   }

   /**
    * Sets the Host ID.
    * 
    * @param hostid the hostid to set
    */
   public void setHostid(String hostid) {
       this.hostid = hostid;
   }

   /**
    * Gets the metadata associated with the server.
    * 
    * @return the metadata
    */
   public Hashtable getMetadata() {
       return metadata;
   }

   /**
    * Sets the metadata of the server.
    * 
    * @param metadata the metadata to set
    */
   public void setMetadata(Hashtable metadata) {
       this.metadata = metadata;
   }

   /**
    * Gets a list of Public IPs associated with the server.
    * 
    * @return the publicips
    */
   public Vector getPublicips() {
       return publicips;
   }

   /**
    * Sets a list of Public IPs for the server.
    * 
    * @param publicips the publicips to set
    */
   public void setPublicips(Vector publicips) {
       this.publicips = publicips;
   }

   /**
    * Gets a list of Private IPs associated with the server.
    * 
    * @return the privateips
    */
   public Vector getPrivateips() {
       return privateips;
   }

   /**
    * Sets a list of Private IPs for the server.
    * 
    * @param privateips the privateips to set
    */
   public void setPrivateips(Vector privateips) {
       this.privateips = privateips;
   }

   /**
    * Server ID
    */
   private String serverid;
   /**
    * The name of the server.
    */
   private String servername;
   /**
    * Image used to build the server.
    */
   private String imageid;
   /**
    * Flavor of the server.
    */
   private String flavorid;
   /**
    * Status of the server.
    */
   private String status;
   /**
    * progess of the server.
    */
   private String progress;
   /**
    * Host ID of the server.
    */
   private String hostid;
   /**
    * Metadata assocated with the server.
    */
   private Hashtable metadata;
   /**
    * Public IPs of the server.
    */
   private Vector publicips;
   /**
    * Private IPs of the server.
    */
   private Vector privateips;
}
