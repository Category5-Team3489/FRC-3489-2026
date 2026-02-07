package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface intakeIO {
    @AutoLog
    public class intakeIOInputs {
        public double motorCurrent = 0.0;
        public boolean isBallDetected = false;
    }

    // added an update inputs class
    public void updateInputs(intakeIOInputs inputs);

    public void spinThatStuff(double initialSpeed);

    public void stopMotors();
}
