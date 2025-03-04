package subsystems;

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

public class OuttakeSlide extends Subsystem {
    public static final OuttakeSlide INSTANCE = new OuttakeSlide();
    private OuttakeSlide() {}
    public MotorEx motor;
    public double Kf;
    public PIDFController controller = new PIDFController(0.005,0.0,0.0,new StaticFeedforward(0.0));
    public String name = "OuttakeSlide";

    @Override
    public void initialize(){
        motor = new MotorEx(name);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry.addData("OuttakeSlide Position",motor.getCurrentPosition());
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
        return new NullCommand();
    }















}
