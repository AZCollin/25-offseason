package subsystems.lib;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand;
import com.rowanmcalpin.nextftc.core.command.utility.delays.WaitUntil;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;

@Config
public class BeltLib extends Subsystem {
    // BOILERPLATE
    public static final BeltLib INSTANCE = new BeltLib();
    private final PIDController controller;
    public static double kP = 0.0, kI = 0.0, kD = 0.0, kF = 0.0;
    public static double target = 0.0, threshold = 5, minExtension = 0.0, maxExtension = 200;

    // USER CODE
    public MotorEx motor;

    public String name = "Belt";

    public BeltLib() {
        controller = new PIDController(kP, kI, kD);
    }

    public Command toHigh() {
        return new SequentialGroup(
                new InstantCommand(() -> { target = 1500; }),
                new WaitUntil(() -> Math.abs(motor.getCurrentPosition() - target) <= threshold)
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

        double currentPosition = motor.getCurrentPosition();

        controller.setPID(kP, kI, kD);

        double power = controller.calculate(currentPosition, target);
        motor.setPower(power);

        OpModeData.telemetry.addData("BeltLib pos: ", motor.getCurrentPosition());
        OpModeData.telemetry.addData("BeltLib target: ", target);
        OpModeData.telemetry.update();
    }

    public void resetEncoderZero() {
        motor.resetEncoder();
        target = 0;


    }

    public void setTarget(double target) {
        this.target = Math.max(minExtension, Math.min(maxExtension, target));
    }

    public double getTarget() {
        return target;
    }

    public double getPosition(){
        return motor.getCurrentPosition();
    }
}