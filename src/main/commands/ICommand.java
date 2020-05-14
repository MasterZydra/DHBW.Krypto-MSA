package main.commands;

public interface ICommand {
    void setParam(String name, String value);
    void execute();
}
