package opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import java.sql.Driver;

import subsystems.Claw;

@TeleOp(name = "ClipBot")
public class Teleop extends PedroOpMode {
    public Teleop(){
        super(Claw.INSTANCE);
    }
    public Command driver;
    public MotorEx frontLeft;
    public MotorEx backLeft;
    public MotorEx frontRight;
    public MotorEx backRight;
    public MotorEx[] driveMotors;
    @Override
    public void onInit () {
        OpModeData.telemetry = telemetry;
        mecanumDriveInit();
        telemetry.update();
    }
    @Override
    public void onWaitForStart () {

    }
    @Override
    public void onStartButtonPressed () {
        driver = new MecanumDriverControlled(driveMotors,gamepadManager.getGamepad1());
        driver.invoke();
        registerControls();
    }
    @Override
    public void onUpdate () {
        telemetry.update();
    }
    @Override
    public void onStop() {

    }
    public void mecanumDriveInit() {
        frontLeft = new MotorEx("frontLeft");
        frontRight = new MotorEx("frontRight");
        backLeft = new MotorEx("backLeft");
        backRight = new MotorEx("backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        driveMotors = new MotorEx[] {frontLeft, frontRight, backLeft, backRight};

    }
    private void registerControls() {
        gamepadManager.getGamepad1().getRightBumper().setPressedCommand(Claw.INSTANCE::toggle);
    }



}
