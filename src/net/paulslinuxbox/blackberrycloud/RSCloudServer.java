/**
 * 
 */
package net.paulslinuxbox.blackberrycloud;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Paulus
 *
 */
/**
*
* @author Paulus
*/
public class RSCloudServer {

   /**
    * 
    * @param doc
    */

   public RSCloudServer(Integer server_id, String server_name, Integer image_id, Integer flavor_id, String status, Integer progress, String host_id, Hashtable metadate, String[] public_ips, String[] private_ips) {

   }

   public RSCloudServer() {

   }

   protected void update() {

   }
   
   /**
    * @return the serverid
    */
   public String getServerid() {
       return serverid;
   }

   /**
    * @param serverid the serverid to set
    */
   public void setServerid(String serverid) {
       this.serverid = serverid;
   }

   /**
    * @return the servername
    */
   public String getServername() {
       return servername;
   }

   /**
    * @param servername the servername to set
    */
   public void setServername(String servername) {
       this.servername = servername;
   }

   /**
    * @return the imageid
    */
   public String getImageid() {
       return imageid;
   }

   /**
    * @param imageid the imageid to set
    */
   public void setImageid(String imageid) {
       this.imageid = imageid;
   }

   /**
    * @return the flavorid
    */
   public String getFlavorid() {
       return flavorid;
   }

   /**
    * @param flavorid the flavorid to set
    */
   public void setFlavorid(String flavorid) {
       this.flavorid = flavorid;
   }

   /**
    * @return the status
    */
   public String getStatus() {
       return status;
   }

   /**
    * @param status the status to set
    */
   public void setStatus(String status) {
       this.status = status;
   }

   /**
    * @return the progress
    */
   public String getProgress() {
       return progress;
   }

   /**
    * @param progress the progress to set
    */
   public void setProgress(String progress) {
       this.progress = progress;
   }

   /**
    * @return the hostid
    */
   public String getHostid() {
       return hostid;
   }

   /**
    * @param hostid the hostid to set
    */
   public void setHostid(String hostid) {
       this.hostid = hostid;
   }

   /**
    * @return the metadata
    */
   public Hashtable getMetadata() {
       return metadata;
   }

   /**
    * @param metadata the metadata to set
    */
   public void setMetadata(Hashtable metadata) {
       this.metadata = metadata;
   }

   /**
    * @return the publicips
    */
   public Vector getPublicips() {
       return publicips;
   }

   /**
    * @param publicips the publicips to set
    */
   public void setPublicips(Vector publicips) {
       this.publicips = publicips;
   }

   /**
    * @return the privateips
    */
   public Vector getPrivateips() {
       return privateips;
   }

   /**
    * @param privateips the privateips to set
    */
   public void setPrivateips(Vector privateips) {
       this.privateips = privateips;
   }

   private String serverid;
   private String servername;
   private String imageid;
   private String flavorid;
   private String status;
   private String progress;
   private String hostid;
   private Hashtable metadata;
   private Vector publicips;
   private Vector privateips;
}
