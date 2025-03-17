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

import org.jetbrains.annotations.NotNull;

@Config
public class NextLift extends Subsystem {
    public static final NextLift INSTANCE = new NextLift();

    private NextLift() {}
    public MotorEx motor;

    public double Kf;
    public static double kP = 0.000, kI = 0, kD = 0.0000, kF = 0.0;
    public static double targetTolerance = 10;
    public static double target = 0;
    public PIDFController controller = new PIDFController(kP,kI,kD, new StaticFeedforward(kF), targetTolerance);
    public String name = "OuttakeSlide";
    @Override
    public void initialize(){
        motor = new MotorEx(name);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry = new MultipleTelemetry(OpModeData.telemetry, FtcDashboard.getInstance().getTelemetry());

        controller.setKP(kP);
        controller.setKI(kI);
        controller.setKD(kD);
        controller.setTarget(target);
        controller.setSetPointTolerance(targetTolerance);

        OpModeData.telemetry.addData("pos: ", motor.getCurrentPosition());
        OpModeData.telemetry.addData("target: ", target);
        OpModeData.telemetry.update();
    }

    @Override
    @NotNull
    public Command getDefaultCommand(){
        return new HoldPosition(motor, controller,this);
    }

    public Command pickup(){
        return new RunToPosition(motor,1000,controller,this);
    }

    public Command toPosition(double targetPosition){
        return new RunToPosition(motor,targetPosition,controller,this);
    }
    public void resetEncoderZero() {
        motor.setCurrentPosition(0);
    }















}
