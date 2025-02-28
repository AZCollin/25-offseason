package opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Constants;
import com.rowanmcalpin.nextftc.core.command.CommandManager;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import routines.BucketRoutines;
import subsystems.Claw;
import subsystems.IntakeArm;
import utils.TrajectoryBuilder;

public class TestAuto extends PedroOpMode {
    private final FConstants fConstants = new FConstants();
    private final LConstants lConstants = new LConstants();

    public TestAuto(){
        super(Claw.INSTANCE, IntakeArm.INSTANCE);
    }

    @Override
    public void onInit() {
        Constants.setConstants(fConstants.getClass(), lConstants.getClass());
        follower = new Follower(hardwareMap);
        follower.poseUpdater.resetIMU();
        follower.setStartingPose(TrajectoryBuilder.startPose);

        //IntakeArm.resetEncoder(); WE SHOULD ADD THIS - it just sets the encoder value to 0 at start.
        Claw.INSTANCE.close(); // Close claw

        OpModeData.telemetry = telemetry;
    }

    @Override
    public void onUpdate() {
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        TrajectoryBuilder.buildBucketPaths(follower);

        CommandManager.INSTANCE.scheduleCommand(
                new SequentialGroup(
                        BucketRoutines.firstSample()
                        //BucketRoutines.secondSample(),
                        //BucketRoutines.thirdSample(),
                        //BucketRoutines.fourthSample(),
                        //new Delay(1.0),
                        //BucketRoutines.park()
                )
        );
    }
}
