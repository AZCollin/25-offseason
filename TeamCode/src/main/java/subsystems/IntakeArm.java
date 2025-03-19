package subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

@Config
public class IntakeArm extends Subsystem {

    public static final IntakeArm INSTANCE = new IntakeArm();
    public static double kP = 0.002; //0.01
    public static double kI = 0.0;
    public static double kD = 0.0005; //0.00015
    public static double kF = 0.004;
    public static double target = 0.0;
    public static double threshold = 15;

    public String name = "IntakeArm";
    private MotorEx motor;
    private final PIDFController controller = new PIDFController(kP, kI, kD, (pos) -> kF, threshold);

    public double pickupPosition = 400;
    public double transferPosition = -20;
    public double clip = -300;

    public Command pickup() { return new RunToPosition(motor, pickupPosition, controller, this); }

    public Command transfer() { return new RunToPosition(motor, transferPosition, controller, this); }
    public Command clip() {
        return new RunToPosition(motor, clip, controller, this);
    }

    @Override
    public void initialize() {
        motor = new MotorEx(name);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public Command getDefaultCommand() { return new HoldPosition(motor, controller, this); }

    @Override
    public void periodic() {
        controller.setKP(kP);
        controller.setKI(kI);
        controller.setKD(kD);
        controller.setSetPointTolerance(threshold);

        OpModeData.telemetry.addData("IntakeArm Position", motor.getCurrentPosition());
        OpModeData.telemetry.addData("IntakeArm Target", controller.getTarget());
    }

    public void resetEncoder() {
        motor.resetEncoder();
    }
}