package main.commands;

public interface ICommand {
    void setParam(String name, String value);
    String getParam(String name);
    void execute();
}
