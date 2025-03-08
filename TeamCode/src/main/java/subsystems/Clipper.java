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

public class Clipper extends Subsystem {
    public static final Clipper INSTANCE = new Clipper();
    private Clipper() {}
    public MotorEx motor;
    public double Kf;
    public PIDFController controller = new PIDFController(0.005,0.0,0.0,new StaticFeedforward(0.0));
    public String name = "Clipper";

    @Override
    public void initialize(){
        motor = new MotorEx(name);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry.addData("Clipper Position",motor.getCurrentPosition());
    }

    @Override
    @NotNull
    public Command getDefaultCommand(){
        return new HoldPosition(motor, controller,this);
    }

    public Command pickup(){
        return new RunToPosition(motor,100,controller,this);
    }

    public Command toPosition(double targetPosition){
        return new RunToPosition(motor,targetPosition,controller,this);
    }
    public Command resetEncoderZero() {
        motor.setCurrentPosition(0);
        return new NullCommand();
    }















}
