package frc.robot.subsystems.turrent;

import org.littletonrobotics.junction.AutoLog;

public interface turrentIO {
    @AutoLog
    public class turrentIOInputs {
        public double topMotorCurrent = 0.0;
        public double turrentAngle = 0.0;
    }

    // Update inputs
    public void updateInputs(turrentIOInputs inputs);

    // Set motor to angle
    public void setTurrentAngle(double degrees);
}
