package frc.robot.subsystems.turrent;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public class turrentIOSim implements turrentIO {
  private final turrentIOInputs inputs = new turrentIOInputs();
  private DCMotorSim topMotor;
  private final PIDController anglePID = new PIDController(1.0, 0.0, 0.0);

  // Local dashboard visualization (do not include in AutoLog inputs)
  private final Mechanism2d turnMechanism = new Mechanism2d(1, 1);
  private final MechanismRoot2d root = turnMechanism.getRoot("turn root", 0, 0);
  private final MechanismLigament2d turentTurn =
      root.append(new MechanismLigament2d("turrent direction", 1, 0));

  public turrentIOSim(double gearRatio) {

    // Simulate a motor with 1 CIM motor and a gear ratio of 1:1
    topMotor =
        new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getCIM(1), 1.0, gearRatio),
            DCMotor.getCIM(1));
  }

  @Override
  public void turnTurrent(double speed){
    topMotor.setInputVoltage(speed*12);
  }

  @Override
  public void updateInputs(turrentIOInputs inputs) {
    // Simulate some values for testing
    inputs.topMotorCurrent = 1.5; // Simulated current
    inputs.turrentAngle = topMotor.getAngularPositionRad(); // Simulated angle
    turentTurn.setAngle(inputs.turrentAngle);
    inputs.gearRatio = 1.0; // Simulated gear ratio
  }

  @Override
  public void setTurrentAngle(double degrees) {
    double targetRad = Math.toRadians(degrees);
    double currentRad = topMotor.getAngularPositionRad();
    double outputVoltage = anglePID.calculate(currentRad, targetRad);
    outputVoltage = MathUtil.clamp(outputVoltage, -12.0, 12.0);
    topMotor.setInputVoltage(outputVoltage);
  }
}
