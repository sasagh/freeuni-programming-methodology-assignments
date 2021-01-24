/*
 * File: FacePamphletProfile.java
 * ------------------------------
 * This class keeps track of all the information for one profile
 * in the FacePamphlet social network.  Each profile contains a
 * name, an image (which may not always be set), a status (what 
 * the person is currently doing, which may not always be set),
 * and a list of friends.
 */

import java.io.Serializable;
import java.util.*;

public class FacePamphletProfile implements FacePamphletConstants, Serializable {
	private final String name;
	private String password;
	private String status;
	private String imagePath;
	private final Hashtable<String, FacePamphletProfile> friendList;
	private final ArrayList<FacePamphletProfile> pendingRequests;
	private final ArrayList<FacePamphletProfile> sentRequests;
	private boolean isFriendListPublic;

	public FacePamphletProfile(String name, String password){
		this.name = name;
		this.password = password;

		friendList = new Hashtable<>();
		pendingRequests = new ArrayList<>();
		sentRequests = new ArrayList<>();
		isFriendListPublic = true;
	}

	public String getName() {
		return name;
	}

	public String getPasswordHash(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getStatus(){
		return status == null ? "" : status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getImagePath() {
		return imagePath == null ? "" : imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Iterator<FacePamphletProfile> getFriendList(){
		return friendList.values().iterator();
	}

	public void addFriend(FacePamphletProfile profile) {
		friendList.put(profile.name, profile);
	}

	public void removeFriend(String name) {
		friendList.remove(name);
	}

	public Iterator<FacePamphletProfile> getPendingRequests(){
		return pendingRequests.iterator();
	}

	public int getPendingRequestsCount(){
		return pendingRequests.size();
	}

	public void addPendingRequest(FacePamphletProfile profile){
		pendingRequests.add(profile);
	}

	public void removePendingRequest(FacePamphletProfile profile){
		pendingRequests.remove(profile);
	}

	public boolean isFriend(FacePamphletProfile profile){
		return friendList.get(profile.getName()) != null;
	}

	public boolean isPending(FacePamphletProfile profile){
		return pendingRequests.contains(profile);
	}

	public boolean getIsFriendListPublic(){
		return isFriendListPublic;
	}

	public void setIsFriendListPublic(boolean isFriendListPublic){
		this.isFriendListPublic = isFriendListPublic;
	}

	public Iterator<FacePamphletProfile> getSentRequests(){
		return sentRequests.iterator();
	}

	public int getSentRequestsCount(){
		return sentRequests.size();
	}

	public void addSentRequest(FacePamphletProfile profile){
		sentRequests.add(profile);
	}

	public void removeSentRequest(FacePamphletProfile profile) {
		sentRequests.remove(profile);
	}
}
