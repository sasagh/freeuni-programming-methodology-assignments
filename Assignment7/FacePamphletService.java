import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

class FacePamphletService implements FacePamphletConstants{
    private static final FacePamphletDatabase db = new FacePamphletDatabase();
    private static final String exportDirectory = System.getProperty("user.dir") + "\\";
    private static final String fileName = "database.dtb";

    /** Method for exporting database */
    public static boolean exportProfiles(){
        Hashtable<String, FacePamphletProfile> profiles = db.getProfiles();
        try {
            FileOutputStream fileOut = new FileOutputStream(exportDirectory + fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(profiles);
            out.close();
            fileOut.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Method for importing database */
    public static boolean importProfiles(){
        Hashtable<String, FacePamphletProfile> profiles;
        try {
            FileInputStream fileIn = new FileInputStream(exportDirectory + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            profiles = (Hashtable) in.readObject();
            in.close();
            fileIn.close();
            db.setProfiles(profiles);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    /** Method for registering */
    public static Response<Status, FacePamphletProfile> register(String name, String password){
        if(profileExists(name)) return alreadyExists();

        if(!isPasswordStrong(password)) return weakPassword();

        FacePamphletProfile profile = new FacePamphletProfile(name, hashString(password));
        db.create(name, profile);

        return success(profile);
    }

    /** Method for login */
    public static Response<Status, FacePamphletProfile> login(String name, String password){
        FacePamphletProfile profile = db.getProfileByName(name);

        if(profile == null) return notFound();

        return isCredentialsCorrect(name, password, profile) ? success(profile) : incorrectPassword();
    }

    /** Returns profile by name */
    public static Response<Status, FacePamphletProfile> getProfile(String name){
        FacePamphletProfile profile = db.getProfileByName(name);

        return profile != null ? success(profile) : notFound();
    }

    /** Deletes profile */
    public static Response<Status, FacePamphletProfile> deleteProfile(FacePamphletProfile profile){
        Iterator<FacePamphletProfile> it = profile.getFriendList();

        while(it.hasNext()){
            FacePamphletProfile friendProfile = it.next();
            friendProfile.removeFriend(profile.getName());
        }

        it = profile.getSentRequests();

        while(it.hasNext()){
            FacePamphletProfile prof = it.next();
            prof.removePendingRequest(profile);
        }

        it = profile.getPendingRequests();

        while(it.hasNext()){
            FacePamphletProfile prof = it.next();
            prof.removeSentRequest(profile);
        }

        db.delete(profile.getName());
        return success();
    }

    /** Method for sending friend request */
    public static void sendFriendRequest(FacePamphletProfile friendProfile, FacePamphletProfile userProfile){
        friendProfile.addPendingRequest(userProfile);
        userProfile.addSentRequest(friendProfile);
    }

    /** Method for canceling friend request */
    public static void cancelFriendRequest(FacePamphletProfile currentProfile, FacePamphletProfile userProfile){
        currentProfile.removePendingRequest(userProfile);
        userProfile.removeSentRequest(currentProfile);
    }

    /** Method for adding friend */
    public static void addFriend(FacePamphletProfile friendProfile, FacePamphletProfile userProfile){
        userProfile.addFriend(friendProfile);
        friendProfile.addFriend(userProfile);
        userProfile.removePendingRequest(friendProfile);
        friendProfile.removeSentRequest(userProfile);
    }

    /** Method for removing friend */
    public static void removeFriend(FacePamphletProfile friendProfile, FacePamphletProfile userProfile){
        userProfile.removeFriend(friendProfile.getName());
        friendProfile.removeFriend(userProfile.getName());
    }

    /** Method for changing password */
    public static Response<Status, FacePamphletProfile> changePassword(String password, FacePamphletProfile profile){
        if(!isPasswordStrong(password)) return weakPassword();
        profile.setPassword(hashString(password));

        return success(profile);
    }

    /** Method for making friend list public */
    public static void makeFriendListPublic(FacePamphletProfile profile){
        profile.setIsFriendListPublic(true);
    }

    /** Method for making friend list private */
    public static void makeFriendListPrivate(FacePamphletProfile profile){
        profile.setIsFriendListPublic(false);
    }

    /** Method returns mutual friends of two profiles */
    public static ArrayList<String>
                        getMutualFriends(FacePamphletProfile currentProfile, FacePamphletProfile userProfile){
        Iterator<FacePamphletProfile> it = userProfile.getFriendList();

        ArrayList<String> mutualFriends = new ArrayList<>();

        while(it.hasNext()){
            FacePamphletProfile profile = it.next();

            if(profile.isFriend(currentProfile)) {
                mutualFriends.add(profile.getName());
            }
        }

        return mutualFriends;
    }

    /** Method for hashing string */
    private static String hashString(String input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    /** Method for checking credentials */
    private static boolean isCredentialsCorrect(String name, String password, FacePamphletProfile profile){
        return profile.getName().equals(name) && profile.getPasswordHash().equals(hashString(password));
    }

    /** Method for checking if profile exists */
    private static boolean profileExists(String name){
        return db.getProfileByName(name) != null;
    }

    /** Method for checking if the password is strong */
    private static boolean isPasswordStrong(String password){
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        return password.matches(pattern);
    }

    /** Methods bellow are to return responses */
    private static Response<Status, FacePamphletProfile> notFound(){
        return new Response<>(Status.NotFound, null);
    }

    private static Response<Status, FacePamphletProfile> success(){
        return new Response<>(Status.Success, null);
    }

    private static Response<Status, FacePamphletProfile> success(FacePamphletProfile data){
        return new Response<>(Status.Success, data);
    }

    private static Response<Status, FacePamphletProfile> alreadyExists(){
        return new Response<>(Status.AlreadyExists, null);
    }

    private static Response<Status, FacePamphletProfile> weakPassword(){
        return new Response<>(Status.WeakPassword, null);
    }

    private static Response<Status, FacePamphletProfile> incorrectPassword(){
        return new Response<>(Status.IncorrectPassword, null);
    }
}
