package subsystems;

import com.acmerobotics.dashboard.config.Config;
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

import java.util.Objects;

import utils.Constants;
@Config
public class IntakeArm extends Subsystem {
    public static final IntakeArm INSTANCE = new IntakeArm();
    private IntakeArm() {}
    public MotorEx motor;
    public double Kf;
    public PIDFController controller = new PIDFController(0.001,0.0,0.0,new StaticFeedforward(0.05));
    public String name = "IntakeArm";

    public String state;

    @Override
    public void initialize(){
        motor = new MotorEx(name);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry.addData("IntakeArm Position",motor.getCurrentPosition());
        OpModeData.telemetry.addData("IntakeArm Position",motor.getMotor().getPIDFCoefficients());

    }

    @Override
    @NotNull
    public Command getDefaultCommand(){
        return new HoldPosition(motor, controller,this);
    }

    public Command pickup(){
        state = "PICKUP";
        return new RunToPosition(motor, Constants.IntakeArmPickup,controller,this);
    }
    public Command transfer(){
        state = "TRANSFER";
        return new RunToPosition(motor, Constants.IntakeArmTransfer,controller,this);
    }
    public Command clip(){
        state = "CLIP";
        return new RunToPosition(motor, Constants.IntakeArmClip,controller,this);
    }
    public Command toPosition(double targetPosition){
        return new RunToPosition(motor,targetPosition,controller,this);
    }
    public void resetEncoderZero() {
        motor.setCurrentPosition(0);
        new NullCommand();
    }

    public Command toggleSpecimen(){
        if (Objects.equals(state, "PICKUP")){ // If the state is "PICKUP" then set it to "CLIP"
            return clip();
        } else if (Objects.equals(state, "CLIP")) { // If the state is "CLIP" then set it to "TRANSFER"
            return transfer();
        } else {
            return pickup(); // Default state is "PICKUP", so starting the program will be "PICKUP"
        }
    }

    public Command toggleSample(){
        if (Objects.equals(state, "PICKUP")){ // If the state is "PICKUP" then set it to "TRANSFER"
            return transfer();
        } else {
            return pickup();
        }
    }

}
