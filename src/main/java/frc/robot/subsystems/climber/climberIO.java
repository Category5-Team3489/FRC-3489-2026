package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

public interface climberIO {
  @AutoLog
  public class climberIOInputs {
    public int motorPort = 0;
  }

  // Climbmotor void
  public void setClimberSpeed(double speed);
}
