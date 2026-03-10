package frc.robot.subsystems.indexer;

import edu.wpi.first.math.MathUtil;

public class indexIOTalonFX implements indexIO {

  private final double maxVolts = 30;
  private final double minVolts = -30;
  private final double eStopVolt = 40;
  // private final indexIOInputs inputs = new indexIOInputs();
  private final com.ctre.phoenix6.hardware.TalonFX indexMotor;

  public indexIOTalonFX(int motorPortT) {
    // Initialize hardware here (e.g., motor controllers)
    indexMotor = new com.ctre.phoenix6.hardware.TalonFX(motorPortT);
  }

  @Override
  public void turnMotor(double speed) {
    double currentVolts = indexMotor.getSupplyCurrent().getValueAsDouble();
    double setSpeed = speed * 12.0;

    // Clamp it, if the current voltage is above the estop limit then emergency stop the indexer
    // min and max volts arent set at the top

    setSpeed = MathUtil.clamp(setSpeed, minVolts, maxVolts);

    if (currentVolts > eStopVolt || currentVolts < -eStopVolt) {
      setSpeed = 0;
    }

    indexMotor.setVoltage(setSpeed);
  }
}
