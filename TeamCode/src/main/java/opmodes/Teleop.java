package opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand;
import com.rowanmcalpin.nextftc.core.command.utility.conditionals.PassiveConditionalCommand;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.pedro.DriverControlled;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import java.sql.Driver;
import java.util.Objects;

import subsystems.Claw;
import subsystems.IntakeSlide;

@TeleOp(name = "ClipBot")
public class Teleop extends PedroOpMode {
    public Teleop(){
        super(Claw.INSTANCE);
    }
    public MecanumDriverControlled driver;
    public MotorEx frontLeft;
    public MotorEx backLeft;
    public MotorEx frontRight;
    public MotorEx backRight;
    public MotorEx[] driveMotors;
    public String lastSequence;
    public int specimenSequenceCount;
    //public int sampleSequenceCount;
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
    public boolean slowMode = true;
    public Command toggleSpeed() {
        return new SequentialGroup(
                new InstantCommand(() -> {
                    slowMode = !slowMode;
                }),
                new PassiveConditionalCommand(
                        () -> slowMode,
                        () -> new InstantCommand(() -> { driver.setScalar(0.2); }),
                        () -> new InstantCommand(() -> { driver.setScalar(0.8); })
                )
        );
    }
    public void sequenceHandler(String sequenceInput){
        if(!(Objects.equals(lastSequence, sequenceInput))){
            specimenSequenceCount = 1;
        }
        switch (sequenceInput){
            default:
                //empty
                break;
            case "sample":
                //sample
                break;
            case "specimen":
                specimenSequence();
                break;
            case "ascend":
                //ascend
                break;
        }
        specimenSequenceCount += 1;
        lastSequence = sequenceInput;
    }
    public void specimenSequence(){
        switch (specimenSequenceCount){
            case 1:
                IntakeSlide.INSTANCE.out();
                break;
            case 2:
                Claw.INSTANCE.open();
                break;

        }
    }


































}
