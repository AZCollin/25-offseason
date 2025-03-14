package subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.jetbrains.annotations.NotNull;

@Config
public class Lift extends Subsystem {
    // BOILERPLATE
    public static final Lift INSTANCE = new Lift();
    private Lift() { }

    public static double kP = 0.005, kI = 0.0, kD = 0.0, kF = 0.25, setPointTolerance = 10;
    public static double target = 0;

    // USER CODE
    public MotorEx motor;

    public PIDFController controller = new PIDFController(kP, kI, kD, (pos) -> kF, setPointTolerance);

    public String name = "OuttakeSlide";

    public Command toLow() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                0.0, // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command toMiddle() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                500.0, // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    public Command toHigh() {
        return new RunToPosition(motor, // MOTOR TO MOVE
                1200.0, // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }

    @Override
    @NotNull
    public Command getDefaultCommand(){
        return new HoldPosition(motor, controller,this);
    }

    @Override
    public void initialize() {
        motor = new MotorEx(name);


    }

    @Override
    public void periodic(){
        OpModeData.telemetry = new MultipleTelemetry(OpModeData.telemetry, FtcDashboard.getInstance().getTelemetry());

        motor.setPower(controller.calculate(motor.getCurrentPosition()));

        controller.setKD(kD);
        controller.setKI(kI);
        controller.setKP(kP);
        controller.setSetPointTolerance(setPointTolerance);
        controller.setTarget(target);

        OpModeData.telemetry.addData("pos: ", motor.getCurrentPosition());
        OpModeData.telemetry.addData("target: ", target);
        OpModeData.telemetry.update();

    }
}