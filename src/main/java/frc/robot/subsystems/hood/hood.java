package frc.robot.subsystems.hood;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.Vision;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class hood extends SubsystemBase {
  public hoodIO io;
  public boolean isFerryMode = false;
  // public final double maxHoodPos = -9.342285;
  public final double maxHoodPos = -9.0;
  public final double minHoodPos = -0.05;
  private double defaultPosition = 0.0;
  private double CONSTANT = 1;

  public double positionToDeg(double pos){
      return pos * CONSTANT;
  }

  public void periodic() {
    // System.out.println("Degrees: " + posToDeg(() -> io.getDegrees()));
    Logger.recordOutput("Hood Angle", io.getPos());
  }

  public double posToDeg(DoubleSupplier yeah) {
    return (3.48) * yeah.getAsDouble() + 63.035;
  }

  public double degToPos(DoubleSupplier no) {
    return (no.getAsDouble() - 63.035) / 3.48;
  }

  public hood(hoodIO givenIo) {
    io = givenIo;
  }

  public Command spinHood(DoubleSupplier speed) {
    return Commands.run(() -> io.turnHood(speed.getAsDouble()));
  }

  public Command setHoodPosCommand(DoubleSupplier pos) {
    return Commands.run(() -> io.setHoodPos(pos.getAsDouble()), this);
  }

  public void setHoodPosVoid(DoubleSupplier pos) {
    io.setHoodPos(pos.getAsDouble());
  }

  public Command setHoodAngle(DoubleSupplier degrees) {
    // System.out.println(degrees.getAsDouble());
    return Commands.run(() -> io.setHoodAngleDegrees(degrees.getAsDouble()));
  }

  public Command setHoodAngleVision(Vision eyes, int CamIndex) {
    return Commands.run(
        () ->
            io.setHoodAngleDegrees(
                // MathUtil.clamp(eyes.getDistanceToSpecificTag(CamIndex, 10), 0.0, 5.0)));
                -10.0),
        this);
  }

  public Command toggleHoodPosition() {
    return Commands.run(() -> io.toggleHoodPosition(), this);
  }

  public Command setFerryMode(boolean ferryMode) {
    this.isFerryMode = ferryMode;
    return Commands.run(() -> io.setHoodPos(ferryMode ? this.maxHoodPos : this.minHoodPos), this);
  }

  public boolean isFerryMode() {
    return this.isFerryMode;
  }

  public void setDefaultPosition(double defPos) {
    this.defaultPosition = defPos;
  }

  public Command setHoodDefaultPositionCommand(DoubleSupplier stick) {
    return Commands.run(() -> io.setHoodPos(this.defaultPosition - stick.getAsDouble()), this);
  }

  public void setHoodDefaultPositionVoid() {
    io.setHoodPos(this.defaultPosition);
  }
}
