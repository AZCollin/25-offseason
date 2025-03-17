package FTCLib;


import com.arcrobotics.ftclib.command.Robot;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import FTCLib.subsystems.IntakeArm;
import FTCLib.subsystems.IntakeClaw;
import FTCLib.subsystems.IntakeSlide;
import FTCLib.subsystems.MecanumDrivetrain;
import FTCLib.subsystems.OuttakeSlide;

public class Bot extends Robot {
    private final IMU imu;
    public final Telemetry telem;
    public final HardwareMap hardwareMap;

    private MecanumDrivetrain drivetrain;
    private IntakeClaw intakeClaw;
    private IntakeSlide intakeSlide;
    private IntakeArm intakeArm;
    private OuttakeSlide outtakeSlide;


    public Bot(Telemetry telem, HardwareMap hardwareMap, boolean enableDrive) {
        this.telem = telem;
        this.hardwareMap = hardwareMap;

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP)));

        if (enableDrive) {
            drivetrain = new MecanumDrivetrain(this);
        }

        intakeClaw = new IntakeClaw(this);
        intakeSlide = new IntakeSlide(this);
        intakeArm = new IntakeArm(this);
        outtakeSlide = new OuttakeSlide(this);
    }

    public MecanumDrivetrain getDrivetrain() { return drivetrain; }

    public IntakeClaw getIntakeClaw() { return intakeClaw; }
    public IntakeSlide getIntakeSlide() { return intakeSlide; }
    public IntakeArm getIntakeArm() { return intakeArm; }
    public OuttakeSlide getOuttakeSlide() { return outtakeSlide; }
}
