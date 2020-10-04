package commands;

public class CommandFactory {
    private CommandFactory() {
    }

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

    public static ICommand getRegisterParticipantCommand() {
        return new RegisterParticipantCommand();
    }

    public static ICommand getSendMessageP2PCommand() {
        return new SendMessageP2PCommand();
    }

    public static ICommand getIntrudeChannelCommand() {
        return new IntrudeChannelCommand();
    }

    public static ICommand getDropChannelCommand() {
        return new DropChannelCommand();
    }

    public static ICommand getShowChannelCommand() {
        return new ShowChannelCommand();
    }

    public static ICommand getCreateChannelCommand() {
        return new CreateChannelCommand() ;
    }

}
