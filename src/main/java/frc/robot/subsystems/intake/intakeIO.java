package frc.robot.subsystems.intake;

public interface intakeIO {
    public class intakeIOInputs {
        public double motorCurrent = 0.0;
        public boolean isBallDetected = false;
    }

    public void spinThatShit(double initialSpeed);

    public void stopMotors();
}
