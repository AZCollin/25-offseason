package FTCLib.opmodes;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import FTCLib.Bot;
import FTCLib.command.drive.TeleOpDriveCommand;
import FTCLib.subsystems.IntakeArm;
import FTCLib.subsystems.IntakeClaw;
import FTCLib.subsystems.IntakeSlide;
import FTCLib.subsystems.MecanumDrivetrain;
import FTCLib.subsystems.OuttakeSlide;

@TeleOp(name = "FTCLIBTeleop")
public class FTCLIBTeleop extends CommandOpMode {

    private Bot bot;
    private MecanumDrivetrain drivetrain;
    private IntakeClaw intakeClaw;
    private IntakeSlide intakeSlide;
    private IntakeArm intakeArm;
    private OuttakeSlide outtakeSlide;

    private boolean enableDrive = true;

    private GamepadEx driverGamepad;
    private MultipleTelemetry telem;

    public void initialize() {
        CommandScheduler.getInstance().reset();

        telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        driverGamepad = new GamepadEx(gamepad1);

        bot = new Bot(telem, hardwareMap, true);

        drivetrain = bot.getDrivetrain();

        TeleOpDriveCommand driveCommand = new TeleOpDriveCommand(
                drivetrain,
                () -> -driverGamepad.getRightX(),
                () -> driverGamepad.getLeftY(),
                () -> -driverGamepad.getLeftX(),
                () -> 1.0
        );

        Button speedToggle = (new GamepadButton(driverGamepad, GamepadKeys.Button.RIGHT_BUMPER)).whenPressed(drivetrain::speedToggle);

        register(drivetrain);
        drivetrain.setDefaultCommand(driveCommand);

        intakeClaw = bot.getIntakeClaw();
        Button intakeClawToggle = (new GamepadButton(driverGamepad, GamepadKeys.Button.A)).whenPressed(intakeClaw::toggle);
        register(intakeClaw);

        intakeSlide = bot.getIntakeSlide();
        Button intakeSlideToggle = (new GamepadButton(driverGamepad, GamepadKeys.Button.B)).whenPressed(intakeSlide::toggle);
        register(intakeSlide);

        intakeArm = bot.getIntakeArm();
        Button intakeArmPickup = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_LEFT)).whenPressed(intakeArm::pickup);
        register(intakeArm);

        bot.telem.update();
    }
}
