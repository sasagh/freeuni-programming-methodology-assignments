import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Hashtable;
import java.util.Iterator;

class FacePamphletService implements FacePamphletConstants{
    private static FacePamphletDatabase db = new FacePamphletDatabase();
    private static final String exportDirectory = System.getProperty("user.dir") + "\\database\\";
    private static final String fileName = "database.dtb";

    public static void exportProfiles(){
        Hashtable<String, FacePamphletProfile> profiles = db.getProfiles();
        try {
            FileOutputStream fileOut = new FileOutputStream(exportDirectory + fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(profiles);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String importProfiles(){
        Hashtable<String, FacePamphletProfile> profiles = null;
        try {
            FileInputStream fileIn = new FileInputStream(exportDirectory + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            profiles = (Hashtable) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        db.setProfiles(profiles);
        return profiles.toString();
    }

    public static Response<Status, FacePamphletProfile> register(String name, String password){
        if(profileExists(name)) return alreadyExists();

        if(!isPasswordStrong(password)) return weakPassword();

        FacePamphletProfile profile = new FacePamphletProfile(name, hashString(password));
        db.create(name, profile);

        return success(profile);
    }

    public static Response<Status, FacePamphletProfile> login(String name, String password){
        FacePamphletProfile profile = db.getProfileByName(name);

        if(profile == null) return notFound();

        return isCredentialsCorrect(name, password, profile) ? success(profile) : incorrectPassword();
    }

    public static Response<Status, FacePamphletProfile> getProfile(String name){
        FacePamphletProfile profile = db.getProfileByName(name);

        return profile != null ? success(profile) : notFound();
    }

    public static Response<Status, FacePamphletProfile> deleteProfile(FacePamphletProfile profile){
        Iterator<FacePamphletProfile> it = profile.getFriendList();

        if(it != null){
            while(it.hasNext()){
                FacePamphletProfile friendProfile = it.next();
                friendProfile.removeFriend(profile.getName());
            }
        }

        db.delete(profile.getName());
        return success();
    }

    public static void sendFriendRequest(FacePamphletProfile friendProfile, FacePamphletProfile userProfile){
        friendProfile.addPendingRequest(userProfile.getName());
    }

    public static void cancelFriendRequest(FacePamphletProfile friendProfile, FacePamphletProfile userProfile){
        friendProfile.removePendingRequest(userProfile.getName());
    }

    public static void addFriend(FacePamphletProfile friendProfile, FacePamphletProfile userProfile){
        userProfile.addFriend(friendProfile);
        friendProfile.addFriend(userProfile);
        friendProfile.removePendingRequest(userProfile.getName());
    }

    public static void removeFriend(FacePamphletProfile friendProfile, FacePamphletProfile userProfile){
        userProfile.removeFriend(friendProfile.getName());
        friendProfile.removeFriend(userProfile.getName());
    }

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

    private static boolean isCredentialsCorrect(String name, String password, FacePamphletProfile profile){
        return profile.getName().equals(name) && profile.getPasswordHash().equals(hashString(password));
    }

    private static boolean profileExists(String name){
        return db.getProfileByName(name) != null;
    }

    private static boolean isPasswordStrong(String password){
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        return password.matches(pattern);
    }

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
