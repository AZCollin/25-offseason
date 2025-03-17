package subsystems;

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
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.jetbrains.annotations.NotNull;

import dev.nextftc.nextcontrol.ControlSystem;
import dev.nextftc.nextcontrol.KineticState;
import dev.nextftc.nextcontrol.feedback.PIDCoefficients;
import dev.nextftc.nextcontrol.feedback.PIDElement;
import dev.nextftc.nextcontrol.feedback.PIDType;
import dev.nextftc.nextcontrol.filters.FilterElement;
import dev.nextftc.nextcontrol.interpolators.ConstantInterpolator;

@Config
public class Lift extends Subsystem {
    // BOILERPLATE
    public static final Lift INSTANCE = new Lift();

    public static PIDCoefficients coefficients = new PIDCoefficients(0.05, 0.0, 0.0);
    //public static double kF = 0.0, tolerance = 10;
    public static double targetPos = 0.0;

    private final PIDFController controller;
    public static double kP = 0.005, kI = 0.0002, kD = 0.0002, kF = 0.0002;
    public double target = 0.0, minExtension = 0.0, maxExtension = 1700;

    // USER CODE
    public MotorEx motor;

    public String name = "OuttakeSlide";

    public Lift() {
        controller = new PIDFController(kP, kI, kD, kF);
    }

    public Command toMiddle() {
        return new SequentialGroup(
                new InstantCommand(() -> { target = 500; }),
                new WaitUntil(() -> Math.abs(motor.getCurrentPosition() - target) <= 10)
        );
    }

//    public ControlSystem controlSystem = new ControlSystem(
//            new PIDElement(PIDType.POSITION, coefficients),
//            kineticState -> kF,
//            new FilterElement(value -> value, value -> value, value -> value),
//            new ConstantInterpolator(new KineticState())
//    );

//    public Command toMiddle() {
//        return new SequentialGroup(
//                new InstantCommand(() -> { controlSystem.setGoal(new KineticState(500));}),
//                new WaitUntil(() -> controlSystem.isWithinTolerance(tolerance))
//        );
//    }

//    @Override
//    @NotNull
//    public Command getDefaultCommand(){
//        return new SequentialGroup(
//                new InstantCommand(() -> { controlSystem.setGoal(new KineticState(targetPos));}),
//                new WaitUntil(() -> controlSystem.isWithinTolerance(tolerance))
//        );
//    }

    @Override
    public void initialize() {
        motor = new MotorEx(name);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry = new MultipleTelemetry(OpModeData.telemetry, FtcDashboard.getInstance().getTelemetry());

//        controlSystem.setGoal(new KineticState(targetPos));
//        motor.setPower(controlSystem.calculate());

        int currentPosition = motor.getMotor().getCurrentPosition();

        controller.setPIDF(kP, kI, kD, kF);
        controller.setF(kF);

        double power = controller.calculate(currentPosition, targetPos);

        motor.setPower(power);

        OpModeData.telemetry.addData("pos: ", motor.getCurrentPosition());
        OpModeData.telemetry.addData("target: ", targetPos);
        OpModeData.telemetry.update();

    }

    public void setTarget(double target) {
        this.target = Math.max(minExtension, Math.min(maxExtension, target));
    }

    public double getTarget() {
        return target;
    }

    public double getPosition(){
        return motor.getMotor().getCurrentPosition();
    }
}