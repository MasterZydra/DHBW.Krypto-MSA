package main.commands;

public class CommandFactory {
    public static ICommand getShowAlgorithmCommand() {
        return new ShowAlgorithmCommand();
    }
}
