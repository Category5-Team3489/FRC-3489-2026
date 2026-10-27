package frc.robot.subsystems.intake;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class intakeIOSim implements intakeIO {
  private static final double EXTENDED_POSITION_ROTATIONS = 84.0;
  private static final double RETRACTED_POSITION_ROTATIONS = 0.0;

  private final DCMotorSim motor =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, 0.02), DCMotor.getCIM(1));
  private final DCMotorSim motor1 =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, 0.02), DCMotor.getCIM(1));
  private final DCMotorSim motor2 =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, 0.02), DCMotor.getCIM(1));
  private double intakeAppliedVolts = 0.0;
  private double actuatorAppliedVolts = 0.0;
  private double actuatorPositionRotations = RETRACTED_POSITION_ROTATIONS;

  // idek what to do bro

  // This is the spin that stuff void
  @Override
  public void spinThatStuff(double initialSpeed) {
    setIntakeVoltage(initialSpeed * 12.0);
  }

  @Override
  public void setIntakeVoltage(double volts) {
    intakeAppliedVolts = MathUtil.clamp(volts, -12.0, 12.0);
    motor.setInputVoltage(intakeAppliedVolts);
  }

  @Override
  public void updateInputs(intakeIOInputs inputs) {
    motor.update(0.02);
    motor1.update(0.02);
    motor2.update(0.02);

    actuatorPositionRotations =
        MathUtil.clamp(
            actuatorPositionRotations + (actuatorAppliedVolts / 12.0) * 35.0 * 0.02,
            RETRACTED_POSITION_ROTATIONS,
            EXTENDED_POSITION_ROTATIONS);

    inputs.isBallDetected = Math.abs(motor.getAngularVelocityRPM()) > 500.0;
    inputs.motorCurrent = Math.abs(motor.getCurrentDrawAmps());
    inputs.velocityflywheel = motor.getAngularVelocityRPM() / 60.0;
    inputs.appliedVolts = intakeAppliedVolts;
    inputs.actuatorPositionRotations = actuatorPositionRotations;
    inputs.actuatorVelocityRotationsPerSecond = (actuatorAppliedVolts / 12.0) * 35.0;
    inputs.actuatorCurrentAmps =
        (Math.abs(motor1.getCurrentDrawAmps()) + Math.abs(motor2.getCurrentDrawAmps())) / 2.0;
    inputs.extended = isExtended();
    inputs.retracted = isRetracted();
  }

  @Override
  public void moveInorOut(double speed) {
    actuatorAppliedVolts = MathUtil.clamp(speed * 12.0, -12.0, 12.0);
    motor1.setInputVoltage(actuatorAppliedVolts);
    motor2.setInputVoltage(-actuatorAppliedVolts);
  }

  @Override
  public void stopMotors() {
    setIntakeVoltage(0.0);
  }

  @Override
  public void extend() {
    moveInorOut(actuatorPositionRotations >= EXTENDED_POSITION_ROTATIONS ? 0.0 : 0.6);
  }

  @Override
  public boolean isExtended() {
    return actuatorPositionRotations >= EXTENDED_POSITION_ROTATIONS - 0.5;
  }

  @Override
  public void retract() {
    moveInorOut(actuatorPositionRotations <= RETRACTED_POSITION_ROTATIONS ? 0.0 : -0.6);
  }

  @Override
  public boolean isRetracted() {
    return actuatorPositionRotations <= RETRACTED_POSITION_ROTATIONS + 0.5;
  }

  @Override
  public void toggleSpinIntake(boolean isSpinning) {
    spinThatStuff(isSpinning ? 0.0 : 0.8);
  }

  @Override
  public void toggleSpinOuttake(boolean isSpinning) {
    spinThatStuff(isSpinning ? 0.0 : -0.8);
  }
}
