/*
 * File: FacePamphletConstants.java
 * --------------------------------
 * This file declares several constants that are shared by the
 * different modules in the FacePamphlet application.  Any class
 * that implements this interface can use these constants.
 */

import java.util.*;

public interface FacePamphletConstants {

	/** The width of the application window */
	int APPLICATION_WIDTH = 1200;

	/** The height of the application window */
	int APPLICATION_HEIGHT = 750;

	/** Number of characters for each of the text input fields */
	int TEXT_FIELD_SIZE = 15;

	/** Text to be used to create an "empty" label to put space
	 *  between interactors on EAST border of application.  Note this
	 *  label is not actually the empty string, but rather a single space */
	String EMPTY_LABEL_TEXT = " ";

	/** Name of font used to display the application message at the
	 *  bottom of the display canvas */
	String MESSAGE_FONT = "Dialog-18";

	/** Name of font used to display the name in a user's profile */
	String PROFILE_NAME_FONT = "Dialog-24";
	
	/** Name of font used to display the text "No Image" in user
	 *  profiles that do not contain an actual image */
	String PROFILE_IMAGE_FONT = "Dialog-24";
	
	/** Name of font used to display the status in a user's profile */
	String PROFILE_STATUS_FONT = "Dialog-16-bold";

	/** Name of font used to display the label "Friends" above the
	 *  user's list of friends in a profile */
	String PROFILE_FRIEND_LABEL_FONT = "Dialog-16-bold";

	/** Name of font used to display the names from the user's list
	 *  of friends in a profile */
	String PROFILE_FRIEND_FONT = "Dialog-16";

	/** The width (in pixels) that profile images should be displayed */
	double IMAGE_WIDTH = 200;

	/** The height (in pixels) that profile images should be displayed */
	double IMAGE_HEIGHT = 200;

	/** The number of pixels in the vertical margin between the bottom 
	 *  of the canvas display area and the baseline for the message 
	 *  text that appears near the bottom of the display */
	double BOTTOM_MESSAGE_MARGIN = 20;

	/** The number of pixels in the hortizontal margin between the 
	 *  left side of the canvas display area and the Name, Image, and 
	 *  Status components that are display in the profile */
	double LEFT_MARGIN = 20;

	/** The number of pixels in the vertical margin between the top 
	 *  of the canvas display area and the top (NOT the baseline) of 
	 *  the Name component that is displayed in the profile */
	double TOP_MARGIN = 20;
	
	/** The number of pixels in the vertical margin between the 
	 *  baseline of the Name component and the top of the Image 
	 *  displayed in the profile */
	double IMAGE_MARGIN = 20;

	/** The number of vertical pixels in the vertical margin between 
	 *  the bottom of the Image and the top of the Status component 
	 *  in the profile */
	double STATUS_MARGIN = 20;

	String COMMAND_LOOKUP = "Lookup";
	String COMMAND_DELETE_PROFILE = "Delete profile";
	String COMMAND_CHANGE_STATUS = "Change status";
	String COMMAND_CHANGE_PICTURE = "Change picture";
	String COMMAND_SEND_FRIEND_REQUEST = "Send friend request";
	String COMMAND_CANCEL_FRIEND_REQUEST = "Cancel friend request";
	String COMMAND_REMOVE_FRIEND = "Remove friend";
	String COMMAND_EXPORT_DATABASE = "Export Database";

	String MESSAGE_BAD_REQUEST = "Input must not be empty";

	String MESSAGE_CHANGE_STATUS_SUCCESS = "Status updated to <STATUS>";
	String MESSAGE_CHANGE_STATUS_NO_ACTIVE_PROFILE = "Please select a profile to change status";

	String MESSAGE_CHANGE_PICTURE_SUCCESS = "Picture updated";
	String MESSAGE_CHANGE_PICTURE_ERROR = "Unable to open image file: <FILENAME>";
	String MESSAGE_CHANGE_PICTURE_NO_ACTIVE_PROFILE = "Please select a profile to change picture";

	String MESSAGE_ADD_FRIEND_SUCCESS = "<FRIEND_NAME> added as a friend";
	String MESSAGE_ADD_FRIEND_NO_ACTIVE_PROFILE = "Please select a profile to add a friend";
	String MESSAGE_ADD_FRIEND_ALREADY_FRIEND = "<NAME> already has <FRIEND_NAME> as a friend";
	String MESSAGE_ADD_FRIEND_SELF_FRIENDSHIP = "A profile can not be a friend of itself";

	String MESSAGE_PROFILE_NOT_FOUND = "A profile with name <NAME> does not exist";
	String MESSAGE_IMPORT = "Do you want to import a database?";
	String MESSAGE_USER_NOT_FOUND = "User with this name does not exist. Enter credentials again";
	String MESSAGE_INCORRECT_PASSWORD = "Password is incorrect. Enter credentials again";
	String MESSAGE_ENTER_NAME = "Enter name";
	String MESSAGE_ENTER_PASSWORD = "Enter password";
	String MESSAGE_LOGIN_OR_REGISTER = "Would you like to log in or register?";
	String MESSAGE_NAME_ALREADY_EXISTS = "Profile with this name already exists";
	String MESSAGE_WEAK_PASSWORD =
					"Password is weak. " +
					"It should be contain at least 8 symbols, one uppercase, one number and one special character";

	enum Status{
		Success, NotFound, BadRequest, AlreadyExists, WeakPassword, IncorrectPassword
	}
}

