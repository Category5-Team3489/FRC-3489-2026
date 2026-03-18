package frc.robot.subsystems.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import edu.wpi.first.math.MathUtil;

public class intakeIOTalonFX implements intakeIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFX intakeMotor;
  private final com.ctre.phoenix6.hardware.TalonFX actuatorMotor2;
  private final com.ctre.phoenix6.hardware.TalonFX actuatorMotor1;
  private final PositionDutyCycle positionRequest = new PositionDutyCycle(0.0);

  public intakeIOTalonFX(int intakeMotorPort, int actmotorport1, int actmotorport2) {
    intakeMotor = new com.ctre.phoenix6.hardware.TalonFX(intakeMotorPort);
    actuatorMotor1 = new com.ctre.phoenix6.hardware.TalonFX(actmotorport1);
    actuatorMotor2 = new com.ctre.phoenix6.hardware.TalonFX(actmotorport2);
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kP = 0.09;
    config.Slot0.kI = 0.0;
    config.Slot0.kD = 0.0;
    actuatorMotor1.getConfigurator().apply(config);
    actuatorMotor2.getConfigurator().apply(config);
  }

  @Override
  public void spinThatStuff(double initialSpeed) {
    double currentSpeed = initialSpeed * 12.0;
    currentSpeed = MathUtil.clamp(currentSpeed, -10, 10);

    intakeMotor.setVoltage(currentSpeed);
  }

  @Override
  public void updateInputs(intakeIOInputs inputs) {
    inputs.isBallDetected = false;
    inputs.motorCurrent = intakeMotor.get();
  }

  @Override
  public void setIntakeVoltage(double volts) {
    intakeMotor.setVoltage(volts);
  }

  @Override
  public void moveInorOut(double speed1) {
    double maxActPos = 82.5;
    double minActPos = 0.5;
    double actPos = actuatorMotor1.getPosition().getValueAsDouble();
    double speed = speed1;
    speed = MathUtil.clamp(speed, -0.8, 0.8);

    if (actuatorMotor1.getSupplyCurrent().getValueAsDouble() > 20
        || actuatorMotor1.getSupplyCurrent().getValueAsDouble() < -20) {
      speed = 0;
    }
    if (speed > 0) {
      actuatorMotor1.setControl(positionRequest.withPosition(maxActPos));
      actuatorMotor2.setControl(positionRequest.withPosition(-maxActPos));
    } else if (speed < 0) {
      actuatorMotor1.setControl(positionRequest.withPosition(0));
      actuatorMotor2.setControl(positionRequest.withPosition(0));
    } else {
      actuatorMotor1.set(0);
      actuatorMotor2.set(0);
    }
  }

  @Override
  public void stopMotors() {
    intakeMotor.set(0);
  }
}
