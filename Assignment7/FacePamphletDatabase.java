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
	private final Hashtable<String, FacePamphletProfile> profiles = new Hashtable<>();

	/** 
	 * This method adds the given profile to the database.  If the 
	 * name associated with the profile is the same as an existing 
	 * name in the database, the existing profile is replaced by 
	 * the new profile passed in.
	 */
	public Response<Status, FacePamphletProfile> addProfile(FacePamphletProfile profile) {
		Response<Status, FacePamphletProfile> response = new Response<>();

		if(profile == null || profile.getName().isBlank()) {
			response.status = Status.BadRequest;
			return response;
		}

		response.data = profile;
		String name = profile.getName();

		if(profiles.get(name) == null){
			profiles.put(name, profile);

			response.status = Status.Add;
		}else{
			profiles.compute(
					name,
					(key, value) -> value = profile
			);

			response.status = Status.Update;
		}

		return response;
	}
	
	/** 
	 * This method returns the profile associated with the given name 
	 * in the database.  If there is no profile in the database with 
	 * the given name, the method returns null.
	 */
	public Response<Status, FacePamphletProfile> getProfile(String name) {
		FacePamphletProfile profile = profiles.get(name);

		Response<Status, FacePamphletProfile> response = new Response<>();
		response.data = profile;
		response.status = profile == null ? Status.NotFound : Status.Success;

		return response;
	}
	
	
	/** 
	 * This method removes the profile associated with the given name
	 * from the database.  It also updates the list of friends of all
	 * other profiles in the database to make sure that this name is
	 * removed from the list of friends of any other profile.
	 * 
	 * If there is no profile in the database with the given name, then
	 * the database is unchanged after calling this method.
	 */
	public Response<Status, FacePamphletProfile> deleteProfile(String name) {
		Response<Status, FacePamphletProfile> response = new Response<>();
		if(name == null || name.isBlank()) {
			response.status = Status.BadRequest;
			return response;
		}

		if(profiles.get(name) == null) {
			response.status = Status.NotFound;
			return response;
		}

		profiles.remove(name);
		response.status = Status.Delete;
		return response;
	}

}
