package frc.team5181.robot.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5181.robot.tasking.ParallelTask;
import frc.team5181.robot.tasking.Task;

/**
 * Created by TylerLiu on 2017/03/04.
 */
public class AutonChooser {

    public static SendableChooser<String> chooser;

    private static void chooserAdd(String commandName){
        chooser.addObject(commandName,commandName);
    }
    
    public static void chooserInit(){
        chooser = new SendableChooser<>();
        chooserAdd("Position 1");
        chooserAdd("Position 2");
        chooserAdd("Position 3");

        chooserAdd("Position 1 Path");
        chooserAdd("Position 2 Path");
        chooserAdd("Position 3 Path");

        chooserAdd("Position 1 USG");
        chooserAdd("Position 3 USG");
        SmartDashboard.putData("Auton Chooser", chooser);
    }
    
    public static Task getAutonCommand(){
        DriverStation.reportWarning(chooser.getSelected(), false);
        switch (chooser.getSelected()) {
            case "Position 1":
                return new ParallelTask(new HookGear(1), new WinchTurn());
            case "Position 2":
                return new ParallelTask(new HookGear(2), new WinchTurn());
            case "Position 3":
                return new ParallelTask(new HookGear(3), new WinchTurn());
            case "Position 1 Path":
                return new ParallelTask(new HookGearPath(1), new WinchTurn());
            case "Position 2 Path":
                return new ParallelTask(new HookGearPath(2), new WinchTurn());
            case "Position 3 Path":
                return new ParallelTask(new HookGearPath(3), new WinchTurn());
            case "Position 1 USG":
                return new ParallelTask(new HookGearGEUS(1), new WinchTurn());
            case "Position 3 USG":
                return new ParallelTask(new HookGearGEUS(3), new WinchTurn());
            default:
                throw new RuntimeException();
        }
    }
}