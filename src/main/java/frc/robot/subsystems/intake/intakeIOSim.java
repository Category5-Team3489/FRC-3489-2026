package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class intakeIOSim implements intakeIO {
  private intakeIOInputs inputs = new intakeIOInputs();
  private final DCMotorSim motor =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, 0.02), DCMotor.getCIM(1));
  private final DCMotorSim motor1 =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, 0.02), DCMotor.getCIM(1));
  private final DCMotorSim motor2 =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, 0.02), DCMotor.getCIM(1));

  // idek what to do bro

  // This is the spin that stuff void
  @Override
  public void spinThatStuff(double initialSpeed) {
    motor.setInputVoltage(initialSpeed);
  }

  @Override
  public void updateInputs(intakeIOInputs inputs) {
    inputs.isBallDetected = false;
    inputs.motorCurrent = motor.getCurrentDrawAmps();
  }

  @Override
  public void moveInorOut(double speed){
    motor1.setInputVoltage(-12*speed);
    motor2.setInputVoltage(12*speed);
  }

  @Override
  public void stopMotors() {
    motor.setInputVoltage(0);
  }
}
