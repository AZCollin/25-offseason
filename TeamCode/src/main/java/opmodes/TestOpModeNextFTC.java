package opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.OpModeData;

import subsystems.IntakeArm;
import subsystems.OuttakeSlide;

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

        gamepadManager.getGamepad2().getA().setPressedCommand(IntakeArm.INSTANCE::clip);
        gamepadManager.getGamepad2().getB().setPressedCommand(IntakeArm.INSTANCE::transfer);
        gamepadManager.getGamepad2().getY().setPressedCommand(IntakeArm.INSTANCE::pickup);

        gamepadManager.getGamepad2().getDpadUp().setPressedCommand(OuttakeSlide.INSTANCE::highChamber);
        gamepadManager.getGamepad2().getDpadDown().setPressedCommand(OuttakeSlide.INSTANCE::transfer);
        gamepadManager.getGamepad2().getDpadLeft().setPressedCommand(OuttakeSlide.INSTANCE::highBasket);
        gamepadManager.getGamepad2().getDpadRight().setPressedCommand(OuttakeSlide.INSTANCE::bypass);
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
