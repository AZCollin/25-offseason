package routines;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.core.units.TimeSpan;

import subsystems.Claw;
import subsystems.IntakeArm;
import subsystems.IntakeSlide;

public class MechanismRoutines {

    private MechanismRoutines() {}

    public static Command intakeOut() {
        return new SequentialGroup(
                IntakeArm.INSTANCE.pickup(),
                Claw.INSTANCE.open(),
                //new Delay(TimeSpan.fromMs(200)),
                IntakeSlide.INSTANCE.out()
        );
    }
}
