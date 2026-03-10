package frc.robot.subsystems.drive;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class GyroIOInputsAutoLogged extends GyroIO.GyroIOInputs implements LoggableInputs {
  @Override
  public void toLog(LogTable table) {}

  @Override
  public void fromLog(LogTable table) {}
}
