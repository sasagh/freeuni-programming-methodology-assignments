/*
 * File: FacePamphletConstants.java
 * --------------------------------
 * This file declares several constants that are shared by the
 * different modules in the FacePamphlet application.  Any class
 * that implements this interface can use these constants.
 */

public interface FacePamphletConstants {

	/** The width of the application window */
	int APPLICATION_WIDTH = 1200;

	/** The height of the application window */
	int APPLICATION_HEIGHT = 600;

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

	String STRING_WITH_WHITESPACES = "     ";

	String COMMAND_MY_PROFILE = "My profile";
	String COMMAND_LOOKUP = "Lookup";
	String COMMAND_DELETE_PROFILE = "Delete profile";
	String COMMAND_CHANGE_STATUS = "Change status";
	String COMMAND_CHANGE_PICTURE = "Change picture";
	String COMMAND_GUESTS = "Gostebi (<COUNT>)";
	String COMMAND_REQUESTS_PENDING = "Pending requests (<COUNT>)";
	String COMMAND_REQUESTS_SENT = "Sent requests (<COUNT>)";
	String COMMAND_REQUEST_SEND = "Send friend request";
	String COMMAND_REQUEST_CANCEL = "Cancel friend request";
	String COMMAND_REQUEST_ACCEPT = "Accept";
	String COMMAND_REQUEST_DECLINE = "Decline";
	String COMMAND_CHANGE_PASSWORD = "Change password";
	String COMMAND_REMOVE_FRIEND = "Remove friend";
	String COMMAND_EXPORT_DATABASE = "Export Database";
	String COMMAND_FRIEND_LIST_PRIVACY = "Make friend list <PRIVACY>";
	String COMMAND_REFRESH = "Refresh";

	String DIALOG_MESSAGE_BAD_REQUEST = "Input must not be empty";
	String DIALOG_MESSAGE_ADD_FRIEND_OR_NOT =
			"Add <PROFILENAME> as a friend? Type 1 to accept request, 2 to decline. Type 0 to cancel";
	String DIALOG_MESSAGE_ENTER_CORRECT_NUMBER = "Enter correct number";
	String DIALOG_MESSAGE_ERROR_COULD_NOT_IMPORT_DATABASE = "Sorry, something went wrong. Could not import database";
	String DIALOG_MESSAGE_DELETE_CONFIRMATION = "Do you want to delete your profile?";
	String DIALOG_MESSAGE_REQUEST_SENT_CANCEL = "Do you want to cancel friend request to <PROFILENAME>?";
	String DIALOG_MESSAGE_IMPORT = "Do you want to import a database?";
	String DIALOG_MESSAGE_USER_NOT_FOUND = "User with this name does not exist. Enter credentials again";
	String DIALOG_MESSAGE_INCORRECT_PASSWORD = "Password is incorrect. Enter credentials again";
	String DIALOG_MESSAGE_ENTER_NAME = "Enter name. Enter 0 to cancel";
	String DIALOG_MESSAGE_ENTER_PASSWORD = "Enter password. Enter 0 to cancel.";
	String DIALOG_MESSAGE_LOGIN_OR_REGISTER = "Would you like to log in or register?";
	String DIALOG_MESSAGE_NAME_ALREADY_EXISTS = "Profile with this name already exists";
	String DIALOG_MESSAGE_EXPORT_SUCCESS = "Exported successfully";
	String DIALOG_MESSAGE_EXPORT_ERROR = "Something went wrong. Could not export database";
	String DIALOG_MESSAGE_WEAK_PASSWORD =
					"Password is weak. " +
					"It should be contain at least 8 symbols, one uppercase, one number and one special character";

	String MESSAGE_CHANGE_STATUS_SUCCESS = "Status updated to <STATUS>";
	String MESSAGE_CHANGE_PICTURE_SUCCESS = "Picture updated";
	String MESSAGE_CHANGE_PICTURE_ERROR = "Unable to open image file: <FILENAME>";
	String MESSAGE_REQUEST_SENT = "Friend request sent";
	String MESSAGE_REQUEST_CANCEL = "Friend request cancenled";
	String MESSAGE_REQUEST_ACCEPT = "Friend Request accepted";
	String MESSAGE_REQUEST_DECLINE = "Friend Request declined";
	String MESSAGE_FRIEND_REMOVED = "Friend removed";
	String MESSAGE_PROFILE_NOT_FOUND = "A profile with name <NAME> does not exist";
	String MESSAGE_PROFILE_DELETED = "Profile deleted";
	String MESSAGE_CHANGED_FRIEND_LIST_PRIVACY = "Friend list privacy changed";
	String MESSAGE_PASSWORD_CHANGED = "Password changed";


	enum Status{
		Success, NotFound, AlreadyExists, WeakPassword, IncorrectPassword
	}
}

