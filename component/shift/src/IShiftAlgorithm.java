import java.io.File;

public interface IShiftAlgorithm {
    String encrypt(String plainMessage, File keyfile);
    String decrypt(String encryptedMessage, File keyfile);
}
