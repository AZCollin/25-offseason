package opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.OpModeData;

import subsystems.IntakeArm;
import subsystems.IntakeSlide;
import subsystems.OuttakeSlide;
import subsystems.TestMotorUsingNextFTCController;
import subsystems.lib.IntakeArmTest;

@TeleOp(name = "Test NextFTC Controller")
public class TestOpModeNextFTC extends NextFTCOpMode {

    private double lastLoopTimestamp = 0.0;

    public TestOpModeNextFTC() {
        super(IntakeArm.INSTANCE, OuttakeSlide.INSTANCE);
    }

    @Override
    public void onInit() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        OpModeData.telemetry = telemetry;

        IntakeArm.INSTANCE.resetEncoder();
        OuttakeSlide.INSTANCE.resetEncoder();

        gamepadManager.getGamepad2().getB().setPressedCommand(IntakeArm.INSTANCE::getTo1000);
        gamepadManager.getGamepad2().getA().setPressedCommand(IntakeArm.INSTANCE::getToZero);

        gamepadManager.getGamepad2().getDpadDown().setPressedCommand(OuttakeSlide.INSTANCE::getTo1000);
        gamepadManager.getGamepad2().getDpadUp().setPressedCommand(OuttakeSlide.INSTANCE::getToZero);
    }

    @Override
    public void onUpdate() {
        if (lastLoopTimestamp == 0.0) {
            lastLoopTimestamp = System.nanoTime() / 1E9;
        }

        OpModeData.telemetry.addData("Loop time", (System.nanoTime() / 1E9) - lastLoopTimestamp);
        lastLoopTimestamp = System.nanoTime() / 1E9;

        OpModeData.telemetry.update();
    }
}
