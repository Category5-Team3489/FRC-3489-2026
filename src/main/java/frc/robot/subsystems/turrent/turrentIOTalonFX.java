package frc.robot.subsystems.turrent;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public class turrentIOTalonFX implements turrentIO {
  // Create motors
  private final com.ctre.phoenix6.hardware.TalonFX topMotor;
  private final Encoder tuffEncoder;
  // The mechanical gear ratio between the encoder shaft and the turret main
  // output: 50 encoder rotations == 1 turret rotation.
  private static final double ENCODER_TO_TURRET_RATIO = 50.0;
  // Default counts-per-revolution for the attached quadrature encoder. Change
  // if your encoder has a different CPR (eg 1024, 2048, 4096).
  private static final int DEFAULT_ENCODER_CPR = 2048;
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
    this(topMotorPort, 0, 1);
  }

  /**
   * Construct with Talon FX port and specific DIO channels for a quadrature encoder.
   *
   * @param topMotorPort CAN device ID for the TalonFX
   * @param encoderChannelA DIO channel for encoder A
   * @param encoderChannelB DIO channel for encoder B
   */
  public turrentIOTalonFX(int topMotorPort, int encoderChannelA, int encoderChannelB) {
    topMotor = new com.ctre.phoenix6.hardware.TalonFX(topMotorPort);
    tuffEncoder = new Encoder(encoderChannelA, encoderChannelB);
    // Configure encoder so getDistance() returns degrees of turret rotation.
    // distancePerPulse = degrees per encoder pulse = 360 deg / (CPR * gearRatio)
    double distancePerPulse = 360.0 / (DEFAULT_ENCODER_CPR * ENCODER_TO_TURRET_RATIO);
    tuffEncoder.setDistancePerPulse(distancePerPulse);
  }

  @Override
  public void updateInputs(turrentIOInputs inputs) {
    inputs.topMotorCurrent = topMotor.getSupplyVoltage().getValueAsDouble();
    // Read encoder distance which is configured to return turret degrees.
    double turretDegrees = tuffEncoder.getDistance();
    inputs.turrentAngle = turretDegrees;
    // Update visualization
    turentTurn.setAngle(inputs.turrentAngle);
  }

  @Override
  public void turnTurrent(double speed) {
    topMotor.set(speed);
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
