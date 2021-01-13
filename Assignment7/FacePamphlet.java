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
import java.io.Console;
import java.util.Iterator;
import javax.swing.*;

public class FacePamphlet extends ConsoleProgram
					implements FacePamphletConstants {
	private JLabel nameLabel;
	private JTextField nameTextField;
	private JTextField changeStatusTextField;
	private JTextField changePictureTextField;
	private JTextField addFriendTextField;
	private JButton addBtn;
	private JButton deleteBtn;
	private JButton lookupBtn;
	private JButton changeStatusBtn;
	private JButton changePictureBtn;
	private JButton addFriendBtn;
	private FacePamphletProfile currentProfile;

	private static FacePamphletDatabase db;

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		db = new FacePamphletDatabase();
		initializeInteractors();
		addActionCommandsToInteractors();
		addActionListenersToInteractors();
		addInteractorsToProgram();
    }

	/** Initializes interactors */
	private void initializeInteractors() {
		nameLabel = new JLabel("Name");

		nameTextField = new JTextField(TEXT_FIELD_SIZE);
		changeStatusTextField = new JTextField(TEXT_FIELD_SIZE);
		changePictureTextField = new JTextField(TEXT_FIELD_SIZE);
		addFriendTextField = new JTextField(TEXT_FIELD_SIZE);

		addBtn = new JButton(ADD_COMMAND);
		deleteBtn = new JButton(DELETE_COMMAND);
		lookupBtn = new JButton(LOOKUP_COMMAND);
		changeStatusBtn = new JButton(CHANGE_STATUS_COMMAND);
		changePictureBtn = new JButton(CHANGE_PICTURE_COMMAND);
		addFriendBtn = new JButton(ADD_FRIEND_COMMAND);
	}

	/** Adds action commands to interactors */
	private void addActionCommandsToInteractors() {
		changeStatusTextField.setActionCommand(CHANGE_STATUS_COMMAND);
		changePictureTextField.setActionCommand(CHANGE_PICTURE_COMMAND);
		addFriendTextField.setActionCommand(ADD_FRIEND_COMMAND);
	}

	/** Adds action listeners */
	private void addActionListenersToInteractors() {
		changeStatusTextField.addActionListener(this);
		changePictureTextField.addActionListener(this);
		addFriendTextField.addActionListener(this);

		addBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		lookupBtn.addActionListener(this);
		changeStatusBtn.addActionListener(this);
		changePictureBtn.addActionListener(this);
		addFriendBtn.addActionListener(this);
	}

	/** Adds interactors in program */
	private void addInteractorsToProgram() {
		add(nameLabel, NORTH);
		add(nameTextField, NORTH);
		add(addBtn, NORTH);
		add(deleteBtn, NORTH);
		add(lookupBtn, NORTH);

		add(changeStatusTextField, WEST);
		add(changeStatusBtn, WEST);

		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		add(changePictureTextField, WEST);
		add(changePictureBtn, WEST);

		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		add(addFriendTextField, WEST);
		add(addFriendBtn, WEST);
	}

	/**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent event) {
		print(event.getActionCommand() + ": \n");

		switch (event.getActionCommand()){
			case ADD_COMMAND -> actionAdd();
			case DELETE_COMMAND -> actionDelete();
			case LOOKUP_COMMAND -> actionLookup();
			case CHANGE_STATUS_COMMAND -> actionChangeStatus();
			case CHANGE_PICTURE_COMMAND -> actionChangePicture();
			case ADD_FRIEND_COMMAND -> actionAddFriend();
		}

		println("-----------------------------");
	}

	/** Method which is invoked when user press Add button"*/
	private void actionAdd(){
		String name = nameTextField.getText();
		Response<Status, FacePamphletProfile> response = db.addProfile(new FacePamphletProfile(name));

		if(response.status == Status.BadRequest){
			getDialog().println(MESSAGE_BAD_REQUEST);
			return;
		}

		if(response.status == Status.Add){
			getDialog().println(MESSAGE_ADD);
		}else if(response.status == Status.Update){
			getDialog().println(MESSAGE_UPDATE);
		}

		currentProfile = response.data;
	}

	/** Method which is invoked when user press Delete button */
	private void actionDelete(){
		String name = nameTextField.getText();
		Response<Status, FacePamphletProfile> response = db.getProfile(name);

		if(response.status == Status.NotFound){
			getDialog().println(MESSAGE_PROFILE_NOT_FOUND);
			return;
		}

		Iterator<String> friendNames = response.data.getFriends();

		while(friendNames.hasNext()){
			FacePamphletProfile friendProfile = db.getProfile(friendNames.next()).data;

			friendProfile.removeFriend(currentProfile.getName());
			db.addProfile(friendProfile);
		}

		db.deleteProfile(name);
		getDialog().println(MESSAGE_DELETE);

		currentProfile = null;
	}

	/** Method which is invoked when user press Lookup button */
	private void actionLookup(){
		String name = nameTextField.getText();
		Response<Status, FacePamphletProfile> response = db.getProfile(name);

		if(response.status == Status.NotFound){
			getDialog().println(MESSAGE_PROFILE_NOT_FOUND);
			return;
		}

		handleStatus(response.status, response.data);
		currentProfile = response.data;
	}

	/** Method which is invoked when user press Change Status button */
	private void actionChangeStatus(){

		if(currentProfile == null){
			getDialog().println(MESSAGE_NO_PROFILE_ACTIVE);
			return;
		}

		String status = changeStatusTextField.getText();

		currentProfile.setStatus(status);
		Response<Status, FacePamphletProfile> response = db.addProfile(currentProfile);

		getDialog().println(MESSAGE_SUCCESS);

		handleStatus(response.status, response.data);
	}

	/** Method which is invoked when user press Change Picture button */
	private void actionChangePicture(){

		if(currentProfile == null){
			getDialog().println(MESSAGE_NO_PROFILE_ACTIVE);
			return;
		}

		String imagePath = changePictureTextField.getText();
		GImage image = null;

		try {
			image = new GImage(imagePath);
		} catch (ErrorException ex) {
			getDialog().println(MESSAGE_PROFILE_NOT_FOUND);
		}

		if(image != null){
			currentProfile.setImage(image);
			db.addProfile(currentProfile);
			getDialog().println(MESSAGE_SUCCESS);
		}
	}

	/** Method which is invoked when user press Add friend button */
	private void actionAddFriend(){

		if(currentProfile == null){
			getDialog().println("Currently no profile is active, please choose one");
			return;
		}

		String friendName = addFriendTextField.getText();
		Response<Status, FacePamphletProfile> response = db.getProfile(friendName);

		if(response.status != Status.Success){
			getDialog().println(MESSAGE_PROFILE_NOT_FOUND);
			return;
		}

		FacePamphletProfile friendProfile = response.data;

		if(currentProfile.isInFriendList(friendName)){
			getDialog().println(MESSAGE_ALREADY_FRIEND);
			return;
		}

		currentProfile.addFriend(friendProfile);
		friendProfile.addFriend(currentProfile);

		db.addProfile(currentProfile);
		db.addProfile(friendProfile);

		getDialog().println(MESSAGE_SUCCESS);
	}

	private void handleStatus(Status status, FacePamphletProfile profile){
    	switch (status){
			case Success -> println("Success: " + profile.toString());
			case Add -> println("Add: new profile: " + profile.toString());
			case Update -> println("Update: profile already exists: " + profile.toString());
			case Delete -> println("Deleted successfully");
			case NotFound -> println("Profile with this name does not exist");
			case BadRequest -> println("Name should not be empty");
		}
	}
}
