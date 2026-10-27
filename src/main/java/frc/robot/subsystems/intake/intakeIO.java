package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface intakeIO {
  @AutoLog
  public class intakeIOInputs {
    public double motorCurrent = 0.0;
    public boolean isBallDetected = false;
    public double velocityflywheel = 0.0;
    public double appliedVolts = 0.0;
    public double actuatorPositionRotations = 0.0;
    public double actuatorVelocityRotationsPerSecond = 0.0;
    public double actuatorCurrentAmps = 0.0;
    public boolean extended = false;
    public boolean retracted = true;
  }

  // added an update inputs class
  public void updateInputs(intakeIOInputs inputs);

  public void spinThatStuff(double initialSpeed);

  public void setIntakeVoltage(double volts);

  public void moveInorOut(double speed);

  public void extend();

  // public void unextend();

  public boolean isExtended();

  public void retract();

  public boolean isRetracted();

  public void stopMotors();

  public void toggleSpinIntake(boolean isSpinning);

  public void toggleSpinOuttake(boolean isSpinning);
}
