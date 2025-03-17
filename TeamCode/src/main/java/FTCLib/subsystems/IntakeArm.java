package FTCLib.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.rowanmcalpin.nextftc.ftc.OpModeData;

import FTCLib.Bot;
import utils.Constants;

@Config
public class IntakeArm extends SubsystemBase {
    private final Bot bot;

    private final DcMotor intakeArm;
    private final PIDFController controller;
    public static double kP = 0.0, kI = 0.0, kD = 0.0, kF = 0.0;
    public static double target = 0.0, minAngle = 0.0, maxAngle = 260, adjustment = 90;

    public double ticksInDegrees = 537.7 / 360.0;

    public IntakeArm(Bot bot) {
        this.bot = bot;
        intakeArm = bot.hardwareMap.get(DcMotor.class, "IntakeArm");
        controller = new PIDFController(kP, kI, kD, kF);
    }

    @Override
    public void periodic() {

        double pivotAngle = intakeArm.getCurrentPosition() / ticksInDegrees; // Get the current position of the arm in degrees

        controller.setF(kF * (Math.cos(pivotAngle - Math.toRadians(adjustment)))); //Not sure what 90 should be

        double power = controller.calculate(Math.toDegrees(intakeArm.getCurrentPosition()), target + adjustment);
        intakeArm.setPower(power);

        bot.telem.addData("IntakeArm Angle", pivotAngle - adjustment);
        bot.telem.addData("IntakeArm Target", target);
    }

    public void setTarget(double target) {
        this.target = Math.max(minAngle, Math.min(maxAngle, target));
    }

    public void pickup() {
        this.target = Math.max(minAngle, Math.min(maxAngle, Constants.IntakeArmPickup));
    }

    public double getTarget() {
        return target;
    }

    public double getPosition(){
        return Math.toDegrees(intakeArm.getCurrentPosition() - adjustment);
    }
}
