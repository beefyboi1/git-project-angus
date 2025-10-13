the initRepo method makes a git directory and a objects subdirectory inside git. It then creates a Head and index flie inside objects.

to test initRepo I made sure all of the files were created

hashFile method takes in the name of the file and reads the text from the file and turns it into the file key using SHA-1

Blob(String path) creates a blob in the objects folder as well as updates the index file.

the indexTree() method only works if all the files are in the same folder inside Git-Project-Angus.