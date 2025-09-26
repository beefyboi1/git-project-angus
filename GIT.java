import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

public class GIT {

    public static void main(String[] args) throws FileNotFoundException {
        blob("help.txt");

    }
    

public static void blob(String path) throws FileNotFoundException {
    File file = new File("git/objects/" + GIT.hashFile(path));
    String input = "";
     try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                input = input + line;
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter("git/objects/" + GIT.hashFile(path)));
            bw.write(input);
            bw.close();

            BufferedWriter bw1 = new BufferedWriter(new FileWriter("git/objects/index", true));
            bw1.write("" + GIT.hashFile(path) + " " + path + "\n");
            bw1.close();
        }
        catch(IOException e){}
    

    
}


public static String hashFile(String path) throws FileNotFoundException {
    //System.out.println("hello");

    // debugger doenst work
    File file = new File(path);
    if (!file.exists()){
        throw new FileNotFoundException("file does not exist. double check file path");
    }
    String input = "";
     try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                input = input + line;
            }
        }
        catch(IOException e){}


    // Static getInstance method is called with hashing SHA
        
        

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        try{
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash =  md.digest(input.getBytes("UTF-8"));

        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 40)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
    catch(Exception e){
        System.out.println("error of type: error");
    }
    return null;
}

    public static void initRepo() throws FileNotFoundException {
        try{
        int check = 0;
        File dir = new File("git");
        if (!dir.exists()){
        check = 1;
        dir.mkdir();
        }
        File dir1 = new File("git/objects");
        if (!dir1.exists()){
        dir1.mkdir();
        }

        File file = new File("git/objects/index");
        if (!file.exists()){
        check = 1;
        file.createNewFile();
        }

        File file1 = new File("git/objects/HEAD");
        if (!file1.exists()){
        check = 1;
        file1.createNewFile();
        }
        if (check == 1){
        System.out.println("Git Repository Created");
        }
        else{
            System.out.println("Git Repository Already Exists");
        }
    }
    catch (Exception e){}
    }


}
