package FTCLib.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import FTCLib.Bot;
import FTCLib.command.drive.TeleOpDriveCommand;
import FTCLib.command.outtakeSlide.SetOuttakeSlide;
import FTCLib.subsystems.IntakeArmNew;
import FTCLib.subsystems.IntakeClawNew;
import FTCLib.subsystems.IntakeSlideNew;
import FTCLib.subsystems.MecanumDrivetrain;
import FTCLib.subsystems.OuttakeSlideNew;

@TeleOp(name = "FTCLIBTeleop")
public class FTCLIBTeleop extends CommandOpMode {

    private Bot bot;
    private MecanumDrivetrain drivetrain;
    private IntakeClawNew intakeClaw;
    private IntakeSlideNew intakeSlide;
    private IntakeArmNew intakeArm;
    private OuttakeSlideNew outtakeSlide;

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


        outtakeSlide = bot.getOuttakeSlide();
        Button outtakeSlideMax = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_UP)).whenPressed(new SetOuttakeSlide(outtakeSlide, 1500));
        Button outtakeSlideMid = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_DOWN)).whenPressed(new SetOuttakeSlide(outtakeSlide, 750));
        register(outtakeSlide);

        bot.telem.update();

    }
}
