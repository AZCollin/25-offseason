package utils;

import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.hardware.Drivetrain;
import com.rowanmcalpin.nextftc.pedro.PedroData;
import com.rowanmcalpin.nextftc.pedro.FollowerNotInitializedException;

import java.util.Collections;
import java.util.Set;

public class FollowPathWithSpeed extends Command {
    private final PathChain path;
    private final boolean holdEnd;
    private final double maxPower;

    // Primary constructor
    public FollowPathWithSpeed(PathChain path, boolean holdEnd, double maxPower) {
        this.path = path;
        this.holdEnd = holdEnd;
        this.maxPower = maxPower;
    }

    // Secondary constructors (Java equivalent of Kotlin's constructor overloading)
    public FollowPathWithSpeed(Path path, boolean holdEnd) {
        this(new PathChain(path), holdEnd, 1.0);
    }

    public FollowPathWithSpeed(Path path) {
        this(new PathChain(path), false, 1.0);
    }

    public FollowPathWithSpeed(PathChain path) {
        this(path, false, 1.0);
    }

    @Override
    public boolean isDone() {
        return !PedroData.INSTANCE.getFollower().isBusy();
    }

    @Override
    public Set<Subsystem> getSubsystems() {
        return Collections.singleton(Drivetrain.INSTANCE);
    }

    @Override
    public void start() {
        if (PedroData.INSTANCE.getFollower() == null) {
            try {
                throw new FollowerNotInitializedException();
            } catch (FollowerNotInitializedException e) {
                throw new RuntimeException(e);
            }
        }

        PedroData.INSTANCE.getFollower().setMaxPower(maxPower);
        PedroData.INSTANCE.getFollower().followPath(path, holdEnd);
    }

    @Override
    public void stop(boolean interrupted) {
        PedroData.INSTANCE.getFollower().setMaxPower(1.0);
    }
}
