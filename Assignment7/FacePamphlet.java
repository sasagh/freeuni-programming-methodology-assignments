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
	private JButton sendFriendRequestBtn;
	private JButton cancelFriendRequestBtn;
	private JButton removeFriendBtn;
	private JButton deleteProfileBtn;
	private JButton exportDatabaseBtn;
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
		loginOrRegister();
		canvas = new FacePamphletCanvas();
		add(canvas);
		initializeInteractors();
		addActionCommandsToInteractors();
		addActionListenersToInteractors();
		setButtonVisibility();
		addInteractorsToProgram();
    }

	private void setButtonVisibility() {
		sendFriendRequestBtn.setVisible(false);
		cancelFriendRequestBtn.setVisible(false);
		removeFriendBtn.setVisible(false);
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
		sendFriendRequestBtn = new JButton(COMMAND_SEND_FRIEND_REQUEST);
		cancelFriendRequestBtn = new JButton(COMMAND_CANCEL_FRIEND_REQUEST);
		removeFriendBtn = new JButton(COMMAND_REMOVE_FRIEND);
		deleteProfileBtn = new JButton(COMMAND_DELETE_PROFILE);
		exportDatabaseBtn = new JButton(COMMAND_EXPORT_DATABASE);
	}

	/** Adds action commands to interactors */
	private void addActionCommandsToInteractors() {
		changeStatusTextField.setActionCommand(COMMAND_CHANGE_STATUS);
		changePictureTextField.setActionCommand(COMMAND_CHANGE_PICTURE);
	}

	/** Adds action listeners */
	private void addActionListenersToInteractors() {
		changeStatusTextField.addActionListener(this);
		changePictureTextField.addActionListener(this);

		lookupBtn.addActionListener(this);
		changeStatusBtn.addActionListener(this);
		changePictureBtn.addActionListener(this);
		sendFriendRequestBtn.addActionListener(this);
		cancelFriendRequestBtn.addActionListener(this);
		removeFriendBtn.addActionListener(this);
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

		add(exportDatabaseBtn, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		add(sendFriendRequestBtn, SOUTH);
		add(cancelFriendRequestBtn, SOUTH);
		add(removeFriendBtn, SOUTH);

		add(deleteProfileBtn, WEST);
	}

	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()){
			case COMMAND_LOOKUP -> actionLookup();
			case COMMAND_CHANGE_STATUS -> actionChangeStatus();
			case COMMAND_CHANGE_PICTURE -> actionChangePicture();
			case COMMAND_SEND_FRIEND_REQUEST -> FacePamphletService.sendFriendRequest(currentProfile, userProfile);
			case COMMAND_CANCEL_FRIEND_REQUEST -> FacePamphletService.cancelFriendRequest(currentProfile, userProfile);
			case COMMAND_REMOVE_FRIEND -> FacePamphletService.removeFriend(currentProfile, userProfile);
			case COMMAND_EXPORT_DATABASE -> actionExportDatabase();
			case COMMAND_DELETE_PROFILE -> FacePamphletService.deleteProfile(userProfile);
		}
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
		removeSouthButtons();
		displaySouthButtons();
	}

	private void removeSouthButtons(){
		sendFriendRequestBtn.setVisible(false);
		cancelFriendRequestBtn.setVisible(false);
		removeFriendBtn.setVisible(false);
	}

	private void displaySouthButtons(){
		if(currentProfile.isFriend(userProfile.getName())){
			removeFriendBtn.setVisible(true);
		}

		else if(userProfile.isPending(currentProfile.getName())) {
			cancelFriendRequestBtn.setVisible(true);
		}

		else if(currentProfile != userProfile) {
			sendFriendRequestBtn.setVisible(true);
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
}
