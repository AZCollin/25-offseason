package subsystems.lib;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand;
import com.rowanmcalpin.nextftc.core.command.utility.delays.WaitUntil;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;

@Config
public class IntakeArmFix extends Subsystem {
    // BOILERPLATE
    public static final IntakeArmFix INSTANCE = new IntakeArmFix();
    private final PIDFController controller;
    public static double kP = 0.0, kI = 0.0, kD = 0.0, kF = 0.0;
    public static double target = 0.0, minAngle = 0.0, maxAngle = 260, adjustment = 90, threshold = 5;
    public double ticksInDegrees = 537.7 / 360.0;

    // USER CODE
    public MotorEx motor;

    public String name = "IntakeArm";

    public IntakeArmFix() {
        controller = new PIDFController(kP, kI, kD, kF);
    }

    public Command toHigh() {
        return new SequentialGroup(
                new InstantCommand(() -> { target = 1500; }),
                new WaitUntil(() -> Math.abs(motor.getCurrentPosition() - target) <= threshold * ticksInDegrees)
        );
    }

    public Command toMiddle() {
        return new SequentialGroup(
                new InstantCommand(() -> { target = 500; }),
                new WaitUntil(() -> Math.abs(motor.getCurrentPosition() - target) <= threshold)
        );
    }

    public Command toLow() {
        return new SequentialGroup(
                new InstantCommand(() -> { target = 5; }),
                new WaitUntil(() -> Math.abs(motor.getCurrentPosition() - target) <= threshold)
        );
    }

    @Override
    public void initialize() {
        motor = new MotorEx(name);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry = new MultipleTelemetry(OpModeData.telemetry, FtcDashboard.getInstance().getTelemetry());

        double pivotAngle = motor.getCurrentPosition() / ticksInDegrees; // Get the current position of the arm in degrees

        controller.setF(kF * (Math.cos(pivotAngle - Math.toRadians(adjustment)))); //Not sure what 90 should be

        double power = controller.calculate(Math.toDegrees(motor.getCurrentPosition()), target + adjustment);
        motor.setPower(power);

        OpModeData.telemetry.addData("pos: ", motor.getCurrentPosition());
        OpModeData.telemetry.addData("target: ", target);
        OpModeData.telemetry.update();

    }

    public void resetEncoderZero() {
        motor.resetEncoder();
        motor.setCurrentPosition(0);
    }

    public void setTarget(double target) {
        this.target = Math.max(minAngle, Math.min(maxAngle, target));
    }

    public double getTarget() {
        return target;
    }

    public double getPosition(){
        return motor.getCurrentPosition();
    }
}