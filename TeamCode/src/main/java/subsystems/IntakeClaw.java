package subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

import java.util.Objects;

import utils.Constants;

public class IntakeClaw extends Subsystem {
    public static final IntakeClaw INSTANCE = new IntakeClaw();
    private IntakeClaw() {}
    public Servo servo;
    public String name = "IntakeClawServo";
    public String state;

    @Override
    public void initialize(){
        servo = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, name);
        open();
    }

    @Override
    public void periodic(){
        OpModeData.telemetry.addData("IntakeClaw State", state);
    }

    public Command open(){
        state = "OPEN";
        return new ServoToPosition(servo, Constants.IntakeClawOpen, this);
    }

    public Command close(){
        state = "CLOSE";
        return new ServoToPosition(servo, Constants.IntakeClawClosed, this);
    }
    public Command setPosition(double target){
        return new ServoToPosition(servo, target, this);
    }

    public Command toggle(){
        if (Objects.equals(state, "OPEN")){
            return close();
        } else {
            return open();
        }
    }



}
