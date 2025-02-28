package utils;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.rowanmcalpin.nextftc.pedro.FollowerNotInitializedException;

public class TrajectoryBuilder {

    public static final Pose startPose = new Pose(7.5, 112.5, Math.toRadians(270));
    public static final Pose placePosition = new Pose(14, 127, Math.toRadians(325));

    public static PathChain startToBucket;

    public static void buildBucketPaths(Follower follower) {
        if (follower == null) {
            try {
                throw new FollowerNotInitializedException();
            } catch (FollowerNotInitializedException e) {
                throw new RuntimeException(e);
            }
        }

        startToBucket = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(startPose), new Point(placePosition)
                        )
                ).setLinearHeadingInterpolation(startPose.getHeading(), placePosition.getHeading()).build();
    }
}
