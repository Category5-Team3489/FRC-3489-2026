package frc.robot.subsystems.turrent;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public class turrentIOTalonFX implements turrentIO {
  // Create motors
  private static final double DEFAULT_GEAR_RATIO = 50.0;
  private final double gearRatio;
  private final com.ctre.phoenix6.hardware.TalonFX topMotor;
  private final CANcoder tuffEncoder;
  private final PositionDutyCycle positionRequest = new PositionDutyCycle(0.0);
  // Allowed absolute-position window (rotations) for turret travel.
  // Requested range is -0.75 to 0.75 on a -1 to 1 scale.
  private static final double MIN_TURRET_POS = -11.25;
  private static final double MAX_TURRET_POS = 12.25;
  // private final double ENCODER_START_POSITION = -5;
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
    this(topMotorPort, 0, DEFAULT_GEAR_RATIO);
  }

  /**
   * Construct with Talon FX port and specific DIO channels for a quadrature encoder.
   *
   * @param topMotorPort CAN device ID for the TalonFX
   * @param encoderChannelA DIO channel for encoder A
   * @param encoderChannelB DIO channel for encoder B
   */
  public turrentIOTalonFX(int topMotorPort, int cancoderId) {
    this(topMotorPort, cancoderId, DEFAULT_GEAR_RATIO);
  }

  public turrentIOTalonFX(int topMotorPort, int cancoderId, double gearRatio) {
    this.topMotor = new TalonFX(topMotorPort);
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kP = 0.2;
    config.Slot0.kI = 0.0;
    config.Slot0.kD = 0.0;
    topMotor.getConfigurator().apply(config);
    // Create CANcoder on the configured CAN bus
    tuffEncoder = new CANcoder(cancoderId);
    // tuffEncoder.setPosition(0);
    this.gearRatio = gearRatio;
  }

  @Override
  public double getCurrentAngle() {
    return getTurretDegrees();
  }

  @Override
  public void updateInputs(turrentIOInputs inputs) {
    inputs.topMotorCurrent = topMotor.getSupplyCurrent().getValueAsDouble();
    inputs.gearRatio = gearRatio;
    inputs.turrentAngle = getTurretDegrees();
    // Update visualization
    turentTurn.setAngle(inputs.turrentAngle);
  }

  @Override
  public void turnTurrent(double speed) {
    double turretPos = tuffEncoder.getAbsolutePosition().getValueAsDouble();
    double commandedSpeed = speed;

    // System.out.println("turrent speed: " + speed);
    // Only block motion that would drive farther outside the allowed window.
    if (turretPos <= MIN_TURRET_POS && speed < 0) {
      // this.topMotor.clearStickyFault_ForwardSoftLimit();
      commandedSpeed = 0.0;
    } else if (turretPos >= MAX_TURRET_POS && speed > 0) {
      // this.topMotor.clearStickyFault_ReverseSoftLimit();
      commandedSpeed = 0.0;
    }

    topMotor.set(commandedSpeed);
  }

  @Override
  public void resetTurrentAngle() {
    tuffEncoder.setPosition(0);
  }

  @Override
  public void setTurrentAngle(double degrees) {
    // Convert requested angle to motor rotations.
    double turretRotations = degrees / 360.0;
    double motorRotations = turretRotations * gearRatio;
    topMotor.setControl(positionRequest.withPosition(motorRotations));
  }

  private double getTurretDegrees() {
    // CANCoder absolute position is in turret rotations (0 to 1).
    double turretRotations = tuffEncoder.getAbsolutePosition().getValueAsDouble();
    return turretRotations * 360.0;
  }
}
