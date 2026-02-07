package frc.robot.subsystems.turrent;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public interface turrentIO {
  @AutoLog
  public class turrentIOInputs {
    public double topMotorCurrent = 0.0;
    public double turrentAngle = 0.0;
    public double gearRatio = 1.0;
    public Mechanism2d turnMechanism = new Mechanism2d(1,1);
    public MechanismRoot2d root = turnMechanism.getRoot("turn root thingamabobimajigger", 0,0);
    public MechanismLigament2d turentTurn = root.append(new MechanismLigament2d("turrent direction", 1, 0));
  }

  // Update inputs
  public void updateInputs(turrentIOInputs inputs);

  // Set motor to angle
  public void setTurrentAngle(double degrees);
}
