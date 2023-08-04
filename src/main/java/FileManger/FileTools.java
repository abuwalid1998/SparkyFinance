package FileManger;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileTools {




    public  String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex >= 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        } else {
            // File name has no extension
            return "";
        }
    }

}
