package frc.robot.subsystems.indexer;

public class indexIOTalonFX implements indexIO {
  private final indexIOInputs inputs = new indexIOInputs();
  private final com.ctre.phoenix6.hardware.TalonFX indexMotor;
  private final com.ctre.phoenix6.hardware.TalonFX kicker;

  public indexIOTalonFX(int motorPortT) {
    // Initialize hardware here (e.g., motor controllers)
    indexMotor = new com.ctre.phoenix6.hardware.TalonFX(inputs.motorPort);
    kicker = new com.ctre.phoenix6.hardware.TalonFX(motorPortT);
  }

  @Override
  public void turnMotor(double speed) {
    indexMotor.set(speed * 0.3);
    kicker.set(speed);
  }
}
