package opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand;
import com.rowanmcalpin.nextftc.core.command.utility.conditionals.PassiveConditionalCommand;
import com.rowanmcalpin.nextftc.core.command.utility.statemachine.AdvancingCommand;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import subsystems.Belt;
import subsystems.Clipper;
import subsystems.IntakeArm;
import subsystems.IntakeClaw;
import subsystems.IntakeSlide;
import subsystems.OuttakeClaw;
import subsystems.OuttakeSlide;

@TeleOp(name = "ClipBot")
public class Teleop extends PedroOpMode {
    public Teleop() {
        super(IntakeClaw.INSTANCE,
                IntakeSlide.INSTANCE,
                IntakeArm.INSTANCE,
                OuttakeSlide.INSTANCE,
                OuttakeClaw.INSTANCE,
                Belt.INSTANCE,
                Clipper.INSTANCE);
    }

    public MecanumDriverControlled driver;
    public MotorEx frontLeft;
    public MotorEx backLeft;
    public MotorEx frontRight;
    public MotorEx backRight;
    public MotorEx[] driveMotors;
    public String lastSequence;
    public int specimenSequenceCount = 0;
    private double lastLoopTimestamp = 0.0;

    @Override
    public void onInit() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        OpModeData.telemetry = telemetry;

        mecanumDriveInit();
        telemetry.update();
    }

    @Override
    public void onWaitForStart() {}

    @Override
    public void onStartButtonPressed() {
        driver = new MecanumDriverControlled(driveMotors, gamepadManager.getGamepad1());
        driver.invoke();
        registerControls();


        IntakeClaw.INSTANCE.close();
        OuttakeClaw.INSTANCE.close();
    }

    @Override
    public void onUpdate() {
        if (lastLoopTimestamp == 0.0) {
            lastLoopTimestamp = System.nanoTime() / 1E9;
        }

        OpModeData.telemetry.addData("Loop time", (System.nanoTime() / 1E9) - lastLoopTimestamp);
        lastLoopTimestamp = System.nanoTime() / 1E9;

        OpModeData.telemetry.update();
    }

    @Override
    public void onStop() {}

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

        IntakeArm.INSTANCE.resetEncoder();
        OuttakeSlide.INSTANCE.resetEncoder();
        Belt.INSTANCE.resetEncoder();
        Clipper.INSTANCE.resetEncoder();
    }

    private void registerControls() {
        //gamepadManager.getGamepad1().getRightBumper().setPressedCommand(this::specimenNextStep);
        //gamepadManager.getGamepad1().getLeftBumper().setPressedCommand(this::specimenPreviousStep);
        gamepadManager.getGamepad1().getA().setPressedCommand(IntakeArm.INSTANCE::IntakeArmUp);
        gamepadManager.getGamepad1().getB().setPressedCommand(IntakeArm.INSTANCE::IntakeArmDown);
        gamepadManager.getGamepad1().getA().setPressedCommand(this::toggleSpeed);
        gamepadManager.getGamepad1().getDpadUp().setPressedCommand(Clipper.INSTANCE::clipUp);
        gamepadManager.getGamepad1().getDpadDown().setPressedCommand(Clipper.INSTANCE::clipDown);
        gamepadManager.getGamepad1().getDpadLeft().setPressedCommand(Belt.INSTANCE::down);
        gamepadManager.getGamepad1().getDpadRight().setPressedCommand(Belt.INSTANCE::up);

        gamepadManager.getGamepad2().getX().setReleasedCommand(IntakeClaw.INSTANCE::toggle); // When pressed it triggers it so say open
        gamepadManager.getGamepad2().getX().setPressedCommand(IntakeClaw.INSTANCE::toggle);  // Then when released it should close it
        gamepadManager.getGamepad2().getDpadUp().setPressedCommand(IntakeArm.INSTANCE::IntakeArmUp);
        gamepadManager.getGamepad2().getDpadDown().setPressedCommand(IntakeArm.INSTANCE::IntakeArmDown);
        gamepadManager.getGamepad2().getRightBumper().setPressedCommand(IntakeSlide.INSTANCE::toggle);

        gamepadManager.getGamepad2().getLeftBumper().setReleasedCommand(OuttakeClaw.INSTANCE::toggle); // When pressed it triggers it so say open
        gamepadManager.getGamepad2().getLeftBumper().setPressedCommand(OuttakeClaw.INSTANCE::toggle);  // Then when released it should close it

        gamepadManager.getGamepad2().getA().setPressedCommand(IntakeArm.INSTANCE::clip);
        gamepadManager.getGamepad2().getB().setPressedCommand(IntakeArm.INSTANCE::transfer);
        gamepadManager.getGamepad2().getY().setPressedCommand(IntakeArm.INSTANCE::pickup);

        gamepadManager.getGamepad2().getDpadUp().setPressedCommand(OuttakeSlide.INSTANCE::highChamber);
        gamepadManager.getGamepad2().getDpadDown().setPressedCommand(OuttakeSlide.INSTANCE::transfer);
        gamepadManager.getGamepad2().getDpadLeft().setPressedCommand(OuttakeSlide.INSTANCE::highBasket);
        gamepadManager.getGamepad2().getDpadRight().setPressedCommand(OuttakeSlide.INSTANCE::bypass);



        //gamepadManager.getGamepad2().getRightBumper().setPressedCommand(this::forwardCommand);
        //gamepadManager.getGamepad2().getLeftBumper().setPressedCommand(this::backCommand);
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

//    private int step = 0; // Tracks current step
//
//    public Command forwardCommand() {
//        return new AdvancingCommand()
//                .add(OuttakeSlide.INSTANCE.highBasket())
//                .add(OuttakeSlide.INSTANCE.transfer());
//    }
}

