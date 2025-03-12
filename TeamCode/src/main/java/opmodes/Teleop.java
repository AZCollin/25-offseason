package opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand;
import com.rowanmcalpin.nextftc.core.command.utility.conditionals.PassiveConditionalCommand;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import subsystems.Belt;
import subsystems.Clipper;
import subsystems.IntakeClaw;
import subsystems.IntakeArm;
import subsystems.IntakeSlide;
import subsystems.OuttakeClaw;
import subsystems.OuttakeSlide;

@TeleOp(name = "ClipBot")
public class Teleop extends PedroOpMode {
    public Teleop() {
        super(IntakeClaw.INSTANCE,IntakeSlide.INSTANCE,IntakeArm.INSTANCE, OuttakeSlide.INSTANCE, OuttakeClaw.INSTANCE, Belt.INSTANCE, Clipper.INSTANCE);
    }

    public MecanumDriverControlled driver;
    public MotorEx frontLeft;
    public MotorEx backLeft;
    public MotorEx frontRight;
    public MotorEx backRight;
    public MotorEx[] driveMotors;
    public String lastSequence;
    public int specimenSequenceCount = 0;

    //public int sampleSequenceCount;
    @Override
    public void onInit() {
        OpModeData.telemetry = telemetry;
        mecanumDriveInit();
        telemetry.update();
    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
        driver = new MecanumDriverControlled(driveMotors, gamepadManager.getGamepad1());
        driver.invoke();
        registerControls();


        IntakeClaw.INSTANCE.close(); //Initially close it
    }

    @Override
    public void onUpdate() {
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

        driveMotors = new MotorEx[]{frontLeft, frontRight, backLeft, backRight};

        for (MotorEx driveMotor : driveMotors) {
            driveMotor.getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        IntakeArm.INSTANCE.resetEncoderZero();
        OuttakeSlide.INSTANCE.resetEncoderZero();

    }

    private void registerControls() {
        //gamepadManager.getGamepad1().getRightBumper().setPressedCommand(this::specimenNextStep);
        //gamepadManager.getGamepad1().getLeftBumper().setPressedCommand(this::specimenPreviousStep);

        gamepadManager.getGamepad1().getA().setPressedCommand(this::toggleSpeed);

        gamepadManager.getGamepad2().getX().setReleasedCommand(IntakeClaw.INSTANCE::toggle); // When pressed it triggers it so say open
        gamepadManager.getGamepad2().getX().setPressedCommand(IntakeClaw.INSTANCE::toggle);  // Then when released it should close it

        gamepadManager.getGamepad2().getB().setPressedCommand(IntakeSlide.INSTANCE::toggle);
        gamepadManager.getGamepad2().getY().setPressedCommand(IntakeArm.INSTANCE::pickup);
        gamepadManager.getGamepad2().getA().setPressedCommand(IntakeArm.INSTANCE::transfer);
        gamepadManager.getGamepad2().getDpadDown().setPressedCommand(IntakeArm.INSTANCE::clip);
    }

    public boolean slowMode = true;

    public Command toggleSpeed() {
        return new SequentialGroup(
                new InstantCommand(() -> {
                    slowMode = !slowMode;
                }),
                new PassiveConditionalCommand(
                        () -> slowMode,
                        () -> new InstantCommand(() -> {
                            driver.setScalar(0.2);
                        }),
                        () -> new InstantCommand(() -> {
                            driver.setScalar(0.8);
                        })
                )
        );
    }

    public Command specimenNextStep() {
        return new InstantCommand(
                () -> {
                    specimenSequenceCount++;
                    nextSpecimenSequence();
                }
        );
    }

    public Command specimenPreviousStep() {
        return new InstantCommand(
                this::previousSpecimenSequence
        );
    }

    public void nextSpecimenSequence() {
        switch (specimenSequenceCount) {
            case 1:
                IntakeArm.INSTANCE.PrePickup();
                break;
            case 2:
                IntakeClaw.INSTANCE.open();
                break;
            case 3:
                IntakeArm.INSTANCE.pickup();
                break;
            case 4:
                IntakeClaw.INSTANCE.close();
                break;
            case 5:
                //IntakeArm.INSTANCE.retract();
                break;
            case 6:
                IntakeSlide.INSTANCE.in();
                specimenSequenceCount = 1;
                break;
        }
    }

    public void previousSpecimenSequence() {
        if (specimenSequenceCount > 1) {
            specimenSequenceCount--; // Move back one step
        }

        switch (specimenSequenceCount) {
            case 1:
                IntakeArm.INSTANCE.transfer();
                break;
            case 2:
                IntakeClaw.INSTANCE.close();
                break;
            case 3:
                IntakeArm.INSTANCE.PrePickup();
                break;
            case 4:
                IntakeClaw.INSTANCE.open();
                break;
            case 5:
                //IntakeArm.INSTANCE.retract();
                break;
            case 6:
        }
    }







}

