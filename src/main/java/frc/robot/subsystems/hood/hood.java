package frc.robot.subsystems.hood;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.DoubleSupplier;

public class hood {
  public hoodIO io;

  public hood(hoodIO givenIo) {
    io = givenIo;
  }

  public Command spinHood(DoubleSupplier speed) {
    return Commands.run(() -> io.turnHood(speed.getAsDouble()));
  }

  public Command setHoodPosCommand(DoubleSupplier pos) {
    return Commands.run(() -> io.setHoodPos(pos.getAsDouble()));
  }

  public Command setHoodAngle(DoubleSupplier degrees) {
    return Commands.run(() -> io.setHoodAngleDegrees(degrees.getAsDouble()));
  }
}
