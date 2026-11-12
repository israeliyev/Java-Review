package org.example.patterns;

import java.util.ArrayList;
import java.util.List;

//receiver
 class Light {
    private boolean light;
    private static final Light lightObject = new Light();

    private Light() {

    }

    public static Light getInstance() {
        return lightObject;
    }

    void turnOn() {
        this.light = true;
        System.out.println("Light is turned on");
    }

    void turnOff() {
        this.light = false;
        System.out.println("Light is turned off");
    }

    boolean getLight() {
        return light;
    }
}

//command
interface Command {
    void execute();
}

//exact command
class TurnOnLightCommand implements Command {

    private final Light light;

    TurnOnLightCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();

    }
}

//exact command
class TurnOffLightCommand implements Command {

    private final Light light;

    TurnOffLightCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOff();

    }
}

class UndoableCommand {

    private final Light light;
    private boolean oldLightValue;
    private boolean newLightValue;

    UndoableCommand(Light light) {
        this.light = light;
    }

    void doOperation() {
        if (light.getLight()) {
            this.oldLightValue = light.getLight();
            light.turnOff();
            this.newLightValue = light.getLight();
        } else {
            this.oldLightValue = light.getLight();
            light.turnOn();
            this.newLightValue = light.getLight();
        }
    }

    void unDoOperation() {
        if (newLightValue) {
            this.oldLightValue = newLightValue;
            light.turnOff();
            this.newLightValue = light.getLight();
        } else {
            this.oldLightValue = newLightValue;
            light.turnOn();
            this.newLightValue = light.getLight();
        }
    }

}

//invoker
class RemoteController {
    private final Command command;
    private static final List<Command> commandHistory = new ArrayList<>();

    RemoteController(Command command) {
        this.command = command;
    }

    public void toggleLight() {
        command.execute();
        commandHistory.add(command);
    }

    public List<Command> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }

}

//client
public class CommandPatternPureJava {

    public static void main(String[] args) {
        Light light = Light.getInstance();

        TurnOnLightCommand turnOnLightCommand = new TurnOnLightCommand(light);
        TurnOffLightCommand turnOffLightCommand = new TurnOffLightCommand(light);

        RemoteController remoteController = new RemoteController(turnOnLightCommand);

        remoteController.toggleLight();

        RemoteController remoteController2 = new RemoteController(turnOffLightCommand);
        remoteController2.toggleLight();

        remoteController.getCommandHistory();

        UndoableCommand undoableCommand = new UndoableCommand(light);
        undoableCommand.doOperation();
        undoableCommand.unDoOperation();
    }


}
