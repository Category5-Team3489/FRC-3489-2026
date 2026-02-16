package frc.robot.subsystems.turrent;

// Mechanism2d and related classes are used by the subsystem implementation
// for dashboard visualization and should not appear in the AutoLog inputs.
import org.littletonrobotics.junction.AutoLog;

public interface turrentIO {
  @AutoLog
  public class turrentIOInputs {
    public double topMotorCurrent = 0.0;
    public double turrentAngle = 0.0;
    public double gearRatio = 1.0;
    // Mechanism2d visualization objects belong to the subsystem implementation
    // and are not suitable for automatic logging. Create and manage them in
    // the subsystem class instead of the AutoLog inputs.
  }

  // Update inputs
  public void updateInputs(turrentIOInputs inputs);

  public void turnTurrent(double speed);
  // Set motor to angle
  public void setTurrentAngle(double degrees);
}
