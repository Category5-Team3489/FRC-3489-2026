package frc.robot.subsystems.turrent;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public class turrentIOTalonFX implements turrentIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFX topMotor;
  private final turrentIOInputs inputs = new turrentIOInputs();
  // Local dashboard visualization (do not include in AutoLog inputs)
  private final Mechanism2d turnMechanism = new Mechanism2d(1, 1);
  private final MechanismRoot2d root = turnMechanism.getRoot("turn root", 0, 0);
  private final MechanismLigament2d turentTurn =
      root.append(new MechanismLigament2d("turrent direction", 1, 0));

  public turrentIOTalonFX(int topMotorPort) {
    topMotor = new com.ctre.phoenix6.hardware.TalonFX(topMotorPort);
  }

  @Override
  public void updateInputs(turrentIOInputs inputs) {
    inputs.topMotorCurrent = topMotor.getSupplyVoltage().getValueAsDouble();
    inputs.turrentAngle = topMotor.getPosition().getValueAsDouble();
    turentTurn.setAngle(inputs.turrentAngle);
  }

  @Override
  public void turnTurrent(double speed){
    topMotor.set(speed);
  }

  @Override
  public void setTurrentAngle(double degrees) {
    double rotations =
        (degrees / 360.0)
            * inputs.gearRatio; // Convert degrees to rotations, accounting for gear ratio

    // Copied from the shooterIOTalonFX, check if this is correct
    PositionDutyCycle request = new PositionDutyCycle(rotations);
    topMotor.setControl(request);
  }
}
