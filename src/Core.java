
import datastructures.Tree;
import datastructures.TreeTest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Core {
    public static void main(String[] args) {
        try {
            new TreeTest();
        } catch (IOException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
