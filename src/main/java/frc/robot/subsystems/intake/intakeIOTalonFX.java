package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import edu.wpi.first.math.MathUtil;

public class intakeIOTalonFX implements intakeIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFX intakeMotor;
  private final com.ctre.phoenix6.hardware.TalonFX actuatorMotor2;
  private final com.ctre.phoenix6.hardware.TalonFX actuatorMotor1;
  private final PositionDutyCycle positionRequest = new PositionDutyCycle(0.0);
  private final double maxActPos = 84;
  private final double minActPos = 0.5;
  private final PositionTorqueCurrentFOC m_positionTorque =
      new PositionTorqueCurrentFOC(0).withSlot(0);

  public intakeIOTalonFX(int intakeMotorPort, int actmotorport1, int actmotorport2) {
    intakeMotor = new com.ctre.phoenix6.hardware.TalonFX(intakeMotorPort);
    // CurrentLimitsConfigs limits = new CurrentLimitsConfigs();
    // limits.SupplyCurrentLimitEnable = true;
    // limits.SupplyCurrentLimit = 40;
    // intakeMotor.getConfigurator().apply(limits);

    actuatorMotor1 = new com.ctre.phoenix6.hardware.TalonFX(actmotorport1);
    actuatorMotor2 = new com.ctre.phoenix6.hardware.TalonFX(actmotorport2);
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kP = 0.09;
    config.Slot0.kI = 0.0;
    config.Slot0.kD = 0.0;

    config.Slot1.kP = 60.0;
    config.Slot1.kI = 0.0;
    config.Slot1.kD = 6.0;

    config.TorqueCurrent.withPeakForwardTorqueCurrent(Amps.of(120))
        .withPeakReverseTorqueCurrent(Amps.of(-120));

    actuatorMotor1.getConfigurator().apply(config);
    actuatorMotor2.getConfigurator().apply(config);

    // actuatorMotor1.setPosition(0);
    // actuatorMotor2.setPosition(0);
  }

  @Override
  public void spinThatStuff(double initialSpeed) {
    double currentSpeed = initialSpeed * 12.0;
    currentSpeed = MathUtil.clamp(currentSpeed, -10, 10);
    // VelocityDutyCycle cole = new VelocityDutyCycle(currentSpeed * 100);
    // intakeMotor.setControl(cole);
    VelocityTorqueCurrentFOC coleg = new VelocityTorqueCurrentFOC(currentSpeed);
    VoltageOut cole = new VoltageOut(currentSpeed);

    intakeMotor.set(initialSpeed);
  }

  @Override
  public void updateInputs(intakeIOInputs inputs) {
    inputs.isBallDetected = false;
    inputs.motorCurrent = intakeMotor.get();
    inputs.velocityflywheel = intakeMotor.getVelocity().getValueAsDouble();
  }

  @Override
  public void setIntakeVoltage(double volts) {
    intakeMotor.setVoltage(volts);
  }

  @Override
  public void moveInorOut(double speed1) {
    double actPos = actuatorMotor1.getPosition().getValueAsDouble();
    double speed = speed1;
    speed = MathUtil.clamp(speed, -0.8, 0.8);

    if (actuatorMotor1.getSupplyCurrent().getValueAsDouble() > 20
        || actuatorMotor1.getSupplyCurrent().getValueAsDouble() < -20) {
      speed = 0;
    }

    // if (speed > 0) {
    //   actuatorMotor1.setControl(positionRequest.withPosition(maxActPos));
    //   actuatorMotor2.setControl(positionRequest.withPosition(-maxActPos));
    // } else if (speed < 0) {
    //   actuatorMotor1.setControl(positionRequest.withPosition(0));
    //   actuatorMotor2.setControl(positionRequest.withPosition(0));
    // } else {
    //   actuatorMotor1.setControl(positionRequest.withPosition(0));
    //   actuatorMotor2.setControl(positionRequest.withPosition(0));
    // }

    System.out.println("Actuator position: " + actPos);
    if (actPos >= maxActPos) {
      if (speed > 0) {
        actuatorMotor1.set(0);
        actuatorMotor2.set(-1 * 0);
      } else {
        actuatorMotor1.set(speed);
        actuatorMotor2.set(-1 * speed);
      }
    } else if (actPos <= minActPos) {
      if (speed < 0) {
        actuatorMotor1.set(0);
        actuatorMotor2.set(-1 * 0);
      } else {
        actuatorMotor1.set(speed);
        actuatorMotor2.set(-1 * speed);
      }
    } else {
      actuatorMotor1.set(speed);
      actuatorMotor2.set(-1 * speed);
      actuatorMotor2.setPosition(0);
    }
  }

  @Override
  public void extend() {
    // PositionDutyCycle dutyCycle = new PositionDutyCycle(maxActPos);
    // dutyCycle.withVelocity(0.6);
    actuatorMotor1.setControl(positionRequest.withPosition(maxActPos));
    actuatorMotor2.setControl(positionRequest.withPosition(-maxActPos));
  }

  @Override
  public boolean isExtended() {
    return intakeMotor.getPosition().getValueAsDouble() == maxActPos;
  }

  @Override
  public void retract() {
    // PositionDutyCycle dutyCycle = new PositionDutyCycle(maxActPos);
    // dutyCycle.withVelocity(0.6);
    // intakeMotor.setControl(dutyCycle);
    actuatorMotor1.setControl(positionRequest.withPosition(0));
    actuatorMotor2.setControl(positionRequest.withPosition(0));
  }

  @Override
  public boolean isRetracted() {
    return intakeMotor.getPosition().getValueAsDouble() == minActPos;
  }

  @Override
  public void stopMotors() {
    intakeMotor.set(0);
  }

  @Override
  public void toggleSpinIntake(boolean isSpinning) {
    this.intakeMotor.set(isSpinning ? 0 : 0.8);
  }

  @Override
  public void toggleSpinOuttake(boolean isSpinning) {
    this.intakeMotor.set(isSpinning ? 0 : -0.8);
  }
}
