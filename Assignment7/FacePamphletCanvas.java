import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas
		implements FacePamphletConstants {

	private GLabel message;
	private GLabel name;
	private GImage image;
	private GLabel status;

	public FacePamphletCanvas(){
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
	}

	/**
	 * This method displays a message string near the bottom of the
	 * canvas.  Every time this method is called, the previously
	 * displayed message (if any) is replaced by the new message text
	 * passed in.
	 */
	public void showMessage(String msg) {
		if (message != null) remove(message);
		message = new GLabel(msg);
		message.setFont(MESSAGE_FONT);
		add(message, getWidth()/2.0 - message.getWidth()/2, getHeight() - BOTTOM_MESSAGE_MARGIN);
	}

	public void showGuests(Iterator<FacePamphletProfile> it){
		removeAll();
		GLabel guestsLabel = new GLabel("Guests:");
		guestsLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(guestsLabel, getWidth()/5.0, name.getY() + IMAGE_MARGIN);

		double margin = 0;

		while(it.hasNext()){
			FacePamphletProfile guest = it.next();
			String labelText = FacePamphletService.profileExists(guest.getName()) ? guest.getName() : "Deleted user";
			GLabel guestLabel = new GLabel(labelText);
			guestLabel.setFont(PROFILE_FRIEND_FONT);
			add(guestLabel, getWidth() / 5.0, guestsLabel.getY() + guestLabel.getHeight() + margin);
			margin += guestLabel.getHeight();
		}
	}

	/**
	 * This method displays the given profile on the canvas.  The
	 * canvas is first cleared of all existing items (including
	 * messages displayed near the bottom of the screen) and then the
	 * given profile is displayed.  The profile display includes the
	 * name of the user from the profile, the corresponding image
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile, ArrayList<String> friends) {
		removeAll();
		if (profile == null) return;
		displayName(profile);
		displayImage(profile);
		displayStatus(profile);
		displayFriends(friends);
	}

	/** Displays name */
	private void displayName(FacePamphletProfile profile) {
		name = new GLabel(profile.getName());
		name.setFont(PROFILE_NAME_FONT);
		double height = name.getAscent();
		name.setColor(Color.blue);
		add(name, LEFT_MARGIN, TOP_MARGIN + height);
	}

	/** Displays image */
	private void displayImage(FacePamphletProfile profile) {
		if (profile.getImagePath().isBlank()) {
			GRect emptyRectangle = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(emptyRectangle, LEFT_MARGIN, name.getY() + IMAGE_MARGIN);

			GLabel emptyLabel = new GLabel("No Image");
			emptyLabel.setFont(PROFILE_IMAGE_FONT);
			double labelX = LEFT_MARGIN + (emptyRectangle.getWidth()- emptyLabel.getWidth())/2;
			double labelY =
					emptyRectangle.getY() +
					(emptyRectangle.getHeight() + (emptyLabel.getAscent() - emptyLabel.getDescent()))/2;

			add(emptyLabel, labelX, labelY);
		} else {
			image = new GImage(profile.getImagePath());
			image.setBounds(LEFT_MARGIN, name.getY() + IMAGE_MARGIN, IMAGE_WIDTH, IMAGE_HEIGHT);
			add(image);
		}
	}

	/** Displays status */
	private void displayStatus(FacePamphletProfile profile) {
		if (!profile.getStatus().equals("")) {
			status = new GLabel(profile.getName() + " is " + profile.getStatus());
		} else {
			status = new GLabel("No current status");
		}

		status.setFont(PROFILE_STATUS_FONT);
		double statusY = name.getY() + IMAGE_MARGIN + IMAGE_HEIGHT + status.getAscent() + STATUS_MARGIN;
		add(status, LEFT_MARGIN, statusY);
	}

	/** Displays friends */
	private void displayFriends(ArrayList<String> friends) {
		GLabel friendsLabel = new GLabel("Friends:");
		friendsLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friendsLabel, getWidth()/2.0, name.getY() + IMAGE_MARGIN);

		double margin = 0;

		for (String friendName : friends) {
			GLabel friendLabel = new GLabel(friendName);
			friendLabel.setFont(PROFILE_FRIEND_FONT);
			add(friendLabel, getWidth() / 2.0, friendsLabel.getY() + friendLabel.getHeight() + margin);
			margin += friendLabel.getHeight();
		}
	}
}