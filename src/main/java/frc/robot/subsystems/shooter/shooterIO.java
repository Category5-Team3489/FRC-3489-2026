package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

public interface shooterIO {
  @AutoLog
  public class shooterIOInputs {
    public double topMotorCurrent = 0.0;
    public double bottomMotorCurrent = 0.0;
    public double shootAngle = 0.0;
    public double distanceToTarget = 0.0;
    public double gearRatio = 1.0;
  }

  // Update inputs
  public void updateInputs(shooterIOInputs inputs);

  // Set motor to angle
  public void stopMotors();

  public void shootBall(double speed);

  public void setShootAngle(double angle);
}
