import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


public class GIT {

    public static void main(String[] args) throws IOException, FileNotFoundException {
        //init repo stretch goal:
        // testInitRepo();
        // Files.delete(Path.of("git"));


        // blob stretch goal
        //fullTest();
        removeAll(Path.of("git"));
        initRepo();

        File file = new File("textFiles/extra/fanta.txt");
        Files.write(Paths.get("textFiles/extra/fanta.txt"), "check".getBytes());
        File file1 = new File("textFiles/help.txt");
        Files.write(Paths.get("textFiles/help.txt"), "wassup".getBytes());
        File file2 = new File("textFiles/test.txt");
        Files.write(Paths.get("textFiles/test.txt"), "help".getBytes());
        blob("textFiles/extra/fanta.txt");
        blob("textFiles/help.txt");
        blob("textFiles/test.txt");
        // blob("test.txt");
        //blob("test.txt");
        
        indexTree();
        


        // File example = new File("check.txt");
        // example.createNewFile();
        // example.renameTo(new File(("checker")));
    
    }


    public static void indexTree() throws IOException{
        int count = 0;
        List<dirClass> workingList = new ArrayList<>();
        List<String> entries = Files.readAllLines(Path.of("git/index"));
        for (String line : entries) {
            dirClass directories = new dirClass("blob", line.split(" ")[0], line.split(" ")[1]);
            workingList.add(directories);
        }
        if (workingList.isEmpty()){
            return;
        }
        while ((workingList.size() > 1 || checkList(workingList)) || count++ < 1) {
            workingList.sort(Comparator.comparingInt(d -> d.toString().split("/").length).reversed()
                    .thenComparing(d -> d.toString()));
            String leafMost;
            if (count == 1){
                leafMost = workingList.get(0).toString();
            }
            else{
                leafMost = workingList.get(0).toString().substring(0, workingList.get(0).toString().lastIndexOf("/"));
            }
            StringBuilder tree = new StringBuilder();
            for (int i = 0; i < workingList.size(); i++) {
                if (workingList.get(i).toString().startsWith(leafMost)) {
                    if (count == 1){
                        tree.append(workingList.get(i).type + " " + workingList.get(i).sha + " (root)");
                    }
                    else{
                        tree.append(workingList.get(i).fullPathName());
                    }
                    tree.append("\n");
                    workingList.remove(i);
                    i--;
                }
            }
            if (tree.length() > 0) {
                tree.deleteCharAt(tree.length() - 1);
            }
            try (FileWriter fw = new FileWriter("temp")){
                fw.write(tree.toString());
            }
            String sha = hashFile("temp");
            Files.delete(Path.of("temp"));
            Path blobPath = Path.of("git/objects/" + sha);
 
            Files.createFile(blobPath);
            try (FileWriter fw = new FileWriter(blobPath.toString())) {
                fw.write(tree.toString());
            }
 
            dirClass treedir = new dirClass("tree", sha, leafMost);
            workingList.add(treedir);
        }
    }

    private static boolean checkList(List<dirClass> workingList){
        if (workingList.isEmpty()){
            return false;
        }
        return workingList.get(0).toString().contains("/");
    }

    private static class dirClass { // Credit to Darren
        String type;
        String sha;
        String pathname;

        public dirClass(String type, String sha, String pathname){
            this.type = type;
            this.sha = sha;
            this.pathname = pathname;
        }

        @Override
        public String toString() {
            return pathname;
        }

        public String fullPathName(){
            return type + " " + sha + " " + pathname;
        }
    }



    public static String createTree(String path) throws IOException{
        String name = "folder" + String.valueOf(Math.random());
        File folder = new File(path);
        // folder.mkdir();
        File tree = new File(name);
        FileWriter directories = new FileWriter(name);
        
        for (File file: folder.listFiles()){
            if (!file.isDirectory()){
                blob(file.getPath());
                directories.write("blob " + GIT.hashFile(file.getPath()) + " " + file.getPath() + "\n");
            }
            if (file.isDirectory()){
                directories.write("tree " + createTree(file.getPath()) + " " + file.getPath() + "\n");
            }
            
        }
        directories.close();
        String finish = GIT.hashFile(name);
        tree.renameTo(new File(("git/objects/" + finish)));
        return finish;

        
    }




    public static void fullTest() throws IOException{
        initRepo();
        FileWriter directories = new FileWriter("help.txt");
        directories.write("wassup");
        directories.close();

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
        File directories = new File("git");
        if (!directories.exists()){
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
        if (directories.exists()){
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
        File directories = new File("git");
        if (!directories.exists()){
        check = 1;
        directories.mkdir();
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
