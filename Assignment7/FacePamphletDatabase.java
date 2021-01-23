/*
 * File: FacePamphletDatabase.java
 * -------------------------------
 * This class keeps track of the profiles of all users in the
 * FacePamphlet application.  Note that profile names are case
 * sensitive, so that "ALICE" and "alice" are NOT the same name.
 */

import java.lang.annotation.Repeatable;
import java.util.*;

public class FacePamphletDatabase implements FacePamphletConstants {
	private Hashtable<String, FacePamphletProfile> profiles = new Hashtable<>();

	protected void setProfiles(Hashtable<String, FacePamphletProfile> profiles){
		this.profiles = profiles;
	}

	/** Add profile */
	protected void create(String name, FacePamphletProfile profile) {
		profiles.put(name, profile);
	}

	/** Delete profile */
	protected void delete(String name){
		profiles.remove(name);
	}

	/** Get profiles */
	protected Hashtable<String, FacePamphletProfile> getProfiles(){
		return profiles;
	}

	/** Get profile by name */
	protected FacePamphletProfile getProfileByName(String name){
		return profiles.get(name);
	}
}
