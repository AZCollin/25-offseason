package subsystems;

import com.acmerobotics.dashboard.config.Config;
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
public class OuttakeSlide extends Subsystem {
    public static final OuttakeSlide INSTANCE = new OuttakeSlide();

    private OuttakeSlide() {}
    public MotorEx motor;

    public double Kf;
    public static double kP = 0.008, kI = 0, kD = 0.0003, kF = 0.1;
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
        OpModeData.telemetry.addData("OuttakeSlide Position",motor.getCurrentPosition());
        //controller.setKP(kP);
        //controller.setKI(kI);
        //controller.setKD(kD);
        //controller.setSetPointTolerance(targetTolerance);
        //controller.setTarget(target);
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
    public Command resetEncoderZero() {
        motor.setCurrentPosition(0);
    }















}
