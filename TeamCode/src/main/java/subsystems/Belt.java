package subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
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

public class Belt extends Subsystem {
    public static final Belt INSTANCE = new Belt();
    private Belt() {}
    public MotorEx motor;
    public double Kf;
    public PIDFController controller = new PIDFController(0.005,0.0,0.0,new StaticFeedforward(0.0));
    public String name = "Belt";

    @Override
    public void initialize(){
        motor = new MotorEx(name);

    }

    @Override
    public void periodic(){
        OpModeData.telemetry.addData("Belt Position",motor.getCurrentPosition());
        OpModeData.telemetry.addData()
    }

    @Override
    @NotNull
    public Command getDefaultCommand(){
        return new HoldPosition(motor, controller,this);
    }

    public Command pickup(){
        return new RunToPosition(motor,1000,controller,this);
    }
    public Command upPosition() { return new RunToPosition(motor, +10,controller, this);}
    public Command downPosition() { return new RunToPosition(motor, -10,controller, this);}
    public Command toPosition(double targetPosition){
        return new RunToPosition(motor,targetPosition,controller,this);
    }
    public Command resetEncoderZero() {
        motor.setCurrentPosition(0);
        return new NullCommand();
    }















}
