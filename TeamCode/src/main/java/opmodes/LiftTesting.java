package opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import subsystems.Lift;

@TeleOp(name = "LiftTesting")
public class LiftTesting extends PedroOpMode {
    public LiftTesting() {
        super(Lift.INSTANCE);
    }
    @Override
    public void onInit() {
        OpModeData.telemetry = telemetry;
        mecanumDriveInit();
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

    public void mecanumDriveInit() {}

    private void registerControls() {
        //gamepadManager.getGamepad2().getY().setPressedCommand(Lift.INSTANCE::toHigh);
        gamepadManager.getGamepad2().getB().setPressedCommand(Lift.INSTANCE::toMiddle);
        //gamepadManager.getGamepad2().getA().setPressedCommand(Lift.INSTANCE::toLow);
    }
}

