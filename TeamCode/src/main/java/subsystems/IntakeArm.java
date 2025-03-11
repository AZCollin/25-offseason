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

    public static double kP = 0.005, kI = 0, kD = 0.0008, kF = 0.05;
    public static double targetTolerance = 10;
    public static double target = 0;


    public PIDFController controller = new PIDFController(kP,kI,kD, v -> kF, targetTolerance);
    public String name = "IntakeArm";

    public String state;

    @Override
    public void initialize(){
        motor = new MotorEx(name);
    }

    @Override
    public void periodic(){
        OpModeData.telemetry.addData("IntakeArm Position",motor.getCurrentPosition());

        controller.setKP(kP);
        controller.setKI(kI);
        controller.setKD(kD);
        controller.setSetPointTolerance(targetTolerance);
        controller.setTarget(target);
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
    public Command PREPICKUP(){
        state = "PREPICKUP";
        return new RunToPosition(motor, Constants.IntakeArmPREPICKUP,controller,this);
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
