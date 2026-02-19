package frc.robot.subsystems.intake;

public class intakeIOTalonFXS implements intakeIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFXS intakeMotor;
  private final com.ctre.phoenix6.hardware.TalonFXS actuatorMotor2;
  private final com.ctre.phoenix6.hardware.TalonFXS actuatorMotor1;

  public intakeIOTalonFXS(int intakeMotorPort, int actmotorport1, int actmotorport2) {
    intakeMotor = new com.ctre.phoenix6.hardware.TalonFXS(intakeMotorPort);
    actuatorMotor1 = new com.ctre.phoenix6.hardware.TalonFXS(actmotorport1);
    actuatorMotor2 = new com.ctre.phoenix6.hardware.TalonFXS(actmotorport2);
  }

  @Override
  public void spinThatStuff(double initialSpeed) {
    intakeMotor.set(initialSpeed);
  }

  @Override
  public void setIntakeVoltage(double volts){
    intakeMotor.setVoltage(volts);
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
