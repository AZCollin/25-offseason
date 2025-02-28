package routines;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;

import subsystems.Claw;
import subsystems.IntakeArm;

public class MechanismRoutines {

    private MechanismRoutines() {}

    public static Command intakeOut() {
        return new SequentialGroup(
                IntakeArm.INSTANCE.pickup(),
                Claw.INSTANCE.open()
        );
    }
}
