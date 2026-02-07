package frc.robot.subsystems.indexer;

public class indexIOTalonFXS implements indexIO {
  private final indexIOInputsAutoLogged inputs = new indexIOInputsAutoLogged();
  private final com.ctre.phoenix6.hardware.TalonFXS indexMotor;

  public indexIOTalonFXS() {
    // Initialize hardware here (e.g., motor controllers)
    indexMotor = new com.ctre.phoenix6.hardware.TalonFXS(inputs.motorPort);
  }

  @Override
  public void turnMotor(double speed) {
    indexMotor.set(speed);
  }
}
