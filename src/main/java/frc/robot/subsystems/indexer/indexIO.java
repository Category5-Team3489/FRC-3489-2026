package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.AutoLog;

public interface indexIO {
  @AutoLog
  public class indexIOInputs {
    public int motorPort = 16;
  }

  // Turnmotor void
  public void turnMotor(double speed);
}
