package opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import subsystems.Belt;
import subsystems.Clipper;
import subsystems.lib.Lift;

@TeleOp(name = "LiftTesting")
public class LiftTesting extends PedroOpMode {
    private double lastTime = 0.0;
    public LiftTesting() {
        super(Lift.INSTANCE, Belt.INSTANCE, Clipper.INSTANCE);
    }
    @Override
    public void onInit() {
        OpModeData.telemetry = telemetry;
        telemetry.update();

        Lift.INSTANCE.resetEncoderZero();
        Belt.INSTANCE.resetEncoderZero();
        Clipper.INSTANCE.resetEncoderZero();
    }

    @Override
    public void onWaitForStart() {
        //telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        registerControls();
    }

    @Override
    public void onUpdate() {
        if (lastTime == 0.0) {
            lastTime = System.nanoTime() / 1E9;
        }
        double loopTime = System.nanoTime() / 1E9 - lastTime;
        lastTime = System.nanoTime() / 1E9;
        telemetry.addData("Loop time", loopTime);
        telemetry.update();
    }

    @Override
    public void onStop() {}

    private void registerControls() {
        //gamepadManager.getGamepad2().getY().setPressedCommand(Lift.INSTANCE::toHigh);
        //gamepadManager.getGamepad2().getB().setPressedCommand(Lift.INSTANCE::toMiddle);
        //gamepadManager.getGamepad2().getA().setPressedCommand(Lift.INSTANCE::toLow);

        gamepadManager.getGamepad2().getY().setPressedCommand(Belt.INSTANCE::pickup);
        gamepadManager.getGamepad2().getB().setPressedCommand(Clipper.INSTANCE::pickup);

    }
}

