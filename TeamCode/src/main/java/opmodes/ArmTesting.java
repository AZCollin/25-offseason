package opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import subsystems.lib.IntakeArmLib;
import subsystems.lib.IntakeArmTest;

@TeleOp(name = "ArmTesting")
public class ArmTesting extends PedroOpMode {
    public ArmTesting() {
        super(IntakeArmLib.INSTANCE);
    }
    @Override
    public void onInit() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        OpModeData.telemetry = telemetry;

        mecanumDriveInit();
        IntakeArmLib.INSTANCE.resetEncoderZero();

        telemetry.update();
    }

    @Override
    public void onWaitForStart() {}

    @Override
    public void onStartButtonPressed() {
        registerControls();
    }

    @Override
    public void onUpdate() {
        telemetry.update();
    }

    @Override
    public void onStop() {}

    public void mecanumDriveInit() {
        IntakeArmLib.INSTANCE.resetEncoderZero();
    }

    private void registerControls() {
        //gamepadManager.getGamepad2().getY().setPressedCommand(IntakeArmFix.INSTANCE::pickup);
        //gamepadManager.getGamepad2().getA().setPressedCommand(IntakeArmFix.INSTANCE::transfer);
        //gamepadManager.getGamepad2().getDpadDown().setPressedCommand(IntakeArmFix.INSTANCE::clip);
    }
}

