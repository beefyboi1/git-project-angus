import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.nio.file.Path;

public class GIT {

    public static void main(String[] args) throws IOException, FileNotFoundException {
        //init repo stretch goal:
        // testInitRepo();
        // Files.delete(Path.of("git"));


        // blob stretch goal
        //fullTest();
        removeAll(Path.of("git"));
        initRepo();
        createTree("textFiles");
        


        // File example = new File("check.txt");
        // example.createNewFile();
        // example.renameTo(new File(("checker")));
    
    }
// public static String createTree(String path) throws IOException {
//     // Step 1: Prepare tree content
//     StringBuilder treeContent = new StringBuilder();
//     File folder = new File(path);
    
//     for (File file : folder.listFiles()) {
//         if (file.isFile()) {
//             blob(file.getPath());  // generate blob
//             String sha = GIT.hashFile(file.getPath());
//             treeContent.append("blob ").append(sha).append(" ").append(file.getName()).append("\n");
//         } else if (file.isDirectory()) {
//             String subtreeSha = createTree(file.getPath());  // recursion
//             treeContent.append("tree ").append(subtreeSha).append(" ").append(file.getName()).append("\n");
//         }
//     }

//     // Step 2: Write tree content to temp file
//     File tempFile = File.createTempFile("tree-", ".tmp");
//     FileWriter writer = new FileWriter(tempFile);
//     writer.write(treeContent.toString());
//     writer.close();

//     // Step 3: Hash the tree content
//     String treeSha = GIT.hashFile(tempFile.getPath());

//     // Step 4: Move temp file to objects folder with hash name
//     File treeObject = new File("git/objects/" + treeSha);
//     tempFile.renameTo(treeObject);

//     return treeSha;
// }



    public static String createTree(String path) throws IOException{
        String name = "folder";
        File folder = new File(path);
        // folder.mkdir();
        File tree = new File(name);
        FileWriter dir = new FileWriter(name);
        
        for (File file: folder.listFiles()){
            if (!file.isDirectory()){
                blob(file.getPath());
                dir.write("blob " + GIT.hashFile(file.getPath()) + " " + file.getPath() + "\n");
            }
            if (file.isDirectory()){
                dir.write("tree " + createTree(file.getPath()) + " " + file.getPath() + "\n");
            }
            
        }
        dir.close();
        String finish = GIT.hashFile(name);
        tree.renameTo(new File(("git/objects/" + finish)));
        return finish;

        
    }




    public static void fullTest() throws IOException{
        initRepo();
        FileWriter dir = new FileWriter("help.txt");
        dir.write("wassup");
        dir.close();

        FileWriter dir1 = new FileWriter("test.txt");
        dir1.write("help");
        dir1.close();



        blob("textFiles/help.txt");
        blob("textFiles/test.txt");

        try{
        if (Files.list(Path.of("git/objects")).count() == 0){
            System.out.println("there are no blobs");
        }
        else{
            System.out.println("Blobs exist");
        }
    }
    catch(IOException e){}
    }
    
public static void testInitRepo() throws IOException{
    initRepo();
        int check = 0;
        File dir = new File("git");
        if (!dir.exists()){
        check = 1;
        System.out.println("git directory was not made");
        }
        File dir1 = new File("git/objects");
        if (!dir1.exists()){
            check = 1;
            System.out.println("objects directory was not made");
        }

        File file = new File("git/index");
        if (!file.exists()){
            check = 1;
            System.out.println("index file was not made");
        }

        File file1 = new File("git/HEAD");
        if (!file1.exists()){
        check = 1;
        System.out.println("HEAD file was not made");
        }
        if (check == 0){
            System.out.println("all files were created by initRepo()");
        }
        if (dir.exists()){
            removeAll(Path.of("git"));
        }
}

public static void removeAll(Path path) throws IOException {
    if (Files.isDirectory(path)){




        try (java.util.stream.Stream<Path> entries = Files.list(path)){
        entries.forEach(p -> {
            try {
                removeAll(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
        // if (Files.list(path).count() > 0){
        //     System.out.println(Files.list(path).toArray()[0].toString());
        // }
    }
    Files.delete(path);
}


public static void blob(String path) throws FileNotFoundException {
    File file = new File("git/objects/" + GIT.hashFile(path));
    String input = "";
    boolean check = false;
     try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                input = input + line;
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter("git/objects/" + GIT.hashFile(path)));
            bw.write(input);
            bw.close();

            String name = "" + GIT.hashFile(path) + " " + path;
            try (BufferedReader br1 = new BufferedReader(new FileReader("git/index"))){
            String text = "";
            
            while ((line = br1.readLine()) != null) {
                text = line;
                if (text.equals(name)){
                    check = true;
                    System.out.println("cannot add identical files");
                }
            }
            br1.close();
        }catch (Exception e){
           System.out.println(e);
        };
            if (check == false){
                
            BufferedWriter bw1 = new BufferedWriter(new FileWriter("git/index", true));
            bw1.write("" + GIT.hashFile(path) + " " + path + "\n");
            bw1.close();
            }
        }
        catch(IOException e){
            //System.out.println("here" + e);
    }
    

    
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

        File file = new File("git/index");
        if (!file.exists()){
        check = 1;
        file.createNewFile();
        }

        File file1 = new File("git/HEAD");
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
