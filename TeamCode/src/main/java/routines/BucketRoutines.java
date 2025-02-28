package routines;

import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;

import subsystems.Claw;
import subsystems.IntakeArm;
import utils.FollowPathWithSpeed;
import utils.TrajectoryBuilder;

public class BucketRoutines {

    private BucketRoutines() {}

    public static Command firstSample() {
        return new ParallelGroup(
                new FollowPathWithSpeed(TrajectoryBuilder.startToBucket, true, 0.5),
                MechanismRoutines.intakeOut()
        );
    }
}
