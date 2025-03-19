package subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

@Config
public class TestMotorUsingNextFTCController extends Subsystem {

    public static final TestMotorUsingNextFTCController INSTANCE = new TestMotorUsingNextFTCController();

    public static double kP = 0.0; //0.01
    public static double kI = 0.0;
    public static double kD = 0.0; //0.00015
    public static double kF = 0.0;

    public String name = "Clipper";

    private MotorEx motor = new MotorEx(name);

    private final PIDFController controller = new PIDFController(kP, kI, kD, (pos) -> kF);

    public Command getToZero() {
        return new RunToPosition(motor, 0.0, controller, this);
    }

    public Command getTo1000() {
        return new RunToPosition(motor, 1000.0, controller, this);
    }

    public static boolean zero = false;

    @Override
    public void initialize() {
        motor = new MotorEx(name);
    }

    @Override
    public void periodic() {
        controller.setKP(kP);
        controller.setKI(kI);
        controller.setKD(kD);

        if (zero) {
            getTo1000().invoke();
        } else {
            getToZero().invoke();
        }

        OpModeData.telemetry.addData("Position", motor.getCurrentPosition());
        OpModeData.telemetry.addData("Target", controller.getTarget());
    }
}