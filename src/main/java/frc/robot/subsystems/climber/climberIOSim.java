package frc.robot.subsystems.climber;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class climberIOSim implements climberIO {
  private final DCMotorSim parker =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getCIM(2), 1.0, 0.02), DCMotor.getCIM(2));

  @Override
  public void setClimberSpeed(double speed) {
    parker.setInputVoltage(speed * 12.00);
  }
}
