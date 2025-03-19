package subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

@Config
public class OuttakeSlide extends Subsystem {

    public static final OuttakeSlide INSTANCE = new OuttakeSlide();

    public static double kP = 0.01;
    public static double kI = 0.0;
    public static double kD = 0.00015;
    public static double kF = 0.1;
    public static double target = 0.0;
    public static double threshold = 10;

    public String name = "OuttakeSlide";

    private MotorEx motor;

    private final PIDFController controller = new PIDFController(kP, kI, kD, (pos) -> kF, threshold);

    public double transferPosition = 0.0;
    public double bypassPosition = 500;
    public double highChamberPosition = 1300;
    public double highBasketPosition = 1700;


    public Command transfer() {
        return new RunToPosition(motor, transferPosition, controller, this);
    }
    public Command bypass() {
        return new RunToPosition(motor, bypassPosition, controller, this);
    }
    public Command highChamber() {
        return new RunToPosition(motor, highChamberPosition, controller, this);
    }
    public Command highBasket() {
        return new RunToPosition(motor, highBasketPosition, controller, this);
    }

    @Override
    public void initialize() {
        motor = new MotorEx(name);
    }

    @NonNull
    @Override
    public Command getDefaultCommand() { return new HoldPosition(motor, controller, this);}

    @Override
    public void periodic() {
        controller.setKP(kP);
        controller.setKI(kI);
        controller.setKD(kD);
        controller.setSetPointTolerance(threshold);

        OpModeData.telemetry.addData("OuttakeSlide Position", motor.getCurrentPosition());
        OpModeData.telemetry.addData("OuttakeSlide Target", controller.getTarget());
    }

    public void resetEncoder() {
        motor.resetEncoder();
    }
}