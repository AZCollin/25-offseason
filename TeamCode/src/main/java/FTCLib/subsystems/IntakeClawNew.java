package FTCLib.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import FTCLib.Bot;
import utils.Constants;

public class IntakeClawNew extends SubsystemBase {

    private final Bot bot;
    private final Servo intakeSlide;

    public enum IntakeSlideState { OPEN, CLOSED }
    private IntakeSlideState state = IntakeSlideState.CLOSED;

    public IntakeClawNew(Bot bot) {
        this.bot = bot;

        intakeSlide = bot.hardwareMap.get(Servo.class, "IntakeClaw");

    }

    public void open() {
        intakeSlide.setPosition(Constants.IntakeClawOpen);
        state = IntakeSlideState.OPEN;
    }

    public void close() {
        intakeSlide.setPosition(Constants.IntakeClawClosed);
        state = IntakeSlideState.CLOSED;
    }

    public void toggle(){
        if (state == IntakeSlideState.OPEN) {
            close();
        } else {
            open();
        }
    }
}
