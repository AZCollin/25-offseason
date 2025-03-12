package opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup;
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand;
import com.rowanmcalpin.nextftc.core.command.utility.conditionals.PassiveConditionalCommand;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.driving.MecanumDriverControlled;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.pedro.PedroOpMode;

import subsystems.Belt;
import subsystems.Clipper;
import subsystems.IntakeArm;
import subsystems.IntakeClaw;
import subsystems.IntakeSlide;
import subsystems.OuttakeClaw;
import subsystems.OuttakeSlide;

@TeleOp(name = "ArmTesting")
public class ArmTesting extends PedroOpMode {
    public ArmTesting() {
        super(IntakeArm.INSTANCE);
    }
    @Override
    public void onInit() {
        OpModeData.telemetry = telemetry;
        mecanumDriveInit();
        telemetry.update();
    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
        registerControls();
    }

    @Override
    public void onUpdate() {
        telemetry.update();
    }

    @Override
    public void onStop() {

    }

    public void mecanumDriveInit() {
        IntakeArm.INSTANCE.resetEncoderZero();
    }

    private void registerControls() {
        gamepadManager.getGamepad2().getY().setPressedCommand(IntakeArm.INSTANCE::pickup);
        gamepadManager.getGamepad2().getA().setPressedCommand(IntakeArm.INSTANCE::transfer);
        gamepadManager.getGamepad2().getDpadDown().setPressedCommand(IntakeArm.INSTANCE::clip);
    }
}

