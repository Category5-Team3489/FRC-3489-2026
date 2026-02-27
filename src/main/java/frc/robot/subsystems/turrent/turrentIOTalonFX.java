package frc.robot.subsystems.turrent;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public class turrentIOTalonFX implements turrentIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFX topMotor;
  private final CANcoder tuffEncoder;
  // The mechanical gear ratio between the encoder (motor/shaft) and the turret
  // output: 50 encoder/motor rotations == 1 turret rotation.
  private static final double ENCODER_TO_TURRET_RATIO = 50.0;
  // Allowed absolute-position window (rotations) for turret travel.
  // Requested range is -0.75 to 0.75 on a -1 to 1 scale.
  private static final double MIN_TURRET_POS = -10;
  private static final double MAX_TURRET_POS = 10;
  private final turrentIOInputs inputs = new turrentIOInputs();
  // Local dashboard visualization (do not include in AutoLog inputs)
  private final Mechanism2d turnMechanism = new Mechanism2d(1, 1);
  private final MechanismRoot2d root = turnMechanism.getRoot("turn root", 0, 0);
  private final MechanismLigament2d turentTurn =
      root.append(new MechanismLigament2d("turrent direction", 1, 0));

  /**
   * Construct with Talon FX port and default DIO channels (0/1) for encoder. Assumes a quadrature
   * encoder on DIO 0/1 and an encoder CPR of DEFAULT_ENCODER_CPR; change the overload below to pass
   * custom channels.
   */
  public turrentIOTalonFX(int topMotorPort) {
    this(topMotorPort, 0);
  }

  /**
   * Construct with Talon FX port and specific DIO channels for a quadrature encoder.
   *
   * @param topMotorPort CAN device ID for the TalonFX
   * @param encoderChannelA DIO channel for encoder A
   * @param encoderChannelB DIO channel for encoder B
   */
  public turrentIOTalonFX(int topMotorPort, int cancoderId) {
    topMotor = new TalonFX(topMotorPort);
    // Create CANcoder on the configured CAN bus
    tuffEncoder = new CANcoder(cancoderId);
  }

  @Override
  public double getCurrentAngle() {
    return topMotor.getPosition().getValueAsDouble();
  }

  @Override
  public void updateInputs(turrentIOInputs inputs) {
    inputs.topMotorCurrent = topMotor.getSupplyVoltage().getValueAsDouble();
    // Read CANCoder absolute position (returns rotations). Convert motor
    // rotations to turret rotations using ENCODER_TO_TURRET_RATIO, then to
    // degrees.
    double motorRotations = tuffEncoder.getAbsolutePosition().getValueAsDouble();
    double turretRotations = motorRotations / ENCODER_TO_TURRET_RATIO;
    double turretDegrees = turretRotations * 360.0;
    inputs.turrentAngle = turretDegrees;
    // Update visualization
    turentTurn.setAngle(inputs.turrentAngle);
  }

  @Override
  public void turnTurrent(double speed) {
    double turretPos = tuffEncoder.getPosition().getValueAsDouble();
    double commandedSpeed = speed;

    // Only block motion that would drive farther outside the allowed window.
    if (turretPos <= MIN_TURRET_POS && speed > 0) {
      commandedSpeed = 0.0;
    } else if (turretPos >= MAX_TURRET_POS && speed < 0) {
      commandedSpeed = 0.0;
    }

    topMotor.set(commandedSpeed);
  }

  @Override
  public void setTurrentAngle(double degrees) {
    // Convert turret degrees to motor rotations. ENCODER_TO_TURRET_RATIO is the
    // number of encoder/motor rotations per one turret rotation (50:1).
    double motorRotations = (degrees / 360.0) * ENCODER_TO_TURRET_RATIO;

    // PositionDutyCycle expects motor rotations as the position setpoint.
    PositionDutyCycle request = new PositionDutyCycle(motorRotations);
    topMotor.setControl(request);
  }
}
