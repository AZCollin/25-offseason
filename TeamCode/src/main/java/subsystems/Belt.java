package subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

@Config
public class Belt extends Subsystem {

    public static final Belt INSTANCE = new Belt();

    public static double kP = 0.01;
    public static double kI = 0.0;
    public static double kD = 0.0015;
    public static double kF = 0.0;
    public static double target = 0.0;
    public static double threshold = 10;

    public String name = "Belt";

    private MotorEx motor;

    private final PIDFController controller = new PIDFController(kP, kI, kD, (pos) -> kF, threshold);

    public Command getToZero() {
        return new RunToPosition(motor, 0.0, controller, this);
    }

    public Command getTo1000() {
        return new RunToPosition(motor, 500.0, controller, this);
    }

    @Override
    public void initialize() {
        motor = new MotorEx(name);
    }

    @Override
    public void periodic() {
        controller.setKP(kP);
        controller.setKI(kI);
        controller.setKD(kD);


        OpModeData.telemetry.addData("Belt Position", motor.getCurrentPosition());
        OpModeData.telemetry.addData("Belt Target", controller.getTarget());
    }

    public void resetEncoder() {
        motor.resetEncoder();
    }
}