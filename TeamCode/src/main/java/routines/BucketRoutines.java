package routines;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.core.units.TimeSpan;
import com.rowanmcalpin.nextftc.pedro.FollowPath;

import subsystems.IntakeClaw;
import subsystems.lib.Lift;
import utils.TrajectoryBuilder;

public class BucketRoutines {

    private BucketRoutines() {}

    public static Command firstSample() {
        return new ParallelGroup(
                new FollowPath(TrajectoryBuilder.startToBucket, true, 0.5),
                MechanismRoutines.intakeOut()
        );
    }

    public static Command testLift() {
        return new SequentialGroup(
                Lift.INSTANCE.toHigh(),
                new Delay(TimeSpan.fromMs(500)),
                Lift.INSTANCE.toMiddle(),
                new Delay(TimeSpan.fromMs(2000)),
                IntakeClaw.INSTANCE.open(),
                new Delay(TimeSpan.fromMs(500)),
                Lift.INSTANCE.toLow()
        );
    }
}
