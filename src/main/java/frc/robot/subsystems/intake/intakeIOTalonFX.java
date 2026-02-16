package frc.robot.subsystems.intake;

public class intakeIOTalonFX implements intakeIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFX intakeMotor;
  private final com.ctre.phoenix6.hardware.TalonFX actuatorMotor2;
  private final com.ctre.phoenix6.hardware.TalonFX actuatorMotor1;

  public intakeIOTalonFX(int intakeMotorPort, int actmotorport1, int actmotorport2) {
    intakeMotor = new com.ctre.phoenix6.hardware.TalonFX(intakeMotorPort);
    actuatorMotor1 = new com.ctre.phoenix6.hardware.TalonFX(actmotorport1);
    actuatorMotor2 = new com.ctre.phoenix6.hardware.TalonFX(actmotorport2);
  }

  @Override
  public void spinThatStuff(double initialSpeed) {
    intakeMotor.set(initialSpeed);
  }

  @Override
  public void updateInputs(intakeIOInputs inputs) {
    inputs.isBallDetected = false;
    inputs.motorCurrent = intakeMotor.get();
  }

  @Override
  public void moveInorOut(double speed) {
    actuatorMotor1.set(speed);
    actuatorMotor2.set(-1 * speed);
  }

  @Override
  public void stopMotors() {
    intakeMotor.set(0);
  }
}
