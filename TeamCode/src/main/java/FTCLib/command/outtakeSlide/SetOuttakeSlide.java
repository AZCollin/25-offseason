package FTCLib.command.outtakeSlide;

import com.arcrobotics.ftclib.command.CommandBase;

import FTCLib.subsystems.OuttakeSlideNew;
import utils.Constants;

public class SetOuttakeSlide extends CommandBase {

    private final OuttakeSlideNew outtakeSlide;
    private double targetPosition = 0;
    public SetOuttakeSlide(OuttakeSlideNew outtakeSlide, double targetPosition) {
        if(targetPosition > Constants.OuttakeSlideMaxPosition) {targetPosition = Constants.OuttakeSlideMaxPosition; }

        this.targetPosition = targetPosition;
        this.outtakeSlide = outtakeSlide;

        addRequirements(outtakeSlide);
    }

    @Override
    public void initialize() { outtakeSlide.setTarget(targetPosition); }

    @Override
    public boolean isFinished() {
        return Math.abs(outtakeSlide.getPosition() - targetPosition) <= Constants.OuttakeSlideThreshold;
    }
}
