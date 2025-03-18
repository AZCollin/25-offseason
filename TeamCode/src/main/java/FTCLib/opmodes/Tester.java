package FTCLib.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp(name = "Tester")
public class Tester extends OpMode {
    private PIDController controller;
    public static double p = 0.00, i = 0, d = 0.0;
    public static double f = 0.05;
    public static int target = 0;
    private final double ticksInDegree = 537.7 / 180;
    private DcMotorEx motor;
    @Override
    public void init() {
        controller = new PIDController(p,i,d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motor = hardwareMap.get(DcMotorEx.class, "Clipper");
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        controller.setPID(p,i,d);
        int pos = motor.getCurrentPosition();
        double pid = controller.calculate(pos,target);
        //double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
        //double power = pid + ff;
        motor.setPower(pid);

        telemetry.addData("pos", pos);
        telemetry.addData("target", target);
        telemetry.update();
    }
}
