import java.io.File;

public interface IRsaAlgorithm {
    String encrypt(String plainMessage, File publicKeyfile);
    String decrypt(String encryptedMessage, File privateKeyfile);
}
