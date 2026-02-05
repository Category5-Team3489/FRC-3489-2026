package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.DCMotor;

public class intakeIOSim implements intakeIO {
  private intakeIOInputs inputs = new intakeIOInputs();
  private final DCMotor motor = DCMotor.getCIM(1);

  // idek what to do bro

  // This is the spin that stuff void
  @Override
  public void spinThatStuff(double initialSpeed) {
    motor.setInputVoltage(initialSpeed);
  }

  @Override
  public void stopMotors() {
    motor.setInputVoltage(0);
  }
}
