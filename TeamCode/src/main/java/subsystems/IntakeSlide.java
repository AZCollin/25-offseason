package subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

import java.util.Objects;

import utils.Constants;

public class IntakeSlide extends Subsystem {
    public static final IntakeSlide INSTANCE = new IntakeSlide();
    private IntakeSlide() {}
    public Servo servo;
    public String name = "IntakeSlide";
    public String state;

    @Override
    public void initialize(){
        servo = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, name);
        in();
    }

    @Override
    public void periodic(){
        OpModeData.telemetry.addData("IntakeSlide State", state);
    }

    public Command out(){
        state = "OUT";
        return new ServoToPosition(servo, Constants.IntakeSlideOut, this);
    }

    public Command in(){
        state = "IN";
        return new ServoToPosition(servo, Constants.IntakeSlideIn, this);
    }
    public Command setPosition(double target){
        return new ServoToPosition(servo, target, this);
    }

    public Command toggle(){
        if (Objects.equals(state, "OUT")){
            return in();
        } else {
            return out();
        }
    }



}
