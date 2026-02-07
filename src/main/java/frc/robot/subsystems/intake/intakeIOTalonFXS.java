package frc.robot.subsystems.intake;

public class intakeIOTalonFXS implements intakeIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFXS intakeMotor;

  public intakeIOTalonFXS(int intakeMotorPort) {
    intakeMotor = new com.ctre.phoenix6.hardware.TalonFXS(intakeMotorPort);
  }

  @Override
  public void spinThatStuff(double initialSpeed) {
    intakeMotor.set(initialSpeed);
  }

  @Override
  public void updateInputs(intakeIOInputs inputs){
    inputs.isBallDetected = false;
    inputs.motorCurrent = intakeMotor.get();
  }

  @Override
  public void stopMotors() {
    intakeMotor.set(0);
  }
}
