package frc.robot.subsystems.indexer;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class indexIOSim implements indexIO {
  private final indexIOInputs inputs = new indexIOInputs();
  private final DCMotorSim indexMotor =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, 0.02), DCMotor.getCIM(1));

  @Override
  public void turnMotor(double speed) {
    // Simulate motor behavior here, e.g., update inputs based on speed
    indexMotor.setInputVoltage(speed * 12.0); // Assuming speed is -1.0 to 1.0
  }
}
