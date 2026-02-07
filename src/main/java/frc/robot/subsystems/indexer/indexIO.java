package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.AutoLog;

public interface indexIO {
    @AutoLog
    public class indexIOInputs {
        public final int motorPort = 0;
    }

    // Turnmotor void
    public void turnMotor(double speed);
}
