package frc.team5181.robot.autonomous;

import frc.team5181.robot.actuators.Winch;
import frc.team5181.robot.tasking.Task;

/**
 * Created by TylerLiu on 2017/03/17.
 */
public class WinchTurn implements Task {
    long time_start = -1;
    @Override
    public boolean nextStep() {
        if (time_start == -1) time_start = System.currentTimeMillis();
        if (System.currentTimeMillis() - time_start > 3000) {
            Winch.set(0);
            return true;
        } else {
            Winch.set(1);
            return false;
        }
    }
}
