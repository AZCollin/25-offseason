package FTCLib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;

import FTCLib.Bot;
import utils.Constants;

@Config
public class IntakeArmNew extends SubsystemBase {
    private final Bot bot;
    private final DcMotor intakeArm;
    private final PIDController controller;
    public static double kP = 0.0015, kI = 0.0, kD = 0.0003, kF = 0.02;
    public static double target = 0.0, threshold = 30, minExtension = 0.0, maxExtension = 750;

    public double ticksInDegrees = 537.7 / 360.0;

    public IntakeArmNew(Bot bot) {
        this.bot = bot;
        intakeArm = bot.hardwareMap.get(DcMotor.class, "IntakeArm");
        controller = new PIDController(kP, kI, kD);
    }

    @Override
    public void periodic() {
        double currentPosition = intakeArm.getCurrentPosition();

        controller.setPID(kP, kI, kD);

        double pid = controller.calculate(currentPosition, target);
        double ff = Math.cos(Math.toRadians(target / ticksInDegrees)) * kF;
        intakeArm.setPower(pid + ff);

        bot.telem.addData("IntakeArm pos: ", intakeArm.getCurrentPosition());
        bot.telem.addData("IntakeArm Target", target);
    }


    public void setTarget(double target) {
        this.target = Math.max(minExtension, Math.min(maxExtension, target));
    }

    public void pickup() {
        this.target = Math.max(minExtension, Math.min(maxExtension, Constants.IntakeArmPickup));
    }

    public double getTarget() {
        return target;
    }

    public double getPosition(){
        return intakeArm.getCurrentPosition();
    }
}
