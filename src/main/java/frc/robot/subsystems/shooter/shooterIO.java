package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public interface shooterIO {
  @AutoLog
  public class shooterIOInputs {
    public double topMotorCurrent = 0.0;
    public double bottomMotorCurrent = 0.0;
    public double shootAngle = 0.0;
    public double distanceToTarget = 0.0;
    public double gearRatio = 1.0;
    public Mechanism2d turnMechanism = new Mechanism2d(1,1);
    public MechanismRoot2d root = turnMechanism.getRoot("root", 0,0);
    public MechanismLigament2d shooterTurn = root.append(new MechanismLigament2d("shooter direction", 1, 0));
  }

  // Update inputs
  public void updateInputs(shooterIOInputs inputs);

  // Set motor to angle
  public void stopMotors();

  public void shootBall(double speed);

  public void setShootAngle(double angle);
}
