package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class VisionIOInputsAutoLogged extends VisionIO.VisionIOInputs
    implements LoggableInputs {
  @Override
  public void toLog(LogTable table) {}

  @Override
  public void fromLog(LogTable table) {}
}
