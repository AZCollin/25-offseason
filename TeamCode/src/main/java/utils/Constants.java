package utils;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Constants {

    // - INTAKE CLAW - //

    public static double IntakeClawOpen = 0.5;
    public static double IntakeClawClosed = 0.72;

   // - OUTTAKE CLAW - //

    public static double OuttakeClawOpen = 0.6;
    public static double OuttakeClawClosed = 0.71;

    // - OUTTAKE SLIDE - //

    public static double OuttakeSlideMaxPosition = 1700;
    public static double OuttakeSlideThreshold = 30;

    // - INTAKE SLIDE - //
    public static double IntakeSlideOut = 0.03;
    public static double IntakeSlideIn = 0.7;

    // - INTAKE ARM - //

    public static double IntakeArmPickup = -300;
    public static double IntakeArmTransfer  = 100;
    public static double IntakeArmClip = 250;
    public static double IntakeArmPrePickup = -300;

    // - BELT - //

    // - CLIPPER - //
}
