package opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;
import com.rowanmcalpin.nextftc.ftc.OpModeData;

import subsystems.TestMotorUsingNextFTCController;

@TeleOp(name = "Test NextFTC+NextFTC Controller")
public class TestOpModeNextFTC extends NextFTCOpMode {

    private double lastLoopTimestamp = 0.0;

    public TestOpModeNextFTC() {
        super(TestMotorUsingNextFTCController.INSTANCE);
    }

    @Override
    public void onInit() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        OpModeData.telemetry = telemetry;
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
