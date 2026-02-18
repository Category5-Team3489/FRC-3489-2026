package frc.robot.subsystems.kicker;

public class kickerIOTalonFX implements kickerIO {
  // private final indexIOInputs inputs = new indexIOInputs();
  private final com.ctre.phoenix6.hardware.TalonFX kicker;

  public kickerIOTalonFX(int motorPortT) {
    // Initialize hardware here (e.g., motor controllers)
    kicker = new com.ctre.phoenix6.hardware.TalonFX(motorPortT);
  }

  @Override
  public void turnMotor(double speed) {
    kicker.set(1 * speed);
  }
}
