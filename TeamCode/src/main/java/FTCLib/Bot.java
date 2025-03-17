package FTCLib;


import com.arcrobotics.ftclib.command.Robot;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Bot extends Robot {
    private final IMU imu;
    public final Telemetry telemetry;
    public final HardwareMap hardwareMap;


    public Bot(Telemetry telemetry, HardwareMap hardwareMap, boolean enableDrive) {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP)));

        if (enableDrive) {
            //drivatetrain = new MecanumDrivetrain(this)
        }
    }
}
