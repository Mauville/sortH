package sample;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

class ImageHandler {

    public static List<File> getImages(File dir) throws IOException {
        String[] extensions = new String[] { "jpg", "png", "gif" };
		System.out.println("Getting all images " + dir.getCanonicalPath()
				+ " including those in subdirectories");
        return (List<File>) FileUtils.listFiles(dir, extensions,false);
    }
    public static List<File> getImages(String dir) throws IOException {
        String[] extensions = new String[] { "jpg", "png", "gif" };
        System.out.println("Getting all images " + dir
                + " including those in subdirectories");
        return (List<File>) FileUtils.listFiles(new File (dir), extensions,false);
    }

}
