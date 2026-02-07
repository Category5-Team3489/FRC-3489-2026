package frc.robot.subsystems.climber;

public interface climberIO {
    public class climberIOInputs {
        public final int motorPort = 0;
    }

    // Climbmotor void
    public void setClimberSpeed(double speed);
}
