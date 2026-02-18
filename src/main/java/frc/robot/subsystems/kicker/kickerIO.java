package frc.robot.subsystems.kicker;

import org.littletonrobotics.junction.AutoLog;

public interface kickerIO {
  @AutoLog
  public class indexIOInputs {
    public int motorPort = 16;
  }

  // Turnmotor void
  public void turnMotor(double speed);
}
