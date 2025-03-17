package subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

import org.jetbrains.annotations.NotNull;

@Config
public class Clipper extends Subsystem {

    public static final Clipper INSTANCE = new Clipper();
    private Clipper() {}
    public MotorEx motor;
    public static double kP = 0.005, kI = 0, kD = 0.0, kF = 0.0, threshold = 10.0;
    public static double target = 0;
    public PIDFController controller = new PIDFController(kP,kI,kD,new StaticFeedforward(kF), threshold);
    public String name = "Clipper";

    @Override
    public void initialize(){
        motor = new MotorEx(name);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry = new MultipleTelemetry(OpModeData.telemetry, FtcDashboard.getInstance().getTelemetry());

        controller.setKD(kD);
        controller.setKP(kP);
        controller.setKI(kI);
        controller.setTarget(target);
        controller.setSetPointTolerance(threshold);

        OpModeData.telemetry.addData("clipper pos: ", motor.getCurrentPosition());
        OpModeData.telemetry.addData("clipper target: ", target);
    }

    @Override
    @NotNull
    public Command getDefaultCommand(){
        return new HoldPosition(motor, controller,this);
    }

    public Command pickup(){
        return new RunToPosition(motor,target,controller,this);
    }

    public Command toPosition(double targetPosition){
        return new RunToPosition(motor,targetPosition,controller,this);
    }
    public void resetEncoderZero() { motor.resetEncoder();}

    public double getPosition(){
        return motor.getCurrentPosition();
    }
}
