package routines;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.pedro.FollowPath;

import subsystems.IntakeClaw;
import subsystems.lib.OuttakeSlideLib;
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
                OuttakeSlideLib.INSTANCE.toMiddle(),
                IntakeClaw.INSTANCE.open(),
                OuttakeSlideLib.INSTANCE.toHigh()
        );
    }
}
