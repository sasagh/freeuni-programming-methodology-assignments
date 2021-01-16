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
import java.util.Iterator;
import javax.swing.*;

public class FacePamphlet extends Program
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
	private FacePamphletCanvas canvas;

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		db = new FacePamphletDatabase();
		canvas = new FacePamphletCanvas();
		add(canvas);
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
		switch (event.getActionCommand()){
			case ADD_COMMAND -> actionAdd();
			case DELETE_COMMAND -> actionDelete();
			case LOOKUP_COMMAND -> actionLookup();
			case CHANGE_STATUS_COMMAND -> actionChangeStatus();
			case CHANGE_PICTURE_COMMAND -> actionChangePicture();
			case ADD_FRIEND_COMMAND -> actionAddFriend();
		}
	}

	/** Method which is invoked when user press Add button"*/
	private void actionAdd(){
		String name = nameTextField.getText();
		Response<Status, FacePamphletProfile> response = db.addProfile(new FacePamphletProfile(name));

		if(response.status == Status.BadRequest){
			getDialog().println(MESSAGE_BAD_REQUEST);
			return;
		}

		currentProfile = response.data;
		canvas.displayProfile(currentProfile);

		if(response.status == Status.Add){
			canvas.showMessage(MESSAGE_ADD);
		}else if(response.status == Status.Update){
			canvas.showMessage(MESSAGE_UPDATE.replace("<NAME>", name));
		}
	}

	/** Method which is invoked when user press Delete button */
	private void actionDelete(){
		String name = nameTextField.getText();
		Response<Status, FacePamphletProfile> response = db.getProfile(name);

		if(response.status == Status.BadRequest){
			getDialog().println(MESSAGE_BAD_REQUEST);
			return;
		}

		if(response.status == Status.NotFound){
			canvas.showMessage(MESSAGE_PROFILE_NOT_FOUND.replace("<NAME>", name));
			return;
		}

		Iterator<String> friendNames = response.data.getFriends();

		while(friendNames.hasNext()){
			FacePamphletProfile friendProfile = db.getProfile(friendNames.next()).data;

			friendProfile.removeFriend(name);
			db.addProfile(friendProfile);
		}

		db.deleteProfile(name);
		currentProfile = null;
		canvas.removeAll();

		canvas.showMessage(MESSAGE_DELETE.replace("<NAME>", name));
	}

	/** Method which is invoked when user press Lookup button */
	private void actionLookup(){
		String name = nameTextField.getText();
		Response<Status, FacePamphletProfile> response = db.getProfile(name);

		if(response.status == Status.NotFound){
			canvas.removeAll();
			canvas.showMessage(MESSAGE_PROFILE_NOT_FOUND.replace("<NAME>", name));
			return;
		}

		if(response.status == Status.BadRequest){
			getDialog().println(MESSAGE_BAD_REQUEST);
			return;
		}

		currentProfile = response.data;
		canvas.displayProfile(currentProfile);
		canvas.showMessage(MESSAGE_LOOKUP.replace("<NAME>", name));
	}

	/** Method which is invoked when user press Change Status button */
	private void actionChangeStatus(){

		if(currentProfile == null){
			canvas.showMessage(MESSAGE_CHANGE_STATUS_NO_ACTIVE_PROFILE);
			return;
		}

		String status = changeStatusTextField.getText();

		currentProfile.setStatus(status);

		db.addProfile(currentProfile);
		canvas.displayProfile(currentProfile);
		canvas.showMessage(MESSAGE_CHANGE_STATUS_SUCCESS.replace("<STATUS>", status));
	}

	/** Method which is invoked when user press Change Picture button */
	private void actionChangePicture(){

		if(currentProfile == null){
			canvas.showMessage(MESSAGE_CHANGE_PICTURE_NO_ACTIVE_PROFILE);
			return;
		}
		String fileName = changePictureTextField.getText();
		String imagePath = System.getProperty("user.dir") + "\\images\\" + fileName;
		GImage image;

		try {
			image = new GImage(imagePath);
		} catch (ErrorException ex) {
			canvas.showMessage(MESSAGE_CHANGE_PICTURE_ERROR.replace("<FILENAME>", fileName));
			return;
		}

		currentProfile.setImage(image);
		db.addProfile(currentProfile);
		canvas.displayProfile(currentProfile);
		canvas.showMessage(MESSAGE_CHANGE_PICTURE_SUCCESS);
	}

	/** Method which is invoked when user press Add friend button */
	private void actionAddFriend(){

		if(currentProfile == null){
			canvas.showMessage(MESSAGE_ADD_FRIEND_NO_ACTIVE_PROFILE);
			return;
		}

		String friendName = addFriendTextField.getText();
		Response<Status, FacePamphletProfile> response = db.getProfile(friendName);

		if(response.status == Status.NotFound){
			canvas.showMessage(MESSAGE_PROFILE_NOT_FOUND.replace("<NAME>", friendName));
			return;
		}

		FacePamphletProfile friendProfile = response.data;

		if(currentProfile.getName().equals(friendName)){
			getDialog().showErrorMessage(MESSAGE_ADD_FRIEND_SELF_FRIENDSHIP);
			return;
		}

		if(currentProfile.isInFriendList(friendName)){
			canvas.showMessage(MESSAGE_ADD_FRIEND_ALREADY_FRIEND
							.replace("<NAME>", currentProfile.getName())
							.replace("<FRIEND_NAME>", friendName));
			return;
		}

		currentProfile.addFriend(friendProfile);
		friendProfile.addFriend(currentProfile);

		db.addProfile(currentProfile);
		db.addProfile(friendProfile);

		canvas.displayProfile(currentProfile);
		canvas.showMessage(MESSAGE_ADD_FRIEND_SUCCESS.replace("<FRIEND_NAME>", friendName));
	}
}
