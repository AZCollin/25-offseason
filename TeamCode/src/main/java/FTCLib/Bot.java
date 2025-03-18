package FTCLib;


import com.arcrobotics.ftclib.command.Robot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import FTCLib.subsystems.IntakeArmNew;
import FTCLib.subsystems.IntakeClawNew;
import FTCLib.subsystems.IntakeSlideNew;
import FTCLib.subsystems.MecanumDrivetrain;
import FTCLib.subsystems.OuttakeSlideNew;

public class Bot extends Robot {
    private final IMU imu;
    public final Telemetry telem;
    public final HardwareMap hardwareMap;

    private MecanumDrivetrain drivetrain;
    private IntakeClawNew intakeClaw;
    private IntakeSlideNew intakeSlide;
    private IntakeArmNew intakeArm;
    private OuttakeSlideNew outtakeSlide;


    public Bot(Telemetry telem, HardwareMap hardwareMap, boolean enableDrive) {
        this.telem = telem;
        this.hardwareMap = hardwareMap;

        imu = hardwareMap.get(IMU.class, "imu");
//        imu.initialize(new IMU.Parameters(
//                new RevHubOrientationOnRobot(
//                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
//                        RevHubOrientationOnRobot.UsbFacingDirection.UP)));

        if (enableDrive) {
            drivetrain = new MecanumDrivetrain(this);
        }

        intakeClaw = new IntakeClawNew(this);
        intakeSlide = new IntakeSlideNew(this);
        intakeArm = new IntakeArmNew(this);
        outtakeSlide = new OuttakeSlideNew(this);
    }

    public MecanumDrivetrain getDrivetrain() { return drivetrain; }

    public IntakeClawNew getIntakeClaw() { return intakeClaw; }
    public IntakeSlideNew getIntakeSlide() { return intakeSlide; }
    public IntakeArmNew getIntakeArm() { return intakeArm; }
    public OuttakeSlideNew getOuttakeSlide() { return outtakeSlide; }
}
