package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public class shooterIOTalonFX implements shooterIO {
  // Create motors
  private final TalonFX angleMotor;
  private final TalonFX shooterMotor;
  private final TalonFX angleMotorOther; // Example of a second motor if needed

  private final shooterIOInputs inputs = new shooterIOInputs();
  // Local dashboard visualization (do not include in AutoLog inputs)
  private final Mechanism2d turnMechanism = new Mechanism2d(1, 1);
  private final MechanismRoot2d root = turnMechanism.getRoot("shooter root", 0, 0);
  private final MechanismLigament2d shooterTurn =
      root.append(new MechanismLigament2d("shooter direction", 1, 0));

  public shooterIOTalonFX(int shooterMotorPort, int angleMotorPort, int angleMotorPortOther) {
    angleMotor = new TalonFX(angleMotorPort);
    shooterMotor = new TalonFX(shooterMotorPort);
    angleMotorOther = new TalonFX(angleMotorPortOther); // Example of initializing a second motor
  }

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    // TODO Auto-generated method stub
    // Phoenix6 StatusSignal APIs return signal objects; read numeric values
    inputs.topMotorCurrent = shooterMotor.getSupplyCurrent().getValueAsDouble();
    inputs.shootAngle = angleMotor.getPosition().getValueAsDouble() * 360.0;
    inputs.bottomMotorCurrent = angleMotor.getSupplyCurrent().getValueAsDouble();
    inputs.distanceToTarget = 0.0; // This would need a sensor to be implemented
    // Update local visualization ligament
    shooterTurn.setAngle(inputs.shootAngle);
  }

  @Override
  public void stopMotors() {
    // TODO Auto-generated method stub
    shooterMotor.set(-1);
    angleMotor.set(0);
    angleMotorOther.set(1);
  }

  @Override
  public void shootBall(double speed) {
    // Check this code fs
    shooterMotor.set(0);
    angleMotor.set(0);
    angleMotorOther.set(0);
  }

  @Override
  public void setShootAngle(double degrees) {
    // Check this code fs
    double rotations =
        (degrees / 360.0)
            * inputs.gearRatio; // Convert degrees to rotations, accounting for gear ratio
    PositionDutyCycle request = new PositionDutyCycle(rotations);
    angleMotor.setControl(request);
  }
}
