/*
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Array;
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
	private JButton changeStatusBtn;
	private JButton changePictureBtn;
	private JButton requestSendBtn;
	private JButton requestCancelBtn;
	private JButton requestAcceptBtn;
	private JButton requestDeclineBtn;
	private JButton requestsPendingBtn;
	private JButton deleteProfileBtn;
	private JButton removeFriendBtn;
	private JButton exportDatabaseBtn;
	private JButton changePasswordBtn;
	private FacePamphletProfile userProfile;
	private FacePamphletProfile currentProfile;
	private boolean dbImported = false;
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
		canvas.displayProfile(currentProfile);

		initializeInteractors();
		addActionCommandsToInteractors();
		addActionListenersToInteractors();
		setButtonVisibility();
		addInteractorsToProgram();
    }

	private void setButtonVisibility() {
		requestSendBtn.setVisible(false);
		requestCancelBtn.setVisible(false);
		removeFriendBtn.setVisible(false);
		requestAcceptBtn.setVisible(false);
		requestDeclineBtn.setVisible(false);
	}

	private void loginOrRegister(){
		if(!dbImported){
			register();
			return;
		}

		boolean login = getDialog().readBoolean(MESSAGE_LOGIN_OR_REGISTER, "Log in", "Register");

		if(login) login();
		else register();
	}

	private void register(){
		while(true){
			String name = readName();
			String password = readPassword();

			Response<Status, FacePamphletProfile> response = FacePamphletService.register(name, password);

			if(response.status == Status.AlreadyExists) getDialog().println(MESSAGE_NAME_ALREADY_EXISTS);
			if(response.status == Status.WeakPassword) getDialog().println(MESSAGE_WEAK_PASSWORD);
			else if(response.status == Status.Success){
				userProfile = response.data;
				break;
			}
		}
	}

	private void login(){
		while(true){
			String name = readName();
			String password = readPassword();

			Response<Status, FacePamphletProfile> response = FacePamphletService.login(name, password);

			if(response.status == Status.NotFound) getDialog().println(MESSAGE_USER_NOT_FOUND);
			if(response.status == Status.IncorrectPassword) getDialog().println(MESSAGE_INCORRECT_PASSWORD);
			else if(response.status == Status.Success){
				userProfile = response.data;
				break;
			}
		}
	}

	/** Import database */
	private void importDatabase() {
		dbImported = getDialog().readBoolean(MESSAGE_IMPORT, "Yes", "No");

		if(dbImported){
			println(FacePamphletService.importProfiles());
		}
	}

	/** Initializes interactors */
	private void initializeInteractors() {
		nameLabel = new JLabel("Name");

		nameTextField = new JTextField(TEXT_FIELD_SIZE);
		changeStatusTextField = new JTextField(TEXT_FIELD_SIZE);
		changePictureTextField = new JTextField(TEXT_FIELD_SIZE);

		lookupBtn = new JButton(COMMAND_LOOKUP);
		changeStatusBtn = new JButton(COMMAND_CHANGE_STATUS);
		changePictureBtn = new JButton(COMMAND_CHANGE_PICTURE);
		requestsPendingBtn = new JButton(getFriendRequestsCommand());
		requestSendBtn = new JButton(COMMAND_REQUEST_SEND);
		requestCancelBtn = new JButton(COMMAND_REQUEST_CANCEL);
		requestAcceptBtn = new JButton(COMMAND_REQUEST_ACCEPT);
		requestDeclineBtn = new JButton(COMMAND_REQUEST_DECLINE);
		removeFriendBtn = new JButton(COMMAND_REMOVE_FRIEND);
		changePasswordBtn = new JButton(COMMAND_CHANGE_PASSWORD);
		deleteProfileBtn = new JButton(COMMAND_DELETE_PROFILE);
		exportDatabaseBtn = new JButton(COMMAND_EXPORT_DATABASE);
	}

	/** Adds action commands to interactors */
	private void addActionCommandsToInteractors() {
		changeStatusTextField.setActionCommand(COMMAND_CHANGE_STATUS);
		changePictureTextField.setActionCommand(COMMAND_CHANGE_PICTURE);

		requestsPendingBtn.setActionCommand(COMMAND_REQUESTS_PENDING);
	}

	/** Adds action listeners */
	private void addActionListenersToInteractors() {
		changeStatusTextField.addActionListener(this);
		changePictureTextField.addActionListener(this);

		lookupBtn.addActionListener(this);
		changeStatusBtn.addActionListener(this);
		changePictureBtn.addActionListener(this);
		requestsPendingBtn.addActionListener(this);
		requestSendBtn.addActionListener(this);
		requestCancelBtn.addActionListener(this);
		requestAcceptBtn.addActionListener(this);
		requestDeclineBtn.addActionListener(this);
		removeFriendBtn.addActionListener(this);
		changePasswordBtn.addActionListener(this);
		deleteProfileBtn.addActionListener(this);
		exportDatabaseBtn.addActionListener(this);
	}

	/** Adds interactors in program */
	private void addInteractorsToProgram() {
		add(nameLabel, NORTH);
		add(nameTextField, NORTH);
		add(lookupBtn, NORTH);

		add(changeStatusTextField, WEST);
		add(changeStatusBtn, WEST);

		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		add(changePictureTextField, WEST);
		add(changePictureBtn, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		add(requestsPendingBtn, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		add(exportDatabaseBtn, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		add(changePasswordBtn, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		add(deleteProfileBtn, WEST);

		add(requestSendBtn, SOUTH);
		add(requestCancelBtn, SOUTH);
		add(removeFriendBtn, SOUTH);
		add(requestAcceptBtn, SOUTH);
		add(requestDeclineBtn, SOUTH);
	}

	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()){
			case COMMAND_LOOKUP -> actionLookup();
			case COMMAND_CHANGE_STATUS -> actionChangeStatus();
			case COMMAND_CHANGE_PICTURE -> actionChangePicture();
			case COMMAND_REQUEST_SEND -> actionRequestSend();
			case COMMAND_REQUEST_CANCEL -> actionRequestCancel();
			case COMMAND_REQUEST_ACCEPT -> actionRequestAccept();
			case COMMAND_REQUEST_DECLINE -> actionRequestDecline();
			case COMMAND_REMOVE_FRIEND -> actionRemoveFriend();
			case COMMAND_REQUESTS_PENDING -> actionRequestsPending();
			case COMMAND_CHANGE_PASSWORD -> actionChangePassword();
			case COMMAND_EXPORT_DATABASE -> actionExportDatabase();
			case COMMAND_DELETE_PROFILE -> FacePamphletService.deleteProfile(userProfile);
		}

		handleSouthButtons();
	}

	private void actionChangePassword(){
		while(true){
			String password = getDialog().readLine(MESSAGE_ENTER_PASSWORD + "  (Enter 0 to cancel)");

			if(password.equals("0")) break;

			Response<Status, FacePamphletProfile> response
					= FacePamphletService.changePassword(password, userProfile);

			if(response.status != Status.WeakPassword) break;

			getDialog().println(MESSAGE_WEAK_PASSWORD);
		}
	}

	private void actionRequestAccept(){
		FacePamphletService.addFriend(currentProfile, userProfile);
		requestsPendingBtn.setText(getFriendRequestsCommand());
	}

	private void actionRequestDecline(){
		FacePamphletService.cancelFriendRequest(userProfile, currentProfile);
		requestsPendingBtn.setText(getFriendRequestsCommand());
	}

	private void actionRemoveFriend(){
		FacePamphletService.removeFriend(currentProfile, userProfile);
		canvas.showMessage(MESSAGE_FRIEND_REMOVED);
	}

	private void actionRequestCancel(){
		FacePamphletService.cancelFriendRequest(currentProfile, userProfile);
		canvas.showMessage(MESSAGE_FRIEND_REQUEST_CANCEL);
	}

	private void actionRequestSend(){
		FacePamphletService.sendFriendRequest(currentProfile, userProfile);
		canvas.showMessage(MESSAGE_FRIEND_REQUEST_SENT);
	}

	private void actionRequestsPending(){
		Iterator<String> it = userProfile.getPendingRequests();
		ArrayList<FacePamphletProfile> toAccept = new ArrayList<>();
		ArrayList<FacePamphletProfile> toDecline = new ArrayList<>();

		while(it.hasNext()){
			String profileName = it.next();
			int accept;

			while(true){
				accept = getDialog()
						.readInt(MESSAGE_ADD_FRIEND_OR_NOT.replace("<PROFILENAME>", profileName));

				if(accept == 0 || accept == 1 || accept == 2) break;

				getDialog().println(MESSAGE_ENTER_CORRECT_NUMBER);
			}

			if(accept == 2) break;

			Response<Status, FacePamphletProfile> response = FacePamphletService.getProfile(profileName);

			if(response.status == Status.NotFound){
				getDialog().println(MESSAGE_COULD_NOT_ADD.replace("<PROFILENAME>", profileName));
				continue;
			}

			if(accept == 1) toAccept.add(response.data);
			else toDecline.add(response.data);
		}

		for(FacePamphletProfile profile : toAccept) FacePamphletService.addFriend(profile, userProfile);
		for(FacePamphletProfile profile : toDecline) FacePamphletService.cancelFriendRequest(userProfile, profile);

		requestsPendingBtn.setText(getFriendRequestsCommand());
	}

	/** Method which is invoked when user press Lookup button */
	private void actionLookup(){
		String name = nameTextField.getText();

		if(!isInputValid(name)) {
			getDialog().println(MESSAGE_BAD_REQUEST);
			return;
		}

		Response<Status, FacePamphletProfile> response = FacePamphletService.getProfile(name);

		if(response.status == Status.NotFound){
			canvas.showMessage(MESSAGE_PROFILE_NOT_FOUND.replace("<NAME>", name));
			return;
		}

		canvas.displayProfile(response.data);
		currentProfile = response.data;
		handleSouthButtons();
	}

	private void removeSouthButtons(){
		requestSendBtn.setVisible(false);
		requestCancelBtn.setVisible(false);
		removeFriendBtn.setVisible(false);
		requestAcceptBtn.setVisible(false);
		requestDeclineBtn.setVisible(false);
	}

	private void displaySouthButtons(){
		if(currentProfile == null) return;

		if(userProfile.isPending(currentProfile.getName())){
			requestAcceptBtn.setVisible(true);
			requestDeclineBtn.setVisible(true);
		}

		else if(currentProfile.isFriend(userProfile.getName())){
			removeFriendBtn.setVisible(true);
		}

		else if(currentProfile.isPending(userProfile.getName())) {
			requestCancelBtn.setVisible(true);
		}

		else if(currentProfile != userProfile) {
			requestSendBtn.setVisible(true);
		}
	}

	/** Method which is invoked when user press Export Database button */
	private void actionExportDatabase(){
		FacePamphletService.exportProfiles();
		getDialog().println("Exported successfully");
	}

	/** Method which is invoked when user press Change Status button */
	private void actionChangeStatus(){
		String status = changeStatusTextField.getText();

		userProfile.setStatus(status);

		canvas.displayProfile(userProfile);
		canvas.showMessage(MESSAGE_CHANGE_STATUS_SUCCESS.replace("<STATUS>", status));
		currentProfile = userProfile;
	}

	/** Method which is invoked when user press Change Picture button */
	private void actionChangePicture(){
		String fileName = changePictureTextField.getText();
		String imagePath = System.getProperty("user.dir") + "\\images\\" + fileName;

		try {
			if(new File(imagePath).isFile()){
				currentProfile.setImagePath(imagePath);
				canvas.displayProfile(userProfile);
				canvas.showMessage(MESSAGE_CHANGE_PICTURE_SUCCESS);
				currentProfile = userProfile;
			}else{
				handleImageError(fileName);
			}
		} catch (ErrorException ex) {
			handleImageError(fileName);
		}
	}

	/** Method which handles situation when there is no image with this filename */
	private void handleImageError(String fileName){
		canvas.showMessage(MESSAGE_CHANGE_PICTURE_ERROR.replace("<FILENAME>", fileName));
	}

	private String readName(){
		return getDialog().readLine(MESSAGE_ENTER_NAME);
	}

	private String readPassword(){
		return getDialog().readLine(MESSAGE_ENTER_PASSWORD);
	}

	private boolean isInputValid(String value){
		return !value.isBlank();
	}

	private String getFriendRequestsCommand(){
		println(userProfile.getPendingRequestsCount());
		return COMMAND_REQUESTS_PENDING
				.replace("<COUNT>", ""+userProfile.getPendingRequestsCount());
	}

	private void handleSouthButtons(){
		removeSouthButtons();
		displaySouthButtons();
	}
}
