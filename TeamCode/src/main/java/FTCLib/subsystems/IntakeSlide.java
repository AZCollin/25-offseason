package FTCLib.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import FTCLib.Bot;
import utils.Constants;

public class IntakeSlide extends SubsystemBase {

    private final Bot bot;
    private final Servo intakeSlide;

    public enum IntakeSlideState { OUT, IN }
    private IntakeSlideState state = IntakeSlideState.IN;

    public IntakeSlide(Bot bot) {
        this.bot = bot;
        intakeSlide = bot.hardwareMap.get(Servo.class, "intakeSlide");
    }

    public void intake() {
        intakeSlide.setPosition(Constants.IntakeSlideOut);
        state = IntakeSlideState.IN;
    }

    public void outtake() {
        intakeSlide.setPosition(Constants.IntakeSlideIn);
        state = IntakeSlideState.OUT;
    }

    public void toggle(){
        if (state == IntakeSlideState.IN) {
            outtake();
        } else {
            intake();
        }
    }

}
