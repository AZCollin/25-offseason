package FTCLib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;

import FTCLib.Bot;

@Config
public class Belt extends SubsystemBase {
    private final Bot bot;

    private final DcMotor outtakeSlide;
    private final PIDController controller;
    public static double kP = 0.005, kI = 0.0, kD = 0.0;
    public static double target = 0.0, threshold = 30, minExtension = 0.0, maxExtension = 200;

    public Belt(Bot bot) {
        this.bot = bot;
        outtakeSlide = bot.hardwareMap.get(DcMotor.class, "Belt");
        controller = new PIDController(kP, kI, kD);
    }

    @Override
    public void periodic() {
        int currentPosition = outtakeSlide.getCurrentPosition();

        controller.setPID(kP, kI, kD);

        double power = controller.calculate(currentPosition, target);

        outtakeSlide.setPower(power);

        // Telemetry for debugging
        bot.telem.addData("Belt Position", currentPosition);
        bot.telem.addData("Belt Target", target);
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
