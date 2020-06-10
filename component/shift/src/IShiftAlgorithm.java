import java.io.File;
import java.io.FileNotFoundException;

public interface IShiftAlgorithm {
    String encrypt(String plainMessage, File keyfile) throws FileNotFoundException;
    String decrypt(String encryptedMessage, File keyfile) throws FileNotFoundException;
}
