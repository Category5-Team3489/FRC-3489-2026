package frc.robot.subsystems.kicker;

public class kickerIOTalonFXS implements kickerIO {
  private final indexIOInputs inputs = new indexIOInputs();
  private final com.ctre.phoenix6.hardware.TalonFXS indexMotor;

  public kickerIOTalonFXS() {
    // Initialize hardware here (e.g., motor controllers)
    indexMotor = new com.ctre.phoenix6.hardware.TalonFXS(inputs.motorPort);
  }

  @Override
  public void turnMotor(double speed) {
    indexMotor.set(speed);
  }
}
