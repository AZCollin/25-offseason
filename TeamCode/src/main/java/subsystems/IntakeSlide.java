package subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand;
import com.rowanmcalpin.nextftc.core.command.utility.conditionals.PassiveConditionalCommand;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

import java.util.Objects;

import utils.Constants;
public class IntakeSlide extends Subsystem {
    public static final IntakeSlide INSTANCE = new IntakeSlide();
    //private IntakeSlide() {}
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

    public boolean toggled = true;
    public Command toggle() {
        return new SequentialGroup(
                new InstantCommand(() -> {
                    toggled = !toggled;
                }),
                new PassiveConditionalCommand(
                        () -> toggled,
                        () -> new InstantCommand(() -> {
                            in().invoke();
                        }),
                        () -> new InstantCommand(() -> {
                            out().invoke();
                        })
                )
        );
    }
}
