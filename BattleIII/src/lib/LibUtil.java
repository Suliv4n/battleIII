package lib;

/**
 * Classe trouvée sur internet.
 * 
 * Permet de mettre à jour les librairy path
 */


import java.io.File;
import java.lang.reflect.Field;

public class LibUtil {
    private final static String JAVA_LIBRARY_PATH = "java.library.path";
 
    public static void addToJavaLibraryPath(String dir) {
        addToJavaLibraryPath(new File(dir));
    }
 
    public static void addToJavaLibraryPath(File dir) {
 
        if (!dir.isDirectory()) 
        {
            throw new IllegalArgumentException(dir + " is not a directory. La preuve "+dir+".isDirectory renvoie : "+dir.isDirectory());
        }
        String javaLibraryPath = System.getProperty(JAVA_LIBRARY_PATH);
        System.setProperty(JAVA_LIBRARY_PATH, javaLibraryPath + File.pathSeparatorChar + dir.getAbsolutePath());
 
        resetJavaLibraryPath();
    }
 
    private static void resetJavaLibraryPath() {
        synchronized (Runtime.getRuntime()) {
            try {
                Field field = ClassLoader.class.getDeclaredField("usr_paths");
                field.setAccessible(true);
                field.set(null, null);
 
                field = ClassLoader.class.getDeclaredField("sys_paths");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}