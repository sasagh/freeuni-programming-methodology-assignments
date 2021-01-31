/*
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.util.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

public class FacePamphlet extends Program
					implements FacePamphletConstants {
	private JLabel nameLabel;
	private JTextField nameTextField;
	private JTextField changeStatusTextField;
	private JTextField changePictureTextField;
	private JButton lookupBtn;
	private JButton refreshBtn;
	private JButton changeStatusBtn;
	private JButton changePictureBtn;
	private JButton requestSendBtn;
	private JButton requestCancelBtn;
	private JButton requestAcceptBtn;
	private JButton requestDeclineBtn;
	private JButton requestsPendingBtn;
	private JButton requestsSentBtn;
	private JButton changeFriendListPrivacyBtn;
	private JButton deleteProfileBtn;
	private JButton removeFriendBtn;
	private JButton exportDatabaseBtn;
	private JButton changePasswordBtn;
	private JButton userProfileBtn;
	private JButton guestsBtn;

	private FacePamphletProfile userProfile;
	private FacePamphletProfile currentProfile;

	private boolean dbImported = false;
	private boolean isProfileActive = true;

	private FacePamphletCanvas canvas;

	/**
	 * This method has the responsibility for initializing the
	 * interactors in the application, and taking care of any other
	 * initialization that needs to be performed.
	 */
	public void init() {
		importDatabase();

		canvas = new FacePamphletCanvas();
		add(canvas);

		loginOrRegister();

		currentProfile = userProfile;
		canvas.displayProfile(currentProfile, getFriendsToDisplay());

		initializeInteractors();
		addActionCommands();
		addActionListenersToInteractors();
		setButtonVisibility();
		addInteractorsToProgram();
    }

    /** Method which sets visibility for south buttons */
	private void setButtonVisibility() {
		requestSendBtn.setVisible(false);
		requestCancelBtn.setVisible(false);
		removeFriendBtn.setVisible(false);
		requestAcceptBtn.setVisible(false);
		requestDeclineBtn.setVisible(false);
	}

	/** Method which asks user if he/she wants login or register */
	private void loginOrRegister(){
		if(!dbImported){
			while(true){
				if(register()) return;
			}
		}

		while(true){
			boolean login = getDialog()
					.readBoolean(DIALOG_MESSAGE_LOGIN_OR_REGISTER, "Log in", "Register");
			if(login ? login() : register()) break;
		}
	}

	/** Method for registering a new user*/
	private boolean register(){
		while(true){
			String name = readName();
			if(name.equals("0")) return false;

			String password = readPassword();
			if(password.equals("0")) return false;

			Response<Status, FacePamphletProfile> response = FacePamphletService.register(name, password);

			if(response.status == Status.AlreadyExists) getDialog().println(DIALOG_MESSAGE_NAME_ALREADY_EXISTS);
			if(response.status == Status.WeakPassword) getDialog().println(DIALOG_MESSAGE_WEAK_PASSWORD);
			else if(response.status == Status.Success){
				userProfile = response.data;
				return true;
			}
		}
	}

	/** Method for loging */
	private boolean login(){
		while(true){
			String name = readName();
			if(name.equals("0")) return false;

			String password = readPassword();
			if(password.equals("0")) return false;

			Response<Status, FacePamphletProfile> response = FacePamphletService.login(name, password);

			if(response.status == Status.NotFound) getDialog().println(DIALOG_MESSAGE_USER_NOT_FOUND);
			if(response.status == Status.IncorrectPassword) getDialog().println(DIALOG_MESSAGE_INCORRECT_PASSWORD);
			else if(response.status == Status.Success){
				userProfile = response.data;
				return true;
			}
		}
	}

	/** Import database */
	private void importDatabase() {
		dbImported = getDialog().readBoolean(DIALOG_MESSAGE_IMPORT, "Yes", "No");

		if(dbImported){
			boolean successfullyImported = FacePamphletService.importProfiles();

			if(!successfullyImported){
				getDialog().showErrorMessage(DIALOG_MESSAGE_ERROR_COULD_NOT_IMPORT_DATABASE);
				dbImported = false;
			}
		}
	}

	/** Initializes interactors */
	private void initializeInteractors() {
		nameLabel = new JLabel("Name");

		nameTextField = new JTextField(TEXT_FIELD_SIZE);
		changeStatusTextField = new JTextField(TEXT_FIELD_SIZE);
		changePictureTextField = new JTextField(TEXT_FIELD_SIZE);
		userProfileBtn = new JButton(COMMAND_MY_PROFILE);
		lookupBtn = new JButton(COMMAND_LOOKUP);
		refreshBtn = new JButton(COMMAND_REFRESH);
		changeStatusBtn = new JButton(COMMAND_CHANGE_STATUS);
		changePictureBtn = new JButton(COMMAND_CHANGE_PICTURE);
		guestsBtn = new JButton(getGuestsText());
		requestsPendingBtn = new JButton(getPendingRequestsBtnText());
		requestsSentBtn = new JButton(getSentRequestsBtnText());
		requestSendBtn = new JButton(COMMAND_REQUEST_SEND);
		requestCancelBtn = new JButton(COMMAND_REQUEST_CANCEL);
		requestAcceptBtn = new JButton(COMMAND_REQUEST_ACCEPT);
		requestDeclineBtn = new JButton(COMMAND_REQUEST_DECLINE);
		removeFriendBtn = new JButton(COMMAND_REMOVE_FRIEND);
		changePasswordBtn = new JButton(COMMAND_CHANGE_PASSWORD);
		changeFriendListPrivacyBtn = new JButton(getFriendListPrivacyBtnText());
		deleteProfileBtn = new JButton(COMMAND_DELETE_PROFILE);
		exportDatabaseBtn = new JButton(COMMAND_EXPORT_DATABASE);
	}

	/** Adds action commands to interactors */
	private void addActionCommands() {
		changeStatusTextField.setActionCommand(COMMAND_CHANGE_STATUS);
		changePictureTextField.setActionCommand(COMMAND_CHANGE_PICTURE);
		nameTextField.setActionCommand(COMMAND_LOOKUP);

		guestsBtn.setActionCommand(COMMAND_GUESTS);
		requestsPendingBtn.setActionCommand(COMMAND_REQUESTS_PENDING);
		requestsSentBtn.setActionCommand(COMMAND_REQUESTS_SENT);
		changeFriendListPrivacyBtn.setActionCommand(COMMAND_FRIEND_LIST_PRIVACY);
	}

	/** Adds action listeners */
	private void addActionListenersToInteractors() {
		changeStatusTextField.addActionListener(this);
		changePictureTextField.addActionListener(this);
		nameTextField.addActionListener(this);

		userProfileBtn.addActionListener(this);
		lookupBtn.addActionListener(this);
		refreshBtn.addActionListener(this);
		changeStatusBtn.addActionListener(this);
		changePictureBtn.addActionListener(this);
		guestsBtn.addActionListener(this);
		requestsPendingBtn.addActionListener(this);
		requestsSentBtn.addActionListener(this);
		requestSendBtn.addActionListener(this);
		requestCancelBtn.addActionListener(this);
		requestAcceptBtn.addActionListener(this);
		requestDeclineBtn.addActionListener(this);
		removeFriendBtn.addActionListener(this);
		changePasswordBtn.addActionListener(this);
		changeFriendListPrivacyBtn.addActionListener(this);
		deleteProfileBtn.addActionListener(this);
		exportDatabaseBtn.addActionListener(this);
	}

	/** Adds interactors in program */
	private void addInteractorsToProgram() {
		add(userProfileBtn, NORTH);
		add(new JLabel(STRING_WITH_WHITESPACES), NORTH);

		add(nameLabel, NORTH);
		add(nameTextField, NORTH);
		add(lookupBtn, NORTH);
		add(new JLabel(STRING_WITH_WHITESPACES), NORTH);

		add(refreshBtn, NORTH);
		add(new JLabel(STRING_WITH_WHITESPACES), NORTH);

		add(exportDatabaseBtn, NORTH);

		add(changeStatusTextField, WEST);
		add(changeStatusBtn, WEST);

		addEmptyLabel();

		add(changePictureTextField, WEST);
		add(changePictureBtn, WEST);
		addEmptyLabel();

		add(guestsBtn, WEST);
		addEmptyLabel();

		add(requestsPendingBtn, WEST);
		addEmptyLabel();

		add(requestsSentBtn, WEST);
		addEmptyLabel();

		add(changePasswordBtn, WEST);
		addEmptyLabel();

		add(changeFriendListPrivacyBtn, WEST);
		addEmptyLabel();

		add(deleteProfileBtn, WEST);

		add(requestSendBtn, SOUTH);
		add(requestCancelBtn, SOUTH);
		add(removeFriendBtn, SOUTH);
		add(requestAcceptBtn, SOUTH);
		add(requestDeclineBtn, SOUTH);
	}

	public void actionPerformed(ActionEvent event) {
		if(!isProfileActive && !event.getActionCommand().equals(COMMAND_EXPORT_DATABASE)) return;

		switch (event.getActionCommand()){
			case COMMAND_MY_PROFILE -> actionMyProfile();
			case COMMAND_LOOKUP -> actionLookup();
			case COMMAND_REFRESH -> actionRefresh();
			case COMMAND_CHANGE_STATUS -> actionChangeStatus();
			case COMMAND_CHANGE_PICTURE -> actionChangePicture();
			case COMMAND_GUESTS -> actionGuests();
			case COMMAND_REQUEST_SEND -> actionRequestSend();
			case COMMAND_REQUEST_CANCEL -> actionRequestCancel();
			case COMMAND_REQUEST_ACCEPT -> actionRequestAccept();
			case COMMAND_REQUEST_DECLINE -> actionRequestDecline();
			case COMMAND_REMOVE_FRIEND -> actionRemoveFriend();
			case COMMAND_REQUESTS_PENDING -> actionRequestsPending();
			case COMMAND_REQUESTS_SENT -> actionRequestsSent();
			case COMMAND_CHANGE_PASSWORD -> actionChangePassword();
			case COMMAND_FRIEND_LIST_PRIVACY -> actionChangeFriendListPrivacy();
			case COMMAND_EXPORT_DATABASE -> actionExportDatabase();
			case COMMAND_DELETE_PROFILE -> actionDeleteProfile();
		}

		handleSouthButtons();
	}

	/** Methods bellow are for actions */
	private void actionMyProfile(){
		currentProfile = userProfile;
		canvas.displayProfile(currentProfile, getFriendsToDisplay());
	}

	private void actionLookup(){
		String name = nameTextField.getText();

		if(!isInputValid(name)) {
			getDialog().println(DIALOG_MESSAGE_BAD_REQUEST);
			return;
		}

		Response<Status, FacePamphletProfile> response = FacePamphletService.getProfile(name);

		if(response.status == Status.NotFound){
			canvas.showMessage(MESSAGE_PROFILE_NOT_FOUND.replace("<NAME>", name));
			return;
		}

		currentProfile = response.data;
		currentProfile.addGuest(userProfile);
		canvas.displayProfile(currentProfile, getFriendsToDisplay());
		handleSouthButtons();
	}

	private void actionRefresh() {
		canvas.displayProfile(currentProfile, getFriendsToDisplay());
	}

	private void actionExportDatabase(){
		boolean success = FacePamphletService.exportProfiles();
		getDialog().println(success ? DIALOG_MESSAGE_EXPORT_SUCCESS : DIALOG_MESSAGE_EXPORT_ERROR);
	}

	private void actionChangeStatus(){
		String status = changeStatusTextField.getText();

		userProfile.setStatus(status);

		currentProfile = userProfile;
		canvas.displayProfile(userProfile, getFriendsToDisplay());
		canvas.showMessage(MESSAGE_CHANGE_STATUS_SUCCESS.replace("<STATUS>", status));
	}

	private void actionChangePicture(){
		String fileName = changePictureTextField.getText();
		String imagePath = System.getProperty("user.dir") + "\\images\\" + fileName;

		try {
			if(new File(imagePath).isFile()){
				currentProfile = userProfile;
				currentProfile.setImagePath(imagePath);
				canvas.displayProfile(currentProfile, getFriendsToDisplay());
				canvas.showMessage(MESSAGE_CHANGE_PICTURE_SUCCESS);
			}else{
				handleImageError(fileName);
			}
		} catch (ErrorException ex) {
			handleImageError(fileName);
		}
	}

	private void actionGuests(){
		currentProfile = userProfile;
		userProfile.resetNotSeenGuestsCount();
		canvas.showGuests(userProfile.getGuests());
		guestsBtn.setText(getGuestsText());
	}

	private void actionRequestsSent() {
		Iterator<FacePamphletProfile> it = userProfile.getSentRequests();

		ArrayList<FacePamphletProfile> toCancel = new ArrayList<>();

		while(it.hasNext()){
			FacePamphletProfile profile = it.next();
			boolean cancel = getDialog()
					.readBoolean(
							DIALOG_MESSAGE_REQUEST_SENT_CANCEL
							.replace("<PROFILENAME>", profile.getName()), "Yes", "No"
					);

			if(cancel) toCancel.add(profile);
		}

		for(FacePamphletProfile profile : toCancel) FacePamphletService.cancelFriendRequest(profile, userProfile);

		requestsSentBtn.setText(getSentRequestsBtnText());
	}

	private void actionDeleteProfile(){
		boolean shouldDelete = getDialog()
				.readBoolean(DIALOG_MESSAGE_DELETE_CONFIRMATION, "Yes", "No");

		if(shouldDelete){
			canvas.removeAll();
			isProfileActive = false;
			requestsPendingBtn.setText(COMMAND_REQUESTS_PENDING.replace(" (<COUNT>)", ""));
			requestsSentBtn.setText(COMMAND_REQUESTS_SENT.replace(" (<COUNT>)", ""));
			guestsBtn.setText(COMMAND_GUESTS.replace(" (<COUNT>)", ""));
			FacePamphletService.deleteProfile(userProfile);
			canvas.showMessage(MESSAGE_PROFILE_DELETED);
		}
	}

	private void actionChangeFriendListPrivacy() {
		boolean isFriendListPublic = userProfile.getIsFriendListPublic();

		if(isFriendListPublic) FacePamphletService.makeFriendListPrivate(userProfile);
		else FacePamphletService.makeFriendListPublic(userProfile);

		changeFriendListPrivacyBtn.setText(getFriendListPrivacyBtnText());
		canvas.showMessage(MESSAGE_CHANGED_FRIEND_LIST_PRIVACY);
	}

	private void actionChangePassword(){
		while(true){
			String password = readPassword();

			if(password.equals("0")) break;

			Response<Status, FacePamphletProfile> response
					= FacePamphletService.changePassword(password, userProfile);

			if(response.status == Status.Success) {
				canvas.showMessage(MESSAGE_PASSWORD_CHANGED);
				return;
			}

			getDialog().println(DIALOG_MESSAGE_WEAK_PASSWORD);
		}
	}

	private void actionRequestAccept(){
		FacePamphletService.addFriend(currentProfile, userProfile);
		requestsPendingBtn.setText(getPendingRequestsBtnText());
		canvas.showMessage(MESSAGE_REQUEST_ACCEPT);
	}

	private void actionRequestDecline(){
		FacePamphletService.cancelFriendRequest(userProfile, currentProfile);
		requestsPendingBtn.setText(getPendingRequestsBtnText());
		canvas.showMessage(MESSAGE_REQUEST_DECLINE);
	}

	private void actionRemoveFriend(){
		FacePamphletService.removeFriend(currentProfile, userProfile);
		canvas.showMessage(MESSAGE_FRIEND_REMOVED);
	}

	private void actionRequestCancel(){
		FacePamphletService.cancelFriendRequest(currentProfile, userProfile);
		requestsSentBtn.setText(getSentRequestsBtnText());
		canvas.showMessage(MESSAGE_REQUEST_CANCEL);
	}

	private void actionRequestSend(){
		FacePamphletService.sendFriendRequest(currentProfile, userProfile);
		requestsSentBtn.setText(getSentRequestsBtnText());
		canvas.showMessage(MESSAGE_REQUEST_SENT);
	}

	private void actionRequestsPending(){
		Iterator<FacePamphletProfile> it = userProfile.getPendingRequests();

		ArrayList<FacePamphletProfile> toAccept = new ArrayList<>();
		ArrayList<FacePamphletProfile> toDecline = new ArrayList<>();

		while(it.hasNext()){
			FacePamphletProfile profile = it.next();
			int accept;

			while(true){
				accept = getDialog()
						.readInt(DIALOG_MESSAGE_ADD_FRIEND_OR_NOT.replace("<PROFILENAME>", profile.getName()));

				if(accept == 0 || accept == 1 || accept == 2) break;

				getDialog().println(DIALOG_MESSAGE_ENTER_CORRECT_NUMBER);
			}

			if(accept == 0) break;

			if(accept == 1) toAccept.add(profile);
			else toDecline.add(profile);
		}

		for(FacePamphletProfile profile : toAccept) FacePamphletService.addFriend(profile, userProfile);
		for(FacePamphletProfile profile : toDecline) FacePamphletService.cancelFriendRequest(userProfile, profile);

		requestsPendingBtn.setText(getPendingRequestsBtnText());
	}

	/** Method for hiding south buttons */
	private void hideSouthButtons(){
		requestSendBtn.setVisible(false);
		requestCancelBtn.setVisible(false);
		removeFriendBtn.setVisible(false);
		requestAcceptBtn.setVisible(false);
		requestDeclineBtn.setVisible(false);
	}

	/** Method for displaying south buttons */
	private void displaySouthButtons(){
		if(currentProfile == null) return;

		if(userProfile.isPending(currentProfile)){
			requestAcceptBtn.setVisible(true);
			requestDeclineBtn.setVisible(true);
		}

		else if(currentProfile.isFriend(userProfile)){
			removeFriendBtn.setVisible(true);
		}

		else if(currentProfile.isPending(userProfile)) {
			requestCancelBtn.setVisible(true);
		}

		else if(currentProfile != userProfile) {
			requestSendBtn.setVisible(true);
		}
	}

	/** Method which handles situation when there is no image with this filename */
	private void handleImageError(String fileName){
		canvas.showMessage(MESSAGE_CHANGE_PICTURE_ERROR.replace("<FILENAME>", fileName));
	}

	/** Read name from dialog */
	private String readName(){
		return getDialog().readLine(DIALOG_MESSAGE_ENTER_NAME);
	}

	/** Read password from dialog */
	private String readPassword(){
		return getDialog().readLine(DIALOG_MESSAGE_ENTER_PASSWORD);
	}

	/** Checks if input is valid */
	private boolean isInputValid(String value){
		return !value.isBlank();
	}

	/** Returns text for pending requests button */
	private String getPendingRequestsBtnText(){
		return COMMAND_REQUESTS_PENDING
				.replace("<COUNT>", ""+userProfile.getPendingRequestsCount());
	}

	/** Returns text for sent requests button */
	private String getSentRequestsBtnText(){
		return COMMAND_REQUESTS_SENT
				.replace("<COUNT>", ""+userProfile.getSentRequestsCount());
	}

	private void handleSouthButtons(){
		hideSouthButtons();

		if(!isProfileActive) return;

		displaySouthButtons();
	}

	/** Returns text for friend list privacy button*/
	private String getFriendListPrivacyBtnText(){
		boolean isFriendListPublic = userProfile.getIsFriendListPublic();

		return COMMAND_FRIEND_LIST_PRIVACY.replace("<PRIVACY>", isFriendListPublic ? "private" : "public");
	}

	/** Returns friends which should be displayed on canvas */
	private ArrayList<String> getFriendsToDisplay(){
		ArrayList<String> friendsToDisplay = new ArrayList<>();

		if(currentProfile.getIsFriendListPublic() || currentProfile == userProfile){
			Iterator<FacePamphletProfile> iterator = currentProfile.getFriendList();

			while(iterator.hasNext()) friendsToDisplay.add(iterator.next().getName());
		}else if(!currentProfile.getIsFriendListPublic()){
			friendsToDisplay.addAll(FacePamphletService.getMutualFriends(currentProfile, userProfile));
		}

		return friendsToDisplay;
	}

	/** Adds empty label to west */
	private void addEmptyLabel(){
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
	}

	private String getGuestsText(){
		return COMMAND_GUESTS
				.replace("<COUNT>", ""+userProfile.getNotSeenGuestsCount());
	}
}
