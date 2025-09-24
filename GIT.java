import java.io.*;

public class GIT {

    public static void main(String[] args) throws FileNotFoundException {
        initRepo();
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
