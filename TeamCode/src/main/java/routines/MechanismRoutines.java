package routines;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.WaitUntil;

import subsystems.IntakeClaw;
import subsystems.IntakeArm;
import subsystems.IntakeSlide;

public class MechanismRoutines {

    private MechanismRoutines() {}

    public static Command intakeOut() {
        return new SequentialGroup(
                IntakeArm.INSTANCE.pickup(),
                IntakeClaw.INSTANCE.open(),
                //new Delay(TimeSpan.fromMs(200)),
                IntakeSlide.INSTANCE.out()
        );
    }
}
