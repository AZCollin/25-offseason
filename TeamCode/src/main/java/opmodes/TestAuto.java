package opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.rowanmcalpin.nextftc.core.command.CommandManager;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import routines.BucketRoutines;
import subsystems.IntakeClaw;
import subsystems.IntakeArm;
import subsystems.lib.Lift;
import utils.TrajectoryBuilder;

@Autonomous(name = "TestAuto")
public class TestAuto extends PedroOpMode {
    private final FConstants fConstants = new FConstants();
    private final LConstants lConstants = new LConstants();

    public TestAuto(){
        super(IntakeClaw.INSTANCE, IntakeArm.INSTANCE, Lift.INSTANCE);
    }

    @Override
    public void onInit() {
        Constants.setConstants(fConstants.getClass(), lConstants.getClass());

        follower = new Follower(hardwareMap);
        try {
            follower.poseUpdater.resetIMU();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        follower.setStartingPose(TrajectoryBuilder.startPose);

        IntakeArm.INSTANCE.resetEncoderZero();//reset encoder
        Lift.INSTANCE.resetEncoderZero();
        IntakeClaw.INSTANCE.close().invoke(); // Close claw

        OpModeData.telemetry = telemetry;
    }

    @Override
    public void onWaitForStart() {
        IntakeClaw.INSTANCE.close(); // Close claw
        telemetry.update();
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

                        BucketRoutines.testLift()
                        //BucketRoutines.secondSample(),
                        //BucketRoutines.thirdSample(),
                        //BucketRoutines.fourthSample(),
                        //new Delay(1.0),
                        //BucketRoutines.park()
                )
        );
    }
}
