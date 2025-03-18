package FTCLib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;

import FTCLib.Bot;

@Config
public class OuttakeSlideNew extends SubsystemBase {
    private final Bot bot;

    private final DcMotor outtakeSlide;
    private final PIDFController controller;
    public static double kP = 0.007, kI = 0.0002, kD = 0.0002, kF = 0.0002;
    public static double target = 0.0, threshold = 30, minExtension = 0.0, maxExtension = 1700;

    public double ticksInDegrees = 537.7 / 360.0;

    public OuttakeSlideNew(Bot bot) {
        this.bot = bot;
        outtakeSlide = bot.hardwareMap.get(DcMotor.class, "OuttakeSlide");
        controller = new PIDFController(kP, kI, kD, kF);
    }

    @Override
    public void periodic() {
        int currentPosition = outtakeSlide.getCurrentPosition();

        controller.setPIDF(kP, kI, kD, kF);
        controller.setF(kF);

        double power = controller.calculate(currentPosition, target);

        outtakeSlide.setPower(power);

        // Telemetry for debugging
        bot.telem.addData("Slide Position", currentPosition);
        bot.telem.addData("Slide Target", target);
    }

    public void setTarget(double target) {
        this.target = Math.max(minExtension, Math.min(maxExtension, target));
    }

    public double getTarget() {
        return target;
    }

    public double getPosition(){
        return outtakeSlide.getCurrentPosition();
    }
}
