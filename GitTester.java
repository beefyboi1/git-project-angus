import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * GitTester - A comprehensive tester for GIT.java functionality
 * This tester programmatically performs the same operations as the main method in GIT.java
 */
public class GitTester {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== GitTester Starting ===");
            
            // Test 1: Repository initialization and cleanup
            testRepositoryInitialization();
            
            // Test 2: File creation and content writing
            testFileCreation();
            
            // Test 3: Blob operations
            testBlobOperations();
            
            // Test 4: Index tree creation
            testIndexTreeCreation();
            
            // Test 5: Verify final state
            testFinalState();
            
            System.out.println("=== GitTester Completed Successfully ===");
            
        } catch (Exception e) {
            System.err.println("GitTester failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 1: Repository initialization and cleanup
     * This mirrors the removeAll() and initRepo() calls in GIT.java main
     */
    private static void testRepositoryInitialization() throws IOException {
        System.out.println("\n--- Test 1: Repository Initialization ---");
        
        // Clean up any existing git directory (mirrors removeAll(Path.of("git")) in main)
        if (Files.exists(Path.of("git"))) {
            System.out.println("Removing existing git directory...");
            removeAll(Path.of("git"));
        }
        if (Files.exists(Path.of("textFiles"))) {
            System.out.println("Removing existing textFiles directory...");
            removeAll(Path.of("textFiles"));
        }
        
        // Initialize repository (mirrors initRepo() in main)
        System.out.println("Initializing new git repository...");
        GIT.initRepo();
        
        // Verify repository structure was created
        verifyRepositoryStructure();
        
        System.out.println("✓ Repository initialization test passed");
    }
    
    /**
     * Test 2: File creation and content writing
     * This mirrors the file creation operations in GIT.java main
     */
    private static void testFileCreation() throws IOException {
        System.out.println("\n--- Test 2: File Creation ---");
        
        // Create textFiles directory structure if it doesn't exist
        Files.createDirectories(Paths.get("textFiles/extra"));
        
        // Create and write to fanta.txt (mirrors Files.write in main)
        System.out.println("Creating textFiles/extra/fanta.txt with content 'check'...");
        Files.write(Paths.get("textFiles/extra/fanta.txt"), "check".getBytes());
        
        // Create and write to help.txt (mirrors Files.write in main)
        System.out.println("Creating textFiles/help.txt with content 'wassup'...");
        Files.write(Paths.get("textFiles/help.txt"), "wassup".getBytes());
        
        // Create and write to test.txt (mirrors Files.write in main)
        System.out.println("Creating textFiles/test.txt with content 'help'...");
        Files.write(Paths.get("textFiles/test.txt"), "help".getBytes());
        
        // Verify files were created with correct content
        verifyFileContent("textFiles/extra/fanta.txt", "check");
        verifyFileContent("textFiles/help.txt", "wassup");
        verifyFileContent("textFiles/test.txt", "help");
        
        System.out.println("✓ File creation test passed");
    }
    
    /**
     * Test 3: Blob operations
     * This mirrors the blob() calls in GIT.java main
     */
    private static void testBlobOperations() throws IOException {
        System.out.println("\n--- Test 3: Blob Operations ---");
        
        // Create blobs for each file (mirrors blob() calls in main)
        System.out.println("Creating blob for textFiles/extra/fanta.txt...");
        GIT.blob("textFiles/extra/fanta.txt");
        
        System.out.println("Creating blob for textFiles/help.txt...");
        GIT.blob("textFiles/help.txt");
        
        System.out.println("Creating blob for textFiles/test.txt...");
        GIT.blob("textFiles/test.txt");
        
        // Verify blobs were created in git/objects
        verifyBlobsExist();
        
        // Verify index file was updated
        verifyIndexFile();
        
        System.out.println("✓ Blob operations test passed");
    }
    
    /**
     * Test 4: Index tree creation
     * This mirrors the indexTree() call in GIT.java main
     */
    private static void testIndexTreeCreation() throws IOException {
        System.out.println("\n--- Test 4: Index Tree Creation ---");
        
        // Create index tree (mirrors indexTree() call in main)
        System.out.println("Creating index tree...");
        GIT.indexTree();
        
        // Verify tree objects were created
        verifyTreeObjects();
        
        System.out.println("✓ Index tree creation test passed");
    }
    
    /**
     * Test 5: Verify final state
     * This verifies the complete state after all operations
     */
    private static void testFinalState() throws IOException {
        System.out.println("\n--- Test 5: Final State Verification ---");
        
        // Verify repository structure is intact
        verifyRepositoryStructure();
        
        // Verify all expected files exist
        verifyAllFilesExist();
        
        // Verify git objects directory has content
        verifyGitObjectsContent();
        
        System.out.println("✓ Final state verification passed");
    }
    
    // Helper methods for verification
    
    private static void verifyRepositoryStructure() throws IOException {
        System.out.println("Verifying repository structure...");
        
        if (!Files.exists(Path.of("git"))) {
            throw new AssertionError("git directory does not exist");
        }
        
        if (!Files.exists(Path.of("git/objects"))) {
            throw new AssertionError("git/objects directory does not exist");
        }
        
        if (!Files.exists(Path.of("git/index"))) {
            throw new AssertionError("git/index file does not exist");
        }
        
        if (!Files.exists(Path.of("git/HEAD"))) {
            throw new AssertionError("git/HEAD file does not exist");
        }
        
        System.out.println("✓ Repository structure verified");
    }
    
    private static void verifyFileContent(String filePath, String expectedContent) throws IOException {
        if (!Files.exists(Path.of(filePath))) {
            throw new AssertionError("File " + filePath + " does not exist");
        }
        
        String actualContent = Files.readString(Path.of(filePath));
        if (!actualContent.equals(expectedContent)) {
            throw new AssertionError("File " + filePath + " has incorrect content. Expected: '" + expectedContent + "', Actual: '" + actualContent + "'");
        }
        
        System.out.println("✓ File " + filePath + " has correct content");
    }
    
    private static void verifyBlobsExist() throws IOException {
        System.out.println("Verifying blobs exist in git/objects...");
        
        // Get list of files in git/objects
        List<Path> objectFiles = Files.list(Path.of("git/objects"))
                .filter(Files::isRegularFile)
                .toList();
        
        if (objectFiles.isEmpty()) {
            throw new AssertionError("No blob objects found in git/objects");
        }
        
        System.out.println("✓ Found " + objectFiles.size() + " object files in git/objects");
    }
    
    private static void verifyIndexFile() throws IOException {
        System.out.println("Verifying index file content...");
        
        if (!Files.exists(Path.of("git/index"))) {
            throw new AssertionError("Index file does not exist");
        }
        
        List<String> indexLines = Files.readAllLines(Path.of("git/index"));
        if (indexLines.isEmpty()) {
            throw new AssertionError("Index file is empty");
        }
        
        // Verify index contains entries for our files
        boolean hasFanta = indexLines.stream().anyMatch(line -> line.contains("textFiles/extra/fanta.txt"));
        boolean hasHelp = indexLines.stream().anyMatch(line -> line.contains("textFiles/help.txt"));
        boolean hasTest = indexLines.stream().anyMatch(line -> line.contains("textFiles/test.txt"));
        
        if (!hasFanta) {
            throw new AssertionError("Index file missing entry for fanta.txt");
        }
        if (!hasHelp) {
            throw new AssertionError("Index file missing entry for help.txt");
        }
        if (!hasTest) {
            throw new AssertionError("Index file missing entry for test.txt");
        }
        
        System.out.println("✓ Index file contains all expected entries");
    }
    
    private static void verifyTreeObjects() throws IOException {
        System.out.println("Verifying tree objects were created...");
        
        // Check if any tree objects exist (they should be created by indexTree())
        List<Path> objectFiles = Files.list(Path.of("git/objects"))
                .filter(Files::isRegularFile)
                .toList();
        
        if (objectFiles.isEmpty()) {
            throw new AssertionError("No objects found after indexTree()");
        }
        
        System.out.println("✓ Tree objects verification completed");
    }
    
    private static void verifyAllFilesExist() throws IOException {
        System.out.println("Verifying all expected files exist...");
        
        String[] expectedFiles = {
            "textFiles/extra/fanta.txt",
            "textFiles/help.txt", 
            "textFiles/test.txt"
        };
        
        for (String filePath : expectedFiles) {
            if (!Files.exists(Path.of(filePath))) {
                throw new AssertionError("Expected file " + filePath + " does not exist");
            }
        }
        
        System.out.println("✓ All expected files exist");
    }
    
    private static void verifyGitObjectsContent() throws IOException {
        System.out.println("Verifying git objects directory has content...");
        
        List<Path> objectFiles = Files.list(Path.of("git/objects"))
                .filter(Files::isRegularFile)
                .toList();
        
        if (objectFiles.isEmpty()) {
            throw new AssertionError("git/objects directory is empty");
        }
        
        System.out.println("✓ git/objects contains " + objectFiles.size() + " object files");
    }
    
    /**
     * Helper method to recursively remove directory and all contents
     * This mirrors the removeAll() method in GIT.java
     */
    private static void removeAll(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (var entries = Files.list(path)) {
                entries.forEach(p -> {
                    try {
                        removeAll(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        Files.delete(path);
    }
}
