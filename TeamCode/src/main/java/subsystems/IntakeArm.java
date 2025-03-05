package subsystems;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.Controller;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

import org.jetbrains.annotations.NotNull;

import utils.Constants;

public class IntakeArm extends Subsystem {
    public static final IntakeArm INSTANCE = new IntakeArm();
    private IntakeArm() {}
    public MotorEx motor;
    public double Kf;
    public PIDFController controller = new PIDFController(0.001,0.0,0.0,new StaticFeedforward(0.05));
    public String name = "IntakeArm";

    @Override
    public void initialize(){
        motor = new MotorEx(name);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry.addData("IntakeArm Position",motor.getCurrentPosition());
    }

    @Override
    @NotNull
    public Command getDefaultCommand(){
        return new HoldPosition(motor, controller,this);
    }

    public Command pickup(){
        return new RunToPosition(motor, Constants.IntakeArmPickup,controller,this);
    }
    public Command transfer(){
        return new RunToPosition(motor, Constants.IntakeArmTransfer,controller,this);
    }
    public Command clip(){
        return new RunToPosition(motor, Constants.IntakeArmClip,controller,this);
    }

    public Command toPosition(double targetPosition){
        return new RunToPosition(motor,targetPosition,controller,this);
    }
    public Command resetEncoderZero() {
        motor.setCurrentPosition(0);
        return new NullCommand();
    }















}
