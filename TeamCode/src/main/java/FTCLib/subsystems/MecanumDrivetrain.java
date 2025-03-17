package FTCLib.subsystems;

import static com.arcrobotics.ftclib.kinematics.wpilibkinematics.SwerveDriveKinematics.normalizeWheelSpeeds;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import FTCLib.Bot;

public class MecanumDrivetrain extends SubsystemBase {
    private final Bot bot;
    private final DcMotorEx frontLeft, frontRight, backLeft, backRight;
    public static boolean fieldCentric = false, headingLock = false;

    public enum SpeedState { FAST, SLOW }
    private SpeedState state = SpeedState.FAST;
    double speedMultiplier = 1.0, slowSpeed = 0.5, fastSpeed = 1.0;

    public MecanumDrivetrain(Bot bot) {
        this.bot = bot;

        frontLeft = bot.hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = bot.hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = bot.hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = bot.hardwareMap.get(DcMotorEx.class, "backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void teleopDrive(Vector2d leftStick, double rx, double multiplier) {
        double x = leftStick.getX() * multiplier;
        double y = -leftStick.getY() * multiplier;

        if (state == SpeedState.SLOW) {
            x *= speedMultiplier;
            y *= speedMultiplier;
            rx *= speedMultiplier;
        }

        y *= 1.1; // counteract imperfect strafe
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        double[] powers = {frontLeftPower, frontRightPower, backLeftPower, backRightPower};
        double[] normalizedPowers = normalizeWheelSpeeds(powers);

        frontLeft.setPower(normalizedPowers[0]);
        frontRight.setPower(normalizedPowers[1]);
        backLeft.setPower(normalizedPowers[2]);
        backRight.setPower(normalizedPowers[3]);
    }

    public void resetEncoders() {
        backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        backLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    // Cant go above 1.0 thanks TIO
    private double[] normalizeWheelSpeeds(double[] speeds) {
        if (largestAbsolute(speeds) > 1) {
            double max = largestAbsolute(speeds);
            for (int i = 0; i < speeds.length; i++){
                speeds[i] /= max;
            }
        }
        return speeds;
    }

    private double largestAbsolute(double[] arr) {
        double largestAbsolute = 0;
        for (double d : arr) {
            double absoluteValue = Math.abs(d);
            if (absoluteValue > largestAbsolute) {
                largestAbsolute = absoluteValue;
            }
        }
        return largestAbsolute;
    }

    public void speedToggle() {
        if (state == SpeedState.FAST) {
            speedMultiplier = slowSpeed;
            state = SpeedState.SLOW;
        } else {
            state = SpeedState.FAST;
            speedMultiplier = fastSpeed;
        }
    }
}
