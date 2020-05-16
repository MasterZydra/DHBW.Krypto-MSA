package main.commands;

public class CommandFactory {
    public static ICommand getShowAlgorithmCommand() {
        return new ShowAlgorithmCommand();
    }

    public static ICommand getEncryptMessageCommand() {
        return new EncryptMessageCommand();
    }

    public static ICommand getDecryptMessageCommand() {
        return new DecryptMessageCommand();
    }

    public static ICommand getCrackMessageCommand() {
        return new CrackMessageCommand();
    }
}
